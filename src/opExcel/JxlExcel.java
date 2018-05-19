package opExcel;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class JxlExcel {
	
	public void Writeinto() {
		String[] title ={"id","name","sex"};
		File file = new File("d:/jxl_test.xls");
		
		try {
			file.createNewFile();
			WritableWorkbook workbook =Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("sheet1", 0);
			Label label = null;
			//第一行设置列名
			for (int i = 0;i<title.length;i++){
				label = new  Label(i,0,title[i]);
				sheet.addCell(label);
			}
			//追加数据
			for(int i=1;i<=10;i++){
				label = new Label(0,i,"a"+i);
				sheet.addCell(label);
				label = new Label(1,i,"user"+i);
				sheet.addCell(label);
				label = new Label(2,i,"男");
				sheet.addCell(label);
			}
			//write into the excel
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readExcel(){
		try{
			Workbook workbook = Workbook.getWorkbook(new File("d:/jxl_test.xls"));
			Sheet sheet = workbook.getSheet(0);
			for (int i=0;i<sheet.getRows();i++){
				for (int j=0;j<sheet.getColumns();j++){
					Cell cell = sheet.getCell(j,i);
					System.out.print(cell.getContents()+" ");
				}
				System.out.println();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new JxlExcel().Writeinto();
		new JxlExcel().readExcel();
	}
}
