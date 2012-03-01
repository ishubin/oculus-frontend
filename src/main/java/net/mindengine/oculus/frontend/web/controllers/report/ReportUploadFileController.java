/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.web.controllers.SimpleViewController;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class ReportUploadFileController extends SimpleViewController {
	private Log log = LogFactory.getLog(getClass());
	private Config config;
	private Long fileId = 0L;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		log.info("uploading file");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {

			DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest) request;

			int i = 0;
			boolean bNext = true;
			while (bNext) {
				i++;
				MultipartFile multipartFile = multipartRequest.getFile("file" + i);
				if (multipartFile != null) {
					fileId++;
					Date date = new Date();
					String path = FileUtils.generatePath(date);
					FileUtils.mkdirs(config.getDataFolder() + File.separator + path);

					String fileType = FileUtils.getFileType(multipartFile.getOriginalFilename()).toLowerCase();
					path += File.separator + date.getTime() + "_" + fileId + "." + fileType;

					File file = new File(config.getDataFolder() + File.separator + path);
					file.createNewFile();

					FileOutputStream fos = new FileOutputStream(file);
					fos.write(multipartFile.getBytes());
					fos.close();

					if (fileType.equals("png") || fileType.equals("jpg") || fileType.equals("gif") || fileType.equals("bmp")) {
						pw.println("[uploaded]?type=image-" + fileType + "&object=" + date.getTime() + "&item=" + fileId);
					}
					if (fileType.equals("txt") || fileType.equals("json") || fileType.equals("xml")) {
						pw.println("[uploaded]?type=text-" + fileType + "&object=" + date.getTime() + "&item=" + fileId);
					}
				}
				else
					bNext = false;
			}
		}
		else
			pw.print("[error]not a multipart content");
		return null;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
