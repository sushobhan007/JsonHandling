package jsonflattener;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;
import org.bson.Document;

import java.util.Iterator;

public class JSONFlattenerV2 {
    private static MongoCollection<Document> collection;

    public JSONFlattenerV2(String databaseName, String collectionName) {
        // Connect to MongoDB
        try (MongoClient mongoClient = new MongoClient()) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            collection = database.getCollection(collectionName);
        }
    }

    public void processNestedJsonRecursive(Object data, Document doc) throws JSONException {
        if (data instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) data;
            if (jsonObject.has("component") && !"containers/EmptyContainer.vue".equals(jsonObject.getString("component"))) {
                Iterator<?> it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    Object value = jsonObject.get(key);
                    if (value instanceof JSONObject || value instanceof JSONArray) {
                        processNestedJsonRecursive(value, doc);
                    } else {
                        doc.put(key, value);
                    }
                }
                collection.insertOne(doc);
                return;
            }
            Iterator<?> it = jsonObject.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject || value instanceof JSONArray) {
                    processNestedJsonRecursive(value, doc);
                } else {
                    doc.put(key, value);
                }
            }
            collection.insertOne(doc);
        } else if (data instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) data;
            for (int i = 0; i < jsonArray.length(); i++) {
                Object value = jsonArray.get(i);
                processNestedJsonRecursive(value, doc);
            }
        }
    }
}
