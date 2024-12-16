package herbomics.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import redburning.github.io.npms.entity.DocumentEntity;

public class DataLoader {

	private static final String fileName = "dataset.xlsx";
	
	public List<DocumentEntity> load() throws IOException {
		try (Workbook workbook = new XSSFWorkbook(
				DataLoader.class.getClassLoader().getResourceAsStream(fileName))) {
			List<DocumentEntity> documentEntityList = parse(workbook);
			return documentEntityList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<DocumentEntity> parse(Workbook workbook) {
        List<DocumentEntity> documentEntityList = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);

        // 跳过表头
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            DocumentEntity documentEntity = new DocumentEntity();
            Row row = sheet.getRow(i);

            // 使用setEntityValue方法设置字符串类型的属性
            setEntityStringValue(documentEntity, row, 0, DocumentEntity::setNumber);
            setEntityStringValue(documentEntity, row, 1, DocumentEntity::setMixedName);
            setEntityStringValue(documentEntity, row, 2, DocumentEntity::setChineseName);
            setEntityStringValue(documentEntity, row, 3, DocumentEntity::setIpt);
            setEntityStringValue(documentEntity, row, 4, DocumentEntity::setEnglishName);
            setEntityStringValue(documentEntity, row, 5, DocumentEntity::setSynonyms);
            setEntityStringValue(documentEntity, row, 6, DocumentEntity::setChineseNameAlias);
            setEntityStringValue(documentEntity, row, 7, DocumentEntity::setPathway);
            setEntityStringValue(documentEntity, row, 8, DocumentEntity::setSuperclass);
            setEntityStringValue(documentEntity, row, 9, DocumentEntity::setClazz);
            setEntityDoubleValue(documentEntity, row, 10, DocumentEntity::setPubChemCID);
            setEntityStringValue(documentEntity, row, 11, DocumentEntity::setLotus);
            setEntityStringValue(documentEntity, row, 12, DocumentEntity::setWikidata);
            setEntityStringValue(documentEntity, row, 13, DocumentEntity::setSpecies1);
            setEntityStringValue(documentEntity, row, 14, DocumentEntity::setSpecies2);
            setEntityStringValue(documentEntity, row, 15, DocumentEntity::setSpecies3);
            setEntityStringValue(documentEntity, row, 16, DocumentEntity::setCas);
            setEntityStringValue(documentEntity, row, 17, DocumentEntity::setMolecularFormula);
            setEntityDoubleValue(documentEntity, row, 18, DocumentEntity::setMonoisotopicMass);
            setEntityStringValue(documentEntity, row, 19, DocumentEntity::setMolFile);
            setEntityDoubleValue(documentEntity, row, 20, DocumentEntity::setmPositiveHPositive);
            setEntityDoubleValue(documentEntity, row, 21, DocumentEntity::setmNegativeHNegative);
            setEntityDoubleValue(documentEntity, row, 22, DocumentEntity::setRtPositive);
            setEntityDoubleValue(documentEntity, row, 23, DocumentEntity::setpResponse);
            setEntityDoubleValue(documentEntity, row, 24, DocumentEntity::setPositiveFrontInnerRef);
            setEntityDoubleValue(documentEntity, row, 25, DocumentEntity::setPositiveBackInnerRef);
            setEntityStringValue(documentEntity, row, 26, DocumentEntity::setPositiveIonDataDesc);
            setEntityDoubleValue(documentEntity, row, 27, DocumentEntity::setRtNegative);
            setEntityDoubleValue(documentEntity, row, 28, DocumentEntity::setnResponse);
            setEntityDoubleValue(documentEntity, row, 29, DocumentEntity::setNegativeFrontInnerRef);
            setEntityDoubleValue(documentEntity, row, 30, DocumentEntity::setNegativeBackInnerRef);
            setEntityStringValue(documentEntity, row, 31, DocumentEntity::setNegativeIonDataDesc);
            setEntityStringValue(documentEntity, row, 32, DocumentEntity::setSmiles);
            setEntityStringValue(documentEntity, row, 33, DocumentEntity::setInChIKey);
            setEntityDoubleValue(documentEntity, row, 34, DocumentEntity::setRt);
            setEntityDoubleValue(documentEntity, row, 35, DocumentEntity::setEsiPositiveNegativeRatio);
            
            // 补充message字段内容
            documentEntity.setMessage(String.join("; ", documentEntity.getStringFieldValues()));
            
            documentEntityList.add(documentEntity);
        }
        return documentEntityList;
    }

	private <T> void setEntityDoubleValue(DocumentEntity entity, Row row, int cellIndex,
			BiConsumer<DocumentEntity, Double> setter) {
		try {
			Cell cell = row.getCell(cellIndex);
			if (cell != null) {
				double value = cell.getNumericCellValue();
				setter.accept(entity, value);
			}
		} catch (Exception e) {
			setter.accept(entity, null);
		}
	}

	private void setEntityStringValue(DocumentEntity entity, Row row, int cellIndex,
			BiConsumer<DocumentEntity, String> setter) {
		try {
			Cell cell = row.getCell(cellIndex);
			if (cell != null) {
				String value = cell.getStringCellValue();
				setter.accept(entity, value);
				entity.addStringFieldValues(value);
			}
		} catch (Exception e) {
			setter.accept(entity, null);
		}
	}
	
}
