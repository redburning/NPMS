package herbomics.test.ms1;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.alibaba.fastjson.JSONObject;

import herbomics.test.util.FileUtil;
import umich.ms.datatypes.scan.IScan;
import umich.ms.fileio.exceptions.FileParsingException;
import umich.ms.fileio.filetypes.mzxml.MZXMLFile;
import umich.ms.fileio.filetypes.mzxml.MZXMLIndex;

public class MS1Converter {

	private List<String> ms1Files;
	
	public MS1Converter() {
		String folderPath = "F:\\MyProjects\\herbomics\\资料\\data-v2\\MS1-mzXML";
		ms1Files = FileUtil.listDir(folderPath, ".mzXML");
	}
	
	private JSONObject doTransform(String file) throws FileParsingException {
		JSONObject data = new JSONObject();
		MZXMLFile source = new MZXMLFile(file);
		MZXMLIndex index = source.fetchIndex();
		for (Integer scanNumRaw : index.getMapByRawNum().keySet()) {
			IScan scan = source.parseScan(scanNumRaw, true);
			List<Double> mzList = DoubleStream.of(scan.getSpectrum().getMZs())
					.boxed()
					.collect(Collectors.toList());
			List<Double> intensityLis = DoubleStream.of(scan.getSpectrum().getIntensities())
					.boxed()
					.collect(Collectors.toList());
			data.put("m/z", mzList);
			data.put("intensity", intensityLis);
		}
		source.close();
		return data;
	}
	
	public JSONObject convertPositiveData(String ipt) throws FileParsingException {
		Optional<String> pms1File = ms1Files.stream()
				.filter(fileName -> fileName.contains(ipt) && fileName.contains("PMS1"))
				.findFirst();
		if (pms1File.isPresent()) {
			return doTransform(pms1File.get());
		} else {
			System.out.println("Not found ms1 positive data for " + ipt);
			return null;
		}
	}
	
	public JSONObject convertNegativeData(String ipt) throws FileParsingException {
		Optional<String> pms1File = ms1Files.stream()
				.filter(fileName -> fileName.contains(ipt) && fileName.contains("NMS1"))
				.findFirst();
		if (pms1File.isPresent()) {
			return doTransform(pms1File.get());
		} else {
			System.out.println("Not found ms1 negative data for " + ipt);
			return null;
		}
	}
}
