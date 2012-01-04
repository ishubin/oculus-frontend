package net.mindengine.oculus.frontend.web.controllers.display;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.experior.utils.FileUtils;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * This controller is used for fetching all files which were saved to dataFolder
 * 
 * @author Ivan Shubin
 * @see Config
 */
public class FileDisplayController implements Controller {
	private Config config;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		if (type == null)
			throw new InvalidRequest();

		if (type.equals("project-icon")) {
			Long objectId = Long.parseLong(request.getParameter("object"));
			Long projectId = Long.parseLong(request.getParameter("projectId"));
			String path = config.getDataFolder() + File.separator + "projects" + File.separator + projectId + File.separator + "icon_" + objectId + ".png";

			showFile(response, path, "icon_" + objectId + ".png", "image/png");
		}
		else if (type.equals("image-png")) {
			showImage("png", request, response);
		}
		else if (type.equals("image-jpg")) {
			showImage("jpg", request, response);
		}
		else if (type.startsWith("text-")) {
			String extension = type.substring(5);
			showText(extension, request, response);
		}
		else
			throw new InvalidRequest();

		return null;
	}

	public void showText(String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Long objectId = Long.parseLong(request.getParameter("object"));
		Long itemId = Long.parseLong(request.getParameter("item"));

		String path = config.getDataFolder() + File.separator + FileUtils.generatePath(new Date(objectId)) + File.separator + objectId + "_" + itemId + "." + type;

		showTextFile(response, path, itemId + "." + type, "text/" + type);
	}

	public void showImage(String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Long objectId = Long.parseLong(request.getParameter("object"));
		Long itemId = Long.parseLong(request.getParameter("item"));

		String path = config.getDataFolder() + File.separator + FileUtils.generatePath(new Date(objectId)) + File.separator + objectId + "_" + itemId + "." + type;

		showFile(response, path, itemId + "." + type, "image/" + type);
	}

	public static void showTextFile(HttpServletResponse response, String path, String fileName, String contentType) throws IOException {

		OutputStream os = response.getOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(os);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
		String content = readFileAsString(path);
		w.write(content);
		w.flush();
		os.flush();
		os.close();
	}

	private static String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	public static void showFile(HttpServletResponse response, String path, String fileName, String contentType) throws IOException {
		File file = new File(path);
		response.setBufferSize((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		response.setContentType(contentType);
		response.setContentLength((int) file.length());

		byte[] bytes = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(bytes);
		fis.close();

		FileCopyUtils.copy(bytes, response.getOutputStream());
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
