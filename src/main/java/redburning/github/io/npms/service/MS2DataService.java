package redburning.github.io.npms.service;

import java.util.List;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import redburning.github.io.npms.entity.MS2DataEntity;
import redburning.github.io.npms.repository.MS2DataRepository;

@Service
public class MS2DataService {

	private MS2DataRepository ms2DataRepository;
	
	public List<SearchHit<MS2DataEntity>> searchByIpt(String ipt) {
		return ms2DataRepository.searchByIpt(ipt);
	}
}
