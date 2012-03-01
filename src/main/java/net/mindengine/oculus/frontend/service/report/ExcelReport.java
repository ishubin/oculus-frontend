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
package net.mindengine.oculus.frontend.service.report;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.report.SavedRun;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelReport {
	private OutputStream outputStream;
	private Config config;

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void writeDocument(SavedRun savedRun, List<TestRunSearchData> testRuns) throws IOException {
		HSSFWorkbook workBook = new HSSFWorkbook();

		HSSFSheet sheet = workBook.createSheet();
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCellStyle columnHeaderStyle = workBook.createCellStyle();
		columnHeaderStyle.setBorderTop((short) 1);
		columnHeaderStyle.setBorderLeft((short) 1);
		columnHeaderStyle.setBorderRight((short) 1);
		columnHeaderStyle.setBorderBottom((short) 1);
		columnHeaderStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		columnHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCell cell;

		
		cell = headerRow.createCell((short) 0);
		cell.setCellValue(new HSSFRichTextString("Log"));
		cell.setCellStyle(columnHeaderStyle);

		cell = headerRow.createCell((short) 1);
		cell.setCellValue(new HSSFRichTextString("Test Run Id"));
		cell.setCellStyle(columnHeaderStyle);

		cell = headerRow.createCell((short) 2);
		cell.setCellValue(new HSSFRichTextString("Test Name"));
		cell.setCellStyle(columnHeaderStyle);

		cell = headerRow.createCell((short) 3);
		cell.setCellValue(new HSSFRichTextString("Project"));
		cell.setCellStyle(columnHeaderStyle);

		cell = headerRow.createCell((short) 4);
		cell.setCellValue(new HSSFRichTextString("Status"));
		cell.setCellStyle(columnHeaderStyle);

		cell = headerRow.createCell((short) 5);
		cell.setCellValue(new HSSFRichTextString("Designer"));
		cell.setCellStyle(columnHeaderStyle);

		cell = headerRow.createCell((short) 6);
		cell.setCellValue(new HSSFRichTextString("Runner"));
		cell.setCellStyle(columnHeaderStyle);

		cell = headerRow.createCell((short) 7);
		cell.setCellValue(new HSSFRichTextString("Start Time"));
		cell.setCellStyle(columnHeaderStyle);

		int id = 0;

		HSSFCellStyle rowPassedStyle = workBook.createCellStyle();
		rowPassedStyle.setBorderTop((short) 1);
		rowPassedStyle.setBorderLeft((short) 1);
		rowPassedStyle.setBorderRight((short) 1);
		rowPassedStyle.setBorderBottom((short) 1);
		rowPassedStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		rowPassedStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		rowPassedStyle.setWrapText(true);

		HSSFCellStyle rowWarnStyle = workBook.createCellStyle();
		rowWarnStyle.setBorderTop((short) 1);
		rowWarnStyle.setBorderLeft((short) 1);
		rowWarnStyle.setBorderRight((short) 1);
		rowWarnStyle.setBorderBottom((short) 1);
		rowWarnStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		rowWarnStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		rowWarnStyle.setWrapText(true);

		HSSFCellStyle rowFailStyle = workBook.createCellStyle();
		rowFailStyle.setBorderTop((short) 1);
		rowFailStyle.setBorderLeft((short) 1);
		rowFailStyle.setBorderRight((short) 1);
		rowFailStyle.setBorderBottom((short) 1);
		rowFailStyle.setFillForegroundColor(HSSFColor.RED.index);
		rowFailStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		rowFailStyle.setWrapText(true);

		HSSFCellStyle cellStyle = null;
		for (TestRunSearchData run : testRuns) {
			if (run.getTestRunStatus().equals("PASSED")) {
				cellStyle = rowPassedStyle;
			}
			else if (run.getTestRunStatus().equals("FAILED")) {
				cellStyle = rowFailStyle;
			}
			else
				cellStyle = rowWarnStyle;
			id++;
			HSSFRow row = sheet.createRow(id);

			cell = row.createCell((short) 0);
			cell.setCellValue(new HSSFRichTextString("http://" + config.getOculusServerUrl() + "/report/report-" + run.getTestRunId()));
			cell.setCellStyle(cellStyle);

			cell = row.createCell((short) 1);
			cell.setCellValue(run.getTestRunId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell((short) 2);
			cell.setCellValue(new HSSFRichTextString(run.getFetchTestName()));
			cell.setCellStyle(cellStyle);

			cell = row.createCell((short) 3);
			cell.setCellValue(new HSSFRichTextString(run.getFetchProjectName()));
			cell.setCellStyle(cellStyle);

			cell = row.createCell((short) 4);
			cell.setCellValue(new HSSFRichTextString(run.getTestRunStatus()));
			cell.setCellStyle(cellStyle);

			cell = row.createCell((short) 5);
			cell.setCellValue(new HSSFRichTextString(run.getDesignerName()));
			cell.setCellStyle(cellStyle);

			cell = row.createCell((short) 6);
			cell.setCellValue(new HSSFRichTextString(run.getRunnerName()));
			cell.setCellStyle(cellStyle);

			cell = row.createCell((short) 7);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss");
			cell.setCellValue(new HSSFRichTextString(sdf.format(run.getTestRunStartTime())));
			cell.setCellStyle(cellStyle);

		}

		workBook.write(outputStream);

	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
