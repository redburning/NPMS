package redburning.github.io.npms.service;

import java.util.List;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import redburning.github.io.npms.entity.MS1DataEntity;
import redburning.github.io.npms.repository.MS1DataRepository;

@Service
public class MS1DataService {

	private MS1DataRepository ms1DataRepository;
	
	public List<SearchHit<MS1DataEntity>> searchByIpt(String ipt) {
		return ms1DataRepository.searchByIpt(ipt);
	}
}
