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
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
	public static String readFile(String path) throws FileNotFoundException, IOException {
		return readFile(new File(path));
	}

	public static String readFile(File file) throws FileNotFoundException, IOException {
		FileReader reader = new FileReader(file);
		char buffer[] = new char[(int) file.length()];
		reader.read(buffer);
		return new String(buffer);
	}

	public static String generatePath(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(date);
		String convertedDate = strDate;
		convertedDate = convertedDate.replace("-", File.separator);
		return convertedDate;
	}

	public static void removeDirectory(File folder) {
		if (folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				removeDirectory(file);
			}
		}

		folder.delete();
	}

	public static void removeFile(String path) {
		File file = new File(path);
		file.delete();
	}

	/**
	 * Removes all not allowed symbols
	 * 
	 * @param name
	 *            Name of the file
	 * @return the name of the file with all alowed symbols
	 */
	public static String convertName(String name) {
		name = name.replaceAll("[^a-zA-Z 0-9]+", "");
		return name;
	}

	public static String getFileType(String fileName) {
		for (int i = fileName.length() - 1; i >= 0; i--) {
			if (fileName.charAt(i) == '.') {
				return fileName.substring(i + 1);
			}
		}
		return null;
	}

	public static String getFileSimpleName(String fileName) {
		for (int i = fileName.length() - 1; i >= 0; i--) {
			if (fileName.charAt(i) == '.') {
				return fileName.substring(0, i);
			}
		}
		return fileName;
	}

	public static void zipDirectory(String zipFileName, String dir) throws Exception {
		File dirObj = new File(dir);
		File zipFile = new File(zipFileName);
		if (!zipFile.createNewFile()) {
			throw new RuntimeException("Couldn't create file " + zipFileName);
		}

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		addDir(dirObj, out);
		out.close();
	}

	private static void addDir(File dirObj, ZipOutputStream out) throws IOException {
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(files[i], out);
				continue;
			}
			FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
			System.out.println(" Adding: " + files[i].getAbsolutePath());
			out.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}
}
