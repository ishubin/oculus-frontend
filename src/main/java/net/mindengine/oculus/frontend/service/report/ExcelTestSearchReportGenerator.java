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
package net.mindengine.oculus.frontend.service.report;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.customization.CustomizationPossibleValue;
import net.mindengine.oculus.frontend.domain.customization.UnitCustomizationValue;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

/**
 * Used to generate Excel spreadsheet with all tests or test-cases which appear in the specified search result.
 * Used only from "Tests Search" and "Test-Cases Search"
 * @author ishubin
 *
 */
public class ExcelTestSearchReportGenerator {

    private String unit;
    private CustomizationDAO customizationDAO;
    private UserDAO userDAO;
    /**
     * Generates Excel spreadsheet.
     *
     * @param searchResult Search result for test or document (test-case)
     * @throws Exception 
     */
    public void writeExcelReports(BrowseResult<?> searchResult, Long projectId, HttpServletRequest request, HttpServletResponse response ) throws Exception{
        
        int cellOffset = 5;
        /*
         * Customizations which will be exported to XLS spreadsheet.
         */
        Collection<Customization> customizationsExport = new LinkedList<Customization>();
        
        /*
         * Here will be cell ids stored for each customization. This is needed because there might be a lot of merged cells for list customizations
         */
        Map<Long, Integer> customizationCells = new HashMap<Long, Integer>();
        
        if (projectId!=null && projectId>0) {
            Collection<Customization> customizations = customizationDAO.getCustomizations(projectId, unit);
            for(Customization customization : customizations){
                
                //Checking if the user has selected this specific customization for exporting
                if("on".equals(request.getParameter("cexport"+customization.getId()))){
                    
                    customizationCells.put(customization.getId(), cellOffset);
                    
                    //Checking if the customization contains possible values and fetching them all
                    if(customization.getType().equals(Customization.TYPE_CHECKLIST) || customization.getType().equals(Customization.TYPE_LIST)) {
                        customization.setPossibleValues(customizationDAO.getCustomizationPossibleValues(customization.getId()));
                        cellOffset += customization.getPossibleValues().size();
                    }
                    else{
                        cellOffset += 1;
                    }
                     
                    customizationsExport.add(customization);
                }
            }
        }
        
        /*
         * Generating the Excel spreadsheet
         */

        OutputStream outputStream = response.getOutputStream();
        response.setContentType("application/ms-excel");

        HSSFWorkbook workBook = new HSSFWorkbook();

        HSSFSheet sheet = workBook.createSheet();
        
        HSSFFont fontHeader = workBook.createFont();
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontHeader.setColor(HSSFColor.WHITE.index);
        
        HSSFCellStyle columnHeaderStyle = workBook.createCellStyle();
        columnHeaderStyle.setBorderTop((short) 2);
        columnHeaderStyle.setBorderLeft((short) 2);
        columnHeaderStyle.setBorderRight((short) 2);
        columnHeaderStyle.setBorderBottom((short) 2);
        columnHeaderStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        columnHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        columnHeaderStyle.setFont(fontHeader);
        
        HSSFCellStyle columnRootHeaderStyle = workBook.createCellStyle();
        
        //columnRootHeaderStyle.cloneStyleFrom(columnHeaderStyle);
        columnRootHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFCell cell;
        
        HSSFRow headerRow = sheet.createRow(0);
        HSSFRow header2Row = sheet.createRow(1);
        
        
        sheet.addMergedRegion(new Region((short)0, (short)0, (short)0, (short)4));
        cell = headerRow.createCell((short) 0);
        cell.setCellValue("Common");
        cell.setCellStyle(columnRootHeaderStyle);
        
        cell = header2Row.createCell((short) 0);
        cell.setCellValue("Test");
        cell.setCellStyle(columnHeaderStyle);
        
        cell = header2Row.createCell((short) 1);
        cell.setCellValue("Project");
        cell.setCellStyle(columnHeaderStyle);
        
        cell = header2Row.createCell((short) 2);
        cell.setCellValue("Sub-Project");
        cell.setCellStyle(columnHeaderStyle);

        cell = header2Row.createCell((short) 3);
        cell.setCellValue("Author");
        cell.setCellStyle(columnHeaderStyle);
        
        cell = header2Row.createCell((short) 4);
        cell.setCellValue("Created");
        cell.setCellStyle(columnHeaderStyle);


        
        for(Customization customization : customizationsExport) {
            int cellId = customizationCells.get(customization.getId());
            if(customization.getPossibleValues()!=null){
                int size = customization.getPossibleValues().size();
                if(size>1) {
                    sheet.addMergedRegion(new Region((short)0, (short)0, (short)cellId, (short)(cellId+size-1)));
                }
                
                /*
                 * Exporting possible values 
                 */
                int offset = 0;
                for(CustomizationPossibleValue cpv : customization.getPossibleValues()){
                    cell = header2Row.createCell((short) (cellId+offset));
                    cell.setCellValue(cpv.getPossibleValue());
                    cell.setCellStyle(columnHeaderStyle);
                    offset++;
                }
            }
            else {
                cell = header2Row.createCell((short) cellId);
                cell.setCellStyle(columnHeaderStyle);
            }
            cell = headerRow.createCell((short) cellId);
            cell.setCellValue(customization.getName());
            cell.setCellStyle(columnRootHeaderStyle);
        }
        
        
        HSSFCellStyle cellStyle = workBook.createCellStyle();
        
        HSSFCellStyle checkboxStyle = workBook.createCellStyle();
        checkboxStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        checkboxStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        checkboxStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        HSSFFont fontCheckbox = workBook.createFont();
        fontCheckbox.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontCheckbox.setColor(HSSFColor.BLACK.index);
        checkboxStyle.setFont(fontCheckbox);
        
        HSSFCellStyle boolYesStyle = workBook.createCellStyle();
        boolYesStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        boolYesStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle boolNoStyle = workBook.createCellStyle();
        boolNoStyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        boolNoStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        /*
         * Used in order to cache user names. Key = ID, Value = User name
         */
        Map<Long, String> usersCache = new HashMap<Long, String>();
        
        int j=2;
        for(Object object : searchResult.getResults()){
            HSSFRow row = sheet.createRow(j);
            
            String name, parentProjectName, projectName, authorName;
            Date date;
            Long objectId;
            
            if(object instanceof Test){
                Test test = (Test)object;
                name = test.getName();
                parentProjectName = test.getParentProjectName();
                projectName = test.getProjectName();
                authorName = test.getAuthorName();
                objectId = test.getId();
                date = test.getDate();
            }
            else throw new IllegalArgumentException(object.getClass().getName());
            
            cell = row.createCell((short) 0);
            cell.setCellValue(name);
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) 1);
            cell.setCellValue(parentProjectName);
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) 2);
            cell.setCellValue(projectName);
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) 3);
            cell.setCellValue(authorName);
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) 4);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            cell.setCellValue(sdf.format(date));
            cell.setCellStyle(cellStyle);
            
            int offset = 5;
            
            for(Customization customization : customizationsExport) {
                UnitCustomizationValue ucv = customizationDAO.getUnitCustomizationValue(customization.getId(), objectId);
                
                if(customization.getType().equals(Customization.TYPE_CHECKLIST) || customization.getType().equals(Customization.TYPE_LIST)) {
                    for(CustomizationPossibleValue cpv : customization.getPossibleValues()){
                        
                        boolean matches = false;
                        
                        if(customization.getType().equals(Customization.TYPE_LIST)){
                            if(ucv!=null && ucv.getValue()!=null && ucv.getValue().equals(""+cpv.getId())){
                                matches = true;
                            }
                        }
                        else {
                            String s = "("+cpv.getId()+")";
                            if(ucv!=null && ucv.getValue()!=null && ucv.getValue().contains(s)){
                                matches = true;
                            }
                        }
                        
                        
                        if(matches){
                            cell = row.createCell((short) offset);
                            cell.setCellValue("X");
                            cell.setCellStyle(checkboxStyle);
                        }
                        offset++;
                    }
                }
                else{
                    if(ucv!=null){
                        cell = row.createCell((short) offset);
                        cell.setCellStyle(cellStyle);
                        
                        if(customization.getType().equals(Customization.TYPE_ASSIGNEE)){
                            if(ucv.getValue()!=null && !ucv.getValue().isEmpty()){
                                try{
                                    Long userId = Long.parseLong(ucv.getValue());
                                    /*
                                     * Chaching user names by their ids
                                     */
                                    String userName = null;
                                    if(!usersCache.containsKey(userId)){
                                        User user = userDAO.getUserById(userId);
                                        if(user!=null){
                                            userName = user.getName();
                                        }
                                        else userName = "";
                                        
                                        usersCache.put(userId, userName);
                                    }
                                    else userName = usersCache.get(userId);
                                    
                                    cell.setCellValue(userName);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if(customization.getType().equals(Customization.TYPE_CHECKBOX)) {
                            if(ucv.getValue()!=null){
                                if(ucv.getValue().equals("true")){
                                    cell.setCellValue("Yes");
                                    cell.setCellStyle(boolYesStyle);
                                }
                                else{
                                    cell.setCellValue("No");
                                    cell.setCellStyle(boolNoStyle);
                                }
                            }
                        }
                        else{
                            cell.setCellValue(ucv.getValue());
                        }
                        
                    }
                    offset++;
                }
            }
            j++;
        }

        
        /*
         * Making the text to fit in all cells 
         */
        for(short i=0;i<(short)cellOffset+1;i++){
            sheet.autoSizeColumn(i);
        }
        
        workBook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
    
    
    public CustomizationDAO getCustomizationDAO() {
        return customizationDAO;
    }

    public void setCustomizationDAO(CustomizationDAO customizationDAO) {
        this.customizationDAO = customizationDAO;
    }


    public String getUnit() {
        return unit;
    }


    public void setUnit(String unit) {
        this.unit = unit;
    }


    public UserDAO getUserDAO() {
        return userDAO;
    }


    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
