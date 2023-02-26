package jsonflattener;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class HashTableExample {

    private static MongoCollection<Document> collection;
    static String database = "gid-collection";
    static String collectionName = "employee-details";

    public static void main(String[] args) throws IOException, JSONException {
        HashTableExample hashTableExample = new HashTableExample();
        File file = new File("C:\\Users\\susho\\Downloads\\transparency.json");
        String jsonData = new String(Files.readAllBytes(file.toPath()));
        JSONObject document = new JSONObject(jsonData);
//        hashTableExample.exampleHashTable();
        hashTableExample.insertIntoMongo(document, database, collectionName);
    }

    ArrayList<JSONObject> parseJSONObject(Object source) {
        ArrayList<JSONObject> rootJsonObject = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        if (source instanceof JSONObject) {
            JSONObject rootObject = (JSONObject) source;
            for (Iterator<String> keys = rootObject.keys(); keys.hasNext(); ) {
                String key = keys.next();
                Object value = rootObject.get(key);
                if (value instanceof JSONObject || value instanceof JSONArray) {
                    parseJSONObject(value);
                }
                jsonObject.put(key, value);
            }
            rootJsonObject.add(jsonObject);
        } else if (source instanceof JSONArray) {
            JSONArray rootArray = (JSONArray) source;
            for (int i = 0; i < rootArray.length(); i++) {
                Object childObjects = rootArray.get(i);
                if (childObjects instanceof JSONObject || childObjects instanceof JSONArray) {
                    parseJSONObject(childObjects);
                }
            }
        }
        return rootJsonObject;
    }

    ArrayList<Document> createDocumentL1(JSONObject source) throws JSONException {
        ArrayList<JSONObject> jsonObjects = parseJSONObject(source);
        ArrayList<Document> documents = new ArrayList<>();
        Document document = new Document();
        for (JSONObject jsonObject : jsonObjects) {
            for (Iterator<String> keys = jsonObject.keys(); keys.hasNext(); ) {
                String key = keys.next();
                Object values = jsonObject.get(key);
                document.append(key, values);
            }
            documents.add(document);
        }
//        System.out.println(document);
        return documents;
    }

    Document exampleHashTable() {
        Hashtable<String, Object> hashtable = new Hashtable<>();
        ArrayList<Hashtable<String, String>> list = new ArrayList<>();
        Hashtable<String, String> address = new Hashtable<>();
        address.put("city", "Dankuni");
        address.put("state", "WB");
        address.put("country", "India");

        Hashtable<String, String> banks = new Hashtable<>();
        banks.put("bank1", "SBI");
        banks.put("bank2", "AXIS");

        list.add(address);
        list.add(banks);

        hashtable.put("name", "Sushobhan Mudi");
        hashtable.put("status", list);
        hashtable.put("phone", "8582802697");
        hashtable.put("gender", "male");
        hashtable.put("isMarried", false);

        Document document = new Document(hashtable);
        JSONObject jsonObject = new JSONObject(hashtable);
//        System.out.println(document);
        return document;
    }

    Document createDocumentL2(ArrayList<Document> documents) {

        for (Document document : documents) {

        }
        return null;
    }

    void insertIntoMongo(JSONObject source, String databaseName, String collectionName) {
        try (MongoClient mongoClient = new MongoClient()) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            collection = database.getCollection(collectionName);
            ArrayList<Document> documentL1 = createDocumentL1(source);
            Document documentL2 = createDocumentL2(documentL1);
//            Document document = createDocumentL2(source);
            Map<String, Object> rootMap = new HashMap<>();
            for (Document document : documentL1) {
//                System.out.println(document);
                Set<String> keys = document.keySet();
                for (String key : keys) {
                    Object value = document.get(key);
                    JSONObject childObject = (JSONObject) value;
                    Set<String> childKeys = childObject.keySet();
                    for (String childKey : childKeys) {
                        Object childValue = childObject.get(childKey);
                        rootMap.put(childKey, childValue);
                    }
//                    rootMap.put()
                }
                Document document1 = new Document(rootMap);
                collection.insertOne(document1);
            }

//            System.out.println("Level1: " + documentL1);
//            System.out.println("Level2: " + documentL2);
//            collection.insertOne(document);
        }
    }
}
