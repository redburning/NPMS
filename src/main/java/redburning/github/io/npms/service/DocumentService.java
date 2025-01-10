package redburning.github.io.npms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import redburning.github.io.npms.entity.DocumentEntity;
import redburning.github.io.npms.repository.DocumentRepository;

@Service
public class DocumentService {

	@Autowired
    private DocumentRepository documentRepository;

    public List<SearchHit<DocumentEntity>> searchFullText(String keyword, int page, int size) {
    	Pageable pageable = PageRequest.of(page, size);
    	return documentRepository.searchFullText(keyword, pageable);
    }

    public long getTotalHitsForKeyword(String keyword) {
        SearchHits<DocumentEntity> searchHits = documentRepository.countTotalHits(keyword);
        return searchHits.getTotalHits();
    }
    
    public void saveDocument(DocumentEntity document) {
        documentRepository.save(document);
    }
	
}
