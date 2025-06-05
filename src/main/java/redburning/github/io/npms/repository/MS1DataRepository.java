package redburning.github.io.npms.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import redburning.github.io.npms.entity.MS1DataEntity;

public interface MS1DataRepository extends ElasticsearchRepository<MS1DataEntity, String> {

	@Query("{ \"match\": {\"ipt\": \"?0\" } }")
	List<SearchHit<MS1DataEntity>> searchByIpt(String ipt);
	
}
