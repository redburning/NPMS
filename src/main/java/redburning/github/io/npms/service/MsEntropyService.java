package redburning.github.io.npms.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class MsEntropyService {

	public String searchSpectrum(JSONObject querySpectrum) throws IOException {
		OkHttpClient client = new OkHttpClient();

        // 构造请求体 JSON 数据
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, querySpectrum.toString());

        // 构造请求
        Request request = new Request.Builder()
               .url("http://localhost:5000/ms-search")
               .post(body)
               .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
        	throw new IOException("Request failed:" + response.code());
        }
	}
	
}
