package redburning.github.io.npms.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ElasticsearchClientManager {

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    @PreDestroy
    public void closeElasticsearchClient() throws Exception {
        if (elasticsearchClient != null) {
            elasticsearchClient.close();
        }
    }
}