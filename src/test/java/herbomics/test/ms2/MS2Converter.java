package herbomics.test.ms2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MS2Converter {

	public static String ms2JdbcUrl = "jdbc:sqlite:F://MyProjects//herbomics//资料//data-v2//NPMS-V1 Build0530封装材料//NPMS-V1 Build0530.db";
	
	public List<Double> parseBytes(byte[] data) {
        // 将每8个字节解码为双精度浮点数
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < data.length; i += 8) {
            byte[] doubleBytes = new byte[8];
            System.arraycopy(data, i, doubleBytes, 0, 8);
            double floatValue = ByteBuffer.wrap(doubleBytes)
                                          .order(ByteOrder.LITTLE_ENDIAN) // 小端序
                                          .getDouble();
            values.add(floatValue);
        }
        return values;
    }
	
	public JSONObject convertData(String ipt) {
		JSONObject result = new JSONObject();
        String sql = "select * FROM SpectrumTable spec JOIN (SELECT CompoundId, HMDBId FROM CompoundTable) comp ON"
        		+ " spec.CompoundId = comp.CompoundId WHERE HMDBId = ?";
        try (Connection conn = DriverManager.getConnection(ms2JdbcUrl);
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setString(1, ipt);
        	try (ResultSet rs = pstmt.executeQuery()) {
        		while (rs.next()) {
        			JSONObject frag = new JSONObject();
                    byte[] spectrumMass = rs.getBytes("blobMass");
                    byte[] spectrumIntensity = rs.getBytes("blobIntensity");
                    List<Double> mass = parseBytes(spectrumMass);
                    List<Double> intensity = parseBytes(spectrumIntensity);
                    frag.put("m/z", mass);
                    frag.put("intensity", intensity);
                    String rawFileURL = rs.getString("RawFileURL");
                    String name = rawFileURL.substring(rawFileURL.indexOf("["), rawFileURL.indexOf("]") + 1);
                    result.put(name, frag);
                }
        	}
        	System.out.println(ipt + " convert finish");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
	}
	
	
	public JSONArray convertDataToMsEntropyLibrary(String ipt, JSONObject meta) {
		JSONArray result = new JSONArray();
        String sql = "select * FROM SpectrumTable spec JOIN (SELECT CompoundId, HMDBId FROM CompoundTable) comp ON"
        		+ " spec.CompoundId = comp.CompoundId WHERE HMDBId = ?";
        try (Connection conn = DriverManager.getConnection(ms2JdbcUrl);
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setString(1, ipt);
        	try (ResultSet rs = pstmt.executeQuery()) {
        		while (rs.next()) {
        			JSONObject frag = new JSONObject();
        			JSONArray peaks = new JSONArray();
        			JSONArray metaPeaks = new JSONArray();
        			double precursorMass = rs.getDouble("PrecursorMass");
                    byte[] spectrumMass = rs.getBytes("blobMass");
                    byte[] spectrumIntensity = rs.getBytes("blobIntensity");
                    List<Double> mass = parseBytes(spectrumMass);
                    List<Double> intensity = parseBytes(spectrumIntensity);
                    for (int i = 0; i < mass.size(); i++) {
                    	JSONArray peak = new JSONArray();
                    	JSONArray peak2 = new JSONArray();
                    	peak.add(mass.get(i));
                    	peak.add(intensity.get(i));
                    	peaks.add(peak);
                    	peak2.add(mass.get(i));
                    	peak2.add(intensity.get(i));
                    	metaPeaks.add(peak2);
                    }
                    frag.put("precursor_mz", precursorMass);
                    frag.put("peaks", peaks);
                    
                    String rawFileURL = rs.getString("RawFileURL");
                    String name = rawFileURL.substring(rawFileURL.indexOf("["), rawFileURL.indexOf("]") + 1);
                    String id = ipt + " " + name;
                    frag.put("id", id);
                    
                    JSONObject metadata = new JSONObject();
                    metadata.putAll(meta);
                    metadata.put("ipt", ipt);
                    metadata.put("ion", name);
                    metadata.put("peaks", metaPeaks);
                    frag.put("metadata", metadata);
                    
                    result.add(frag);
                }
        	}
        	System.out.println(ipt + " convert finish");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
	}

}
