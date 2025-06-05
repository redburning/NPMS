package redburning.github.io.npms.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import redburning.github.io.npms.dto.Result;
import redburning.github.io.npms.service.MsEntropyService;

@RestController
@RequestMapping("/msentropy")
public class MsEntropyController {

	@Autowired
	private MsEntropyService msEntropyService;
	
	@PostMapping("/search")
	public Result searchSpectrum(@RequestBody JSONObject querySpectrum) {
		try {
			String data = msEntropyService.searchSpectrum(querySpectrum);
			return Result.success(data);
		} catch (IOException e) {
			e.printStackTrace();
			return Result.error(e.getMessage());
		}
	}
	
}
