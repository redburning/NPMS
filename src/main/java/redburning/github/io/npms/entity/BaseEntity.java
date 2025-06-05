package redburning.github.io.npms.entity;

import org.springframework.data.annotation.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseEntity {

	@Id
	private String id;
	
    private String ipt;
	
	public String getIpt() {
		return ipt;
	}

	public void setIpt(String ipt) {
		this.ipt = ipt;
	}
	
	@Override
	public String toString() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(this);
	}
}
