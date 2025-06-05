package redburning.github.io.npms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import redburning.github.io.npms.dto.Result;
import redburning.github.io.npms.entity.MS1DataEntity;
import redburning.github.io.npms.repository.MS1DataRepository;

@RestController
@RequestMapping("/ms1")
public class MS1DataController {

	@Autowired
	private MS1DataRepository ms1DataRepository;
	
	@GetMapping("/searchIpt")
	public Result searchByIpt(@RequestParam String ipt) {
		try {
			List<SearchHit<MS1DataEntity>> searchHits = ms1DataRepository.searchByIpt(ipt);
			return Result.success(searchHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
}
