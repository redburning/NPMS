package redburning.github.io.npms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import redburning.github.io.npms.dto.Result;
import redburning.github.io.npms.entity.MS2DataEntity;
import redburning.github.io.npms.repository.MS2DataRepository;

@RestController
@RequestMapping("/ms2")
public class MS2DataController {

	@Autowired
	private MS2DataRepository ms2DataRepository;
	
	@GetMapping("/searchIpt")
	public Result searchByIpt(@RequestParam String ipt) {
		try {
			List<SearchHit<MS2DataEntity>> searchHits = ms2DataRepository.searchByIpt(ipt);
			return Result.success(searchHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
}
