package herbomics.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import redburning.github.io.npms.entity.DocumentEntity;

public class EsClient {
	
	private static final String esServerAddress = "127.0.0.1:9200";
	private static final String indexName = "herbomics_index";
	private static final HttpHost[] esHosts;
    
    static {
    	esHosts = parseEsServer(esServerAddress);
    }
    
    /**
     * 解析 esServer 字符串并转换为 HttpHost 数组
     *
     * @param esServer 地址字符串，可能是逗号分隔的多个地址，并且没有 http://
     * @return HttpHost 数组
     */
    private static HttpHost[] parseEsServer(String esServer) {
        String[] addresses = esServer.split(",");

        // 将每个地址添加 http:// 前缀，并转换为 HttpHost 对象
        List<HttpHost> httpHosts = Arrays.stream(addresses)
                .map(address -> "http://" + address)
                .map(HttpHost::create)
                .collect(Collectors.toList());
        
        // 转换为数组并返回
        return httpHosts.toArray(new HttpHost[0]);
    }
	

	private static BulkProcessor createBulkProcessor(RestHighLevelClient client) {
		BulkProcessor.Listener listener = new BulkProcessor.Listener() {
			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
				System.out.println(
						"Executing bulk request [" + executionId + "] with " + request.numberOfActions() + " actions");
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				if (response.hasFailures()) {
					System.out.println("Bulk request [" + executionId + "] completed with failures: "
							+ response.buildFailureMessage());
				} else {
					System.out.println("Bulk request [" + executionId + "] completed successfully");
				}
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				System.out.println("Bulk request [" + executionId + "] failed: " + failure.getMessage());
			}
		};

		BulkProcessor.Builder builder = BulkProcessor.builder(
				(request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener);

		// 设置批量请求的大小和频率
		builder.setBulkActions(1000); // 每 1000 个请求执行一次批量操作
		builder.setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB)); // 每 5MB 执行一次批量操作
		builder.setConcurrentRequests(1); // 设置并发请求数
		builder.setFlushInterval(TimeValue.timeValueSeconds(5)); // 每 5 秒执行一次批量操作

		// 设置重试策略
		builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(5), 3)); // 每5秒重试一次，最多重试3次

		return builder.build();
	}

    
    public static void main(String[] args) throws IOException {
    	List<DocumentEntity> documentList = (new DataLoader()).load();
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(esHosts));
		BulkProcessor bulkProcessor = createBulkProcessor(client);
		try {
			for (DocumentEntity document : documentList) {
				bulkProcessor.add(Requests.indexRequest().index(indexName).source(document.toString(), XContentType.JSON));
			}
			bulkProcessor.flush();
			bulkProcessor.awaitClose(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}
    
}
