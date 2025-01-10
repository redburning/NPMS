package redburning.github.io.npms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import redburning.github.io.npms.dto.Result;
import redburning.github.io.npms.entity.DocumentEntity;
import redburning.github.io.npms.service.DocumentService;

@RestController
@RequestMapping("/docs")
public class DocumentController {

	@Autowired
    private DocumentService documentService;

	@GetMapping("/search")
	public Result searchFullText(@RequestParam String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			List<SearchHit<DocumentEntity>> searchHits = documentService.searchFullText(keyword, page, size);
			return Result.success(searchHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
	
	@GetMapping("/count")
    public Result getTotalHits(@RequestParam String keyword) {
		try {
			long totalHits = documentService.getTotalHitsForKeyword(keyword);
	        return Result.success(totalHits);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
    }
	
}
