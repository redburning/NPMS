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

import redburning.github.io.npms.entity.MetaDataEntity;

public interface MetaDataRepository extends ElasticsearchRepository<MetaDataEntity, String> {

	@Query("{ \"query_string\": {\"query\": \"?0\", \"fields\": [\"*\"] } }")
	@Highlight(
			fields = {
					@HighlightField(name = "ipt"),
					@HighlightField(name = "chineseName"),
					@HighlightField(name = "englishName"), 
					@HighlightField(name = "chineseSynonyms"),
					@HighlightField(name = "englishSynonyms"), 
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
					@HighlightField(name = "smiles"),
					@HighlightField(name = "inChIKey"),
					@HighlightField(name = "bioactivity") }, 
			parameters = @HighlightParameters(
					preTags = {"<span class='highlight'>" }, 
					postTags = { "</span>" }, numberOfFragments = 0)
			)
	List<SearchHit<MetaDataEntity>> searchFullText(String keyword, Pageable pageable);
	
	@Query("{ \"term\": {\"?0\": \"?1\" } }")
	@Highlight(
			fields = {
					@HighlightField(name = "ipt"),
					@HighlightField(name = "chineseName"),
					@HighlightField(name = "englishName"), 
					@HighlightField(name = "chineseSynonyms"),
					@HighlightField(name = "englishSynonyms"), 
					@HighlightField(name = "pathway"),
					@HighlightField(name = "superclass"), 
					@HighlightField(name = "clazz"), 
					@HighlightField(name = "pubChemCID"),
					@HighlightField(name = "lotus"), 
					@HighlightField(name = "cas"),
					@HighlightField(name = "molecularFormula") }, 
			parameters = @HighlightParameters(
					preTags = {"<span class='highlight'>" }, 
					postTags = { "</span>" }, numberOfFragments = 0)
			)
	List<SearchHit<MetaDataEntity>> termQuery(String field, String value, Pageable pageable);
	
	@Query("{ \"match\": {\"ipt\": \"?0\" } }")
	List<SearchHit<MetaDataEntity>> searchByIpt(String ipt);
	
	
	@Query("{ \"query_string\": {\"query\": \"?0\", \"fields\": [\"*\"] } }")
	SearchHits<MetaDataEntity> getTotalHitsForKeyword(String keyword);
	
	@Query("{ \"term\": {\"?0\": \"?1\" } }")
	SearchHits<MetaDataEntity> getTotalHitsForTermQuery(String field, String value);
	
}
