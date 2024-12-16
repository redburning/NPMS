package redburning.github.io.npms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import redburning.github.io.npms.entity.DocumentEntity;
import redburning.github.io.npms.repository.DocumentRepository;

@Service
public class DocumentService {

	@Autowired
    private DocumentRepository documentRepository;

    public List<SearchHit<DocumentEntity>> searchFullText(String keyword) {
    	List<SearchHit<DocumentEntity>> searchResult = documentRepository.searchFullText(keyword);
        return searchResult;
    }

    public void saveDocument(DocumentEntity document) {
        documentRepository.save(document);
    }
	
}
