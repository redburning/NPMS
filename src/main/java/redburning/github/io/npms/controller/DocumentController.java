package redburning.github.io.npms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import redburning.github.io.npms.entity.DocumentEntity;
import redburning.github.io.npms.service.DocumentService;

@RestController
@RequestMapping("/docs")
public class DocumentController {

	@Autowired
    private DocumentService documentService;

    @GetMapping("/search")
    public List<SearchHit<DocumentEntity>> searchFullText(@RequestParam String keyword) {
        return documentService.searchFullText(keyword);
    }

    @PostMapping("/add")
    public void addDocument(@RequestBody DocumentEntity document) {
        documentService.saveDocument(document);
    }
	
}
