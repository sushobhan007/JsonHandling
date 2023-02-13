package jsonflattener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class TestAddKeys {
    public static void main(String[] args) throws IOException {
        try (MongoClient mongoClient = new MongoClient()) {
            String databaseName = "demoDatabase";
            String collectionName = "demoCollection";
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            File filePath = new File("C:\\Users\\susho\\Downloads\\transparency.json");
            String jsonData = new String(Files.readAllBytes(filePath.toPath()));

            JSONFlattener jsonFlattener = new JSONFlattener();

            Map<String, Object> map = new HashMap<>();

            jsonFlattener.addKeys("", new ObjectMapper().readTree(jsonData), map);

            Document document = new Document(map);
            collection.insertOne(document);
        }
    }
}
