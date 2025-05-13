package herbomics.test;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExtractTaxonomy {

	private static final String fileName = "dataset.xlsx";
	
	private static final String outputFileName = "output_dataset.xlsx";
	
	public static void main(String[] args) {
		
		LotusMongo lotusMongo = new LotusMongo();
		
		try (Workbook workbook = new XSSFWorkbook(
				DataLoader.class.getClassLoader().getResourceAsStream(fileName))) {
			Sheet sheet = workbook.getSheetAt(0);

	        // 跳过表头
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell lotusIdCell = row.getCell(11);
                if (lotusIdCell != null) {
                    String lotusId = lotusIdCell.toString();
                    Cell newCell = row.createCell(row.getLastCellNum());
                    if (lotusId != null && !lotusId.isEmpty()) {
                        String lotusTaxonomy = lotusMongo.extractTaxonomy(lotusId);
                        try {
                        	newCell.setCellValue(lotusTaxonomy);
                        } catch (Exception e) {
                        	System.out.println(lotusId);
                        	e.printStackTrace();
                        }
                    } else {
                    	newCell.setCellValue("");
                    }
                }
            }
			
			// 将修改后的 Workbook 写出到磁盘
            try (FileOutputStream fileOut = new FileOutputStream(outputFileName)) {
                workbook.write(fileOut);
                System.out.println("Excel 文件已成功写出到 " + outputFileName);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
