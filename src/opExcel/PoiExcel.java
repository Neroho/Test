package opExcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jxl.write.Label;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiExcel {
	public void writeintoxls(){
		String[] title ={"id","name","sex"};
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row  = sheet.createRow(0);
		HSSFCell cell=null;
		for(int i=0;i<title.length;i++){
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}
		
		for(int i=1;i<=10;i++){
			HSSFRow nextrow = sheet.createRow(i);
			HSSFCell cell2 = nextrow.createCell(0);
			cell2.setCellValue("a"+i);
			cell2 = nextrow.createCell(1);
			cell2.setCellValue("user"+i);
			cell2 = nextrow.createCell(2);
			cell2.setCellValue("boy");
		}
		
		File file = new File("d:/poi_test.xls");
		try {
			file.createNewFile();
			FileOutputStream stream =FileUtils.openOutputStream(file);
			workbook.write(stream);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeintoxlsx(){
		String[] title ={"id","name","sex"};
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row  = sheet.createRow(0);
		XSSFCell cell=null;
		for(int i=0;i<title.length;i++){
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}
		
		for(int i=1;i<=10;i++){
			XSSFRow nextrow = sheet.createRow(i);
			XSSFCell cell2 = nextrow.createCell(0);
			cell2.setCellValue("a"+i);
			cell2 = nextrow.createCell(1);
			cell2.setCellValue("user"+i);
			cell2 = nextrow.createCell(2);
			cell2.setCellValue("boy");
		}
		
		File file = new File("d:/poi_test.xlsx");
		try {
			file.createNewFile();
			FileOutputStream stream =FileUtils.openOutputStream(file);
			workbook.write(stream);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void read(){
		File file=new File("d:/poi_test.xls");
		try {
			HSSFWorkbook workbook = 
					new HSSFWorkbook(FileUtils.openInputStream(file));
			HSSFSheet sheet =workbook.getSheet("Sheet0");
			int firstRowNum = 0;
			int lastRowNum = sheet.getLastRowNum();
			for(int i =0;i<=lastRowNum;i++){
				HSSFRow row = sheet.getRow(i);
				int lastCellNum=row.getLastCellNum();
				for(int j=0;j<lastCellNum;j++)
				{
					HSSFCell cell = row.getCell(j);
					String value=cell.getStringCellValue();
					System.out.print(value+" ");
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]){
		new PoiExcel().writeintoxls();
		new PoiExcel().writeintoxlsx();
		new PoiExcel().read();
		
	}
}
