package herbomics.test.entropy;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import herbomics.test.DataLoader;
import herbomics.test.ms2.MS2Converter;

public class MsEntropyLibrary {
	
	private static final String fileName = "dataset.xlsx";
	
    public static void main(String[] args) throws IOException {
    	try (Workbook workbook = new XSSFWorkbook(
				DataLoader.class.getClassLoader().getResourceAsStream(fileName))) {
    		JSONArray result = parseMS2Data(workbook);
    		
    		try (FileWriter writer = new FileWriter("D://ms2-library.json")) {
                writer.write(JSONArray.toJSONString(result, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
    		
    		System.out.println("finished:" + result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private static JSONArray parseMS2Data(Workbook workbook) {
    	JSONArray result = new JSONArray();
		MS2Converter ms2Converter = new MS2Converter();
		Sheet sheet = workbook.getSheetAt(0);
		// 跳过表头
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String ipt = row.getCell(4).getStringCellValue();
            String englishName = row.getCell(2).getStringCellValue();
            String chineseName = row.getCell(3).getStringCellValue();
            String formula = row.getCell(11).getStringCellValue();
            JSONObject metadata = new JSONObject();
            metadata.put("chineseName", chineseName);
            metadata.put("englishName", englishName);
            metadata.put("formula", formula);
            JSONArray data = ms2Converter.convertDataToMsEntropyLibrary(ipt, metadata);
            result.addAll(data);
        }
		return result;
	}
}
