package herbomics.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class LotusMongo {
	
	private final MongoClient mongoClient;

    public LotusMongo() {
        // 获取单例的 MongoDB 连接管理器
        this.mongoClient = MongoDBConnectionManager.getInstance().getMongoClient();
    }

    public String extractTaxonomy(String lotusId) {
        // 获取指定的数据库
        MongoDatabase database = mongoClient.getDatabase("lotusdb");
        // 获取指定的集合
        MongoCollection<Document> collection = database.getCollection("lotusUniqueNaturalProduct");

        Bson query = com.mongodb.client.model.Filters.eq("lotus_id", lotusId);
        // 执行查询
        List<Document> result = collection.find(query).into(new ArrayList<>());
        // 遍历结果
        for (Document document : result) {
            Document taxonomyReferenceObjects = (Document) document.get("taxonomyReferenceObjects");
            if (taxonomyReferenceObjects != null) {
                Set<String> keySet = taxonomyReferenceObjects.keySet();
                for (String key : keySet) {
                    Document taxonomy = (Document) taxonomyReferenceObjects.get(key);
                    if (taxonomy != null) {
                    	JSONObject taxonomyJson = JSONObject.parseObject(taxonomy.toJson());
                    	if (taxonomyJson.containsKey("NCBI")) {
                			JSONArray subTaxonomyArray = taxonomyJson.getJSONArray("NCBI");
                    		JSONObject targetJson = subTaxonomyArray.getJSONObject(0);
                    		JSONObject resultJson = new JSONObject();
                    		resultJson.put("genus", targetJson.getString("genus"));
                    		resultJson.put("species", targetJson.getString("species"));
                    		return JSONObject.toJSONString(resultJson, true);
                		}
                    }
                }
            }
        }
        return "Not Found Taxonomy in Lotus Database";
    }
}

//使用单例模式管理 MongoDB 连接
class MongoDBConnectionManager {
	
	private static final String CONNECTION_STRING = "mongodb://127.0.0.1:27017";
	private static MongoDBConnectionManager instance;
	private MongoClient mongoClient;
 
	private MongoDBConnectionManager() {
		// 创建 MongoClientSettings 对象
		MongoClientSettings settings = MongoClientSettings.builder()
			   .applyConnectionString(new ConnectionString(CONNECTION_STRING))
			   .build();
		// 创建 MongoClient 实例
		this.mongoClient = MongoClients.create(settings);
	}
 
	public static synchronized MongoDBConnectionManager getInstance() {
		if (instance == null) {
			instance = new MongoDBConnectionManager();
		}
		return instance;
	}
 
	public MongoClient getMongoClient() {
		return mongoClient;
	}
}
 