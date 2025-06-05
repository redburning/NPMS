package herbomics.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;

import herbomics.test.ms1.MS1Converter;
import herbomics.test.ms2.MS2Converter;
import redburning.github.io.npms.entity.BaseEntity;
import redburning.github.io.npms.entity.MetaDataEntity;
import redburning.github.io.npms.entity.MS1DataEntity;
import redburning.github.io.npms.entity.MS2DataEntity;
import umich.ms.fileio.exceptions.FileParsingException;

public class DataLoader {

	private static final String fileName = "dataset.xlsx";
	
	public List<MetaDataEntity> loadMetaData() throws IOException {
		try (Workbook workbook = new XSSFWorkbook(
				DataLoader.class.getClassLoader().getResourceAsStream(fileName))) {
			List<MetaDataEntity> documentEntityList = parseMetaData(workbook);
			return documentEntityList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<MS1DataEntity> loadMS1Data() {
		try (Workbook workbook = new XSSFWorkbook(
				DataLoader.class.getClassLoader().getResourceAsStream(fileName))) {
			List<MS1DataEntity> ms1EntityList = parseMS1Data(workbook);
			return ms1EntityList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<MS2DataEntity> loadMS2Data() {
		try (Workbook workbook = new XSSFWorkbook(
				DataLoader.class.getClassLoader().getResourceAsStream(fileName))) {
			List<MS2DataEntity> ms2EntityList = parseMS2Data(workbook);
			return ms2EntityList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<MetaDataEntity> parseMetaData(Workbook workbook) {
        List<MetaDataEntity> documentEntityList = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);

        // 跳过表头
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            MetaDataEntity documentEntity = new MetaDataEntity();
            Row row = sheet.getRow(i);

            // 使用setEntityValue方法设置字符串类型的属性
            setEntityStringValue(documentEntity, row, 2, MetaDataEntity::setEnglishName);
            setEntityStringValue(documentEntity, row, 3, MetaDataEntity::setChineseName);
            setEntityStringValue(documentEntity, row, 4, MetaDataEntity::setIpt);
            setEntityStringValue(documentEntity, row, 5, MetaDataEntity::setLotus);
            setEntityStringValue(documentEntity, row, 6, MetaDataEntity::setCas);
            setEntityStringValue(documentEntity, row, 7, MetaDataEntity::setPubChemCID);
            setEntityStringValue(documentEntity, row, 8, MetaDataEntity::setWikidata);
            setEntityStringValue(documentEntity, row, 9, MetaDataEntity::setEnglishSynonyms);
            setEntityStringValue(documentEntity, row, 10, MetaDataEntity::setChineseSynonyms);
            setEntityStringValue(documentEntity, row, 11, MetaDataEntity::setMolecularFormula);
            setEntityIntValue(documentEntity, row, 12, MetaDataEntity::setCharge);
            setEntityDoubleValue(documentEntity, row, 13, MetaDataEntity::setMonoisotopicMass);
            setEntityDoubleValue(documentEntity, row, 14, MetaDataEntity::setAvgMolecularWeight);
            setEntityStringValue(documentEntity, row, 15, MetaDataEntity::setInChIKey);
            setEntityStringValue(documentEntity, row, 16, MetaDataEntity::setSmiles);
            setEntityStringValue(documentEntity, row, 17, MetaDataEntity::setPathway);
            setEntityStringValue(documentEntity, row, 18, MetaDataEntity::setSuperclass);
            setEntityStringValue(documentEntity, row, 19, MetaDataEntity::setClazz);
            setEntityStringValue(documentEntity, row, 20, MetaDataEntity::setSpecies1);
            setEntityStringValue(documentEntity, row, 21, MetaDataEntity::setSpecies2);
            setEntityStringValue(documentEntity, row, 22, MetaDataEntity::setSpecies3);
            setEntityDoubleValue(documentEntity, row, 23, MetaDataEntity::setMPlusH);
            setEntityDoubleValue(documentEntity, row, 24, MetaDataEntity::setMMinusH);
            setEntityDoubleValue(documentEntity, row, 25, MetaDataEntity::setMPlusNH4);
            setEntityDoubleValue(documentEntity, row, 26, MetaDataEntity::setMPlusNa);
            setEntityDoubleValue(documentEntity, row, 27, MetaDataEntity::setMPlusAcetate);
            setEntityDoubleValue(documentEntity, row, 28, MetaDataEntity::setMPlusCI);
            setEntityStringValue(documentEntity, row, 31, MetaDataEntity::setBioactivity);
            setEntityStringValue(documentEntity, row, 32, MetaDataEntity::setReference);
            
            // 补充suggest字段内容
            JSONObject suggest = new JSONObject();
            suggest.put("input", documentEntity.getSuggestFieldValues());
            documentEntity.setSuggest(suggest);
            
            documentEntityList.add(documentEntity);
        }
        return documentEntityList;
    }
	
	
	private List<MS1DataEntity> parseMS1Data(Workbook workbook) {
		List<MS1DataEntity> ms1EntityList = new ArrayList<>();
		MS1Converter ms1Converter = new MS1Converter();
        Sheet sheet = workbook.getSheetAt(0);
        // 跳过表头
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            MS1DataEntity ms1Entity = new MS1DataEntity();
            Row row = sheet.getRow(i);
            setEntityStringValue(ms1Entity, row, 29, MS1DataEntity::setPositiveAnnotation);
            setEntityStringValue(ms1Entity, row, 30, MS1DataEntity::setNegativeAnnotation);
            
            // 补充一级数据
            String ipt = row.getCell(4).getStringCellValue();
            try {
            	ms1Entity.setIpt(ipt);
				JSONObject ms1PositiveData = ms1Converter.convertPositiveData(ipt);
				ms1Entity.setPositiveData(ms1PositiveData);
				JSONObject ms1NegativeData = ms1Converter.convertNegativeData(ipt);
				ms1Entity.setNegativeData(ms1NegativeData);
			} catch (FileParsingException e) {
				e.printStackTrace();
			}
            ms1EntityList.add(ms1Entity);
        }
        return ms1EntityList;
    }
	
	
	private List<MS2DataEntity> parseMS2Data(Workbook workbook) {
		List<MS2DataEntity> ms2EntityList = new ArrayList<>();
		MS2Converter ms2Converter = new MS2Converter();
		Sheet sheet = workbook.getSheetAt(0);
		// 跳过表头
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            MS2DataEntity ms2Entity = new MS2DataEntity();
            Row row = sheet.getRow(i);
            String ipt = row.getCell(4).getStringCellValue();
            JSONObject data = ms2Converter.convertData(ipt);
            ms2Entity.setIpt(ipt);
            ms2Entity.setData(data);
            ms2EntityList.add(ms2Entity);
        }
		return ms2EntityList;
	}
	

	private <T extends BaseEntity> void setEntityIntValue(T entity, Row row, int cellIndex,
			BiConsumer<T, Integer> setter) {
		try {
			Cell cell = row.getCell(cellIndex);
			if (cell != null) {
				int value = (int) cell.getNumericCellValue();
				setter.accept(entity, value);
			}
		} catch (Exception e) {
			setter.accept(entity, null);
		}
	}
	
	private <T extends BaseEntity> void setEntityDoubleValue(T entity, Row row, int cellIndex,
			BiConsumer<T, Double> setter) {
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

	@SuppressWarnings("deprecation")
	private <T extends BaseEntity> void setEntityStringValue(T entity, Row row, int cellIndex,
			BiConsumer<T, String> setter) {
		try {
			Cell cell = row.getCell(cellIndex);
			
			// pubChemID列特殊处理
			if (cellIndex == 7) {
				cell.setCellType(CellType.STRING);
			}
			if (cell != null) {
				String value = cell.getStringCellValue();
				setter.accept(entity, value);
				
				// 不处理pubChemID列
				if (cellIndex != 7 && entity instanceof MetaDataEntity) {
					MetaDataEntity documentEntity = (MetaDataEntity) entity;
					documentEntity.addSuggestFieldValues(value);
				}
			}
		} catch (Exception e) {
			setter.accept(entity, null);
		}
	}
}
