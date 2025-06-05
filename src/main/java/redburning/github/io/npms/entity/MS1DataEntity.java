package redburning.github.io.npms.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import com.alibaba.fastjson.JSONObject;

@Document(indexName = "ms1_index")
public class MS1DataEntity extends BaseEntity {

	private JSONObject positiveData;
    
    private JSONObject negativeData;
    
    private String positiveAnnotation;

	private String negativeAnnotation;
    
	public JSONObject getPositiveData() {
		return positiveData;
	}
	
	public void setPositiveData(JSONObject positiveData) {
		this.positiveData = positiveData;
	}
	
	public JSONObject getNegativeData() {
		return negativeData;
	}
	
	public void setNegativeData(JSONObject negativeData) {
		this.negativeData = negativeData;
	}
	
	public String getPositiveAnnotation() {
		return positiveAnnotation;
	}

	public void setPositiveAnnotation(String positiveAnnotation) {
		this.positiveAnnotation = positiveAnnotation;
	}

	public String getNegativeAnnotation() {
		return negativeAnnotation;
	}

	public void setNegativeAnnotation(String negativeAnnotation) {
		this.negativeAnnotation = negativeAnnotation;
	}
}
