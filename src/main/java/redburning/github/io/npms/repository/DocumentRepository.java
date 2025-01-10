package redburning.github.io.npms.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import redburning.github.io.npms.entity.DocumentEntity;

public interface DocumentRepository extends ElasticsearchRepository<DocumentEntity, String> {

	@Query("{ \"query_string\": {\"query\": \"?0\", \"fields\": [\"*\"] } }")
	@Highlight(
			fields = { 
					@HighlightField(name = "id"), 
					@HighlightField(name = "number"),
					@HighlightField(name = "mixedName"), 
					@HighlightField(name = "chineseName"),
					@HighlightField(name = "ipt"),
					@HighlightField(name = "englishName"), 
					@HighlightField(name = "synonyms"),
					@HighlightField(name = "chineseNameAlias"), 
					@HighlightField(name = "pathway"),
					@HighlightField(name = "superclass"), 
					@HighlightField(name = "clazz"), 
					@HighlightField(name = "pubChemCID"),
					@HighlightField(name = "lotus"), 
					@HighlightField(name = "wikidata"), 
					@HighlightField(name = "species1"),
					@HighlightField(name = "species2"), 
					@HighlightField(name = "species3"), 
					@HighlightField(name = "cas"),
					@HighlightField(name = "molecularFormula"), 
					@HighlightField(name = "positiveIonDataDesc"),
					@HighlightField(name = "negativeIonDataDesc"), 
					@HighlightField(name = "smiles"),
					@HighlightField(name = "inChIKey"),
					@HighlightField(name = "message") }, 
	parameters = @HighlightParameters(
			preTags = {"<span class='highlight'>" }, 
			postTags = { "</span>" }, numberOfFragments = 0))
	List<SearchHit<DocumentEntity>> searchFullText(String keyword, Pageable pageable);
	
	@Query("{ \"query_string\": {\"query\": \"?0\", \"fields\": [\"*\"] } }")
	SearchHits<DocumentEntity> countTotalHits(String keyword);

}
