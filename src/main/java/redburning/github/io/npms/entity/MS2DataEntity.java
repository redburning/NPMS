package redburning.github.io.npms.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import com.alibaba.fastjson.JSONObject;

@Document(indexName = "ms2_index")
public class MS2DataEntity extends BaseEntity {

	private JSONObject data;

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}
}
