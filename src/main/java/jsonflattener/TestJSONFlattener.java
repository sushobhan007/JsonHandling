package jsonflattener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.activemq.artemis.utils.json.JSONObject;
import org.bson.Document;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static jsonflattener.JSONFlattener.jsonData;

public class TestJSONFlattener {

    public static void main(String[] args) throws Exception {
        JSONFlattener jsonFlattener = new JSONFlattener("demoDatabase", "demoCollection");
        File filePath = new File("C:\\Users\\susho\\Downloads\\transparency.json");
        String jsonData = new String(Files.readAllBytes(filePath.toPath()));
        try (MongoClient mongoClient = new MongoClient()) {
            String databaseName = "demoDatabase";
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            String collectionName = "demoCollection";
            MongoCollection<Document> collection = database.getCollection(collectionName);
            JSONFlattener jsonFlattener1 = new JSONFlattener();
            Map<String, Object> map = new HashMap<>();
            jsonFlattener.insertJsonFromFile(String.valueOf(filePath));
            jsonFlattener1.addKeys("", new ObjectMapper().readTree(jsonData), map);
            JSONObject jsonObject = new JSONObject(map);
            Document document = new Document();
            document.append("doc", map);
//            document.append("document", jsonObject);
            System.out.println("It is the Document: " + jsonObject);
            collection.insertOne(document);
        }

    }

}
