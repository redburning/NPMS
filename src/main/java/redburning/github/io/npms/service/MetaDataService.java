package redburning.github.io.npms.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import redburning.github.io.npms.entity.MetaDataEntity;
import redburning.github.io.npms.repository.MetaDataRepository;

@Service
public class MetaDataService {

	private static final String INDEX_NAME = "herbomics_index";
	
	@Autowired
    private MetaDataRepository metaDataRepository;
	
	@Autowired
    private RestHighLevelClient restHighLevelClient;
	
    public List<SearchHit<MetaDataEntity>> searchFullText(String keyword, int page, int size) {
    	Pageable pageable = PageRequest.of(page, size);
    	return metaDataRepository.searchFullText(keyword, pageable);
    }

    public List<SearchHit<MetaDataEntity>> searchByIpt(String ipt) {
    	return metaDataRepository.searchByIpt(ipt);
    }
    
    public long getTotalHitsForKeyword(String keyword) {
        SearchHits<MetaDataEntity> searchHits = metaDataRepository.countTotalHits(keyword);
        return searchHits.getTotalHits();
    }
	
    public List<String> getSuggestions(String keyword) throws IOException {
    	// 构建 suggest 查询
        SuggestBuilder suggestBuilder = new SuggestBuilder()
                .addSuggestion("suggest", new CompletionSuggestionBuilder("suggest")
                        .prefix(keyword)
                        .size(10));
        
        // 构建搜索请求
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        searchRequest.source().suggest(suggestBuilder);
        
        // 执行查询
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        
        // 解析 suggest 结果
        Suggest suggest = searchResponse.getSuggest();
        CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggest");
        
        return completionSuggestion.getEntries().stream()
                .flatMap(entry -> entry.getOptions().stream())
                .map(option -> option.getText().string())
                .distinct()
                .collect(Collectors.toList());
    }
    
}
