package redburning.github.io.npms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import redburning.github.io.npms.dto.Result;
import redburning.github.io.npms.entity.MetaDataEntity;
import redburning.github.io.npms.service.MetaDataService;

@RestController
@RequestMapping("/docs")
public class MetaDataController {

	@Autowired
    private MetaDataService metaDataService;

	@GetMapping("/search")
	public Result searchFullText(@RequestParam String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			List<SearchHit<MetaDataEntity>> searchHits = metaDataService.searchFullText(keyword, page, size);
			return Result.success(searchHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
	
	@GetMapping("/term")
	public Result searchByIpt(@RequestParam String field, @RequestParam String value,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			List<SearchHit<MetaDataEntity>> searchHits = metaDataService.termQuery(field, value, page, size);
			return Result.success(searchHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
	
	@GetMapping("/searchIpt")
	public Result searchByIpt(@RequestParam String ipt) {
		try {
			List<SearchHit<MetaDataEntity>> searchHits = metaDataService.searchByIpt(ipt);
			return Result.success(searchHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
	
	@GetMapping("/count")
    public Result getTotalHitsForKeyword(@RequestParam String keyword) {
		try {
			long totalHits = metaDataService.getTotalHitsForKeyword(keyword);
	        return Result.success(totalHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
    }
	
	@GetMapping("/termcount")
    public Result getTotalHitsForTermQuery(@RequestParam String field, @RequestParam String value) {
		try {
			long totalHits = metaDataService.getTotalHitsForTermQuery(field, value);
	        return Result.success(totalHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
    }
	
	@GetMapping("/suggest")
	public Result getSuggestions(@RequestParam String keyword) {
		try {
			List<String> suggestions = metaDataService.getSuggestions(keyword);
			return Result.success(suggestions);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
	
}
