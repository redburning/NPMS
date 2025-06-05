package redburning.github.io.npms.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import redburning.github.io.npms.entity.MS2DataEntity;

public interface MS2DataRepository extends ElasticsearchRepository<MS2DataEntity, String> {

	@Query("{ \"match\": {\"ipt\": \"?0\" } }")
	List<SearchHit<MS2DataEntity>> searchByIpt(String ipt);
	
}
