package jsonflattener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;
import org.bson.Document;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

public class JSONFlattener {

    private static MongoCollection<Document> collection;
    public static File file = new File("C:\\Users\\susho\\Downloads\\transparency.json");
    public static String jsonData;

    static {
        try {
            jsonData = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONFlattener(String databaseName, String collectionName) {
        // Connect to MongoDB
        try (MongoClient mongoClient = new MongoClient()) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            collection = database.getCollection(collectionName);
        }
    }

    public JSONFlattener() {

    }

    /*public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        try {
            addKeys("", new ObjectMapper().readTree(jsonData), map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(map);
        JSONObject jsonObject = new JSONObject(map);
        System.out.println(jsonObject);
    }*/

//    @Test
//    public void testCreatingKeyValues() {
//        Map<String, String> map = new HashMap<>();
//        try {
//            addKeys("", new ObjectMapper().readTree(jsonData), map);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(map);
//    }

    public void addKeys(String currentPath, JsonNode jsonNode, Map<String, Object> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }

    public void insertJsonFromFile(String filePath) throws Exception {
        // Read JSON data from file
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        char[] buffer = new char[(int) file.length()];
        fileReader.read(buffer);
        fileReader.close();

        // Convert JSON data to a JSON object
        JSONObject jsonData = new JSONObject(new String(buffer));

        // Call the recursive function to insert the JSON data into MongoDB
        insertJsonData(jsonData);
    }

    //Recursive Function to read JSON Data
    private void insertJsonData(JSONObject jsonData) throws JSONException {
        for (Iterator<?> it = jsonData.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            Object value = jsonData.get(key);

            if (value instanceof JSONObject) {
                // If the value is a JSON object, call the function recursively
                insertJsonData((JSONObject) value);
            } else if (value instanceof JSONArray) {
                // If the value is a JSON array, insert each element as a separate document
                JSONArray jsonArray = (JSONArray) value;
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object arrayElement = jsonArray.get(i);
                    if (arrayElement instanceof JSONObject) {
                        // If the array element is a JSON object, insert it as a separate document
                        collection.insertOne(Document.parse(arrayElement.toString()));
                    } else {
                        // If the array element is not a JSON object, insert it as a field in the parent document
                        Document document = new Document();
                        document.put(key, arrayElement);
                        collection.insertOne(document);
                    }
                }
            } else {
                // If the value is not a JSON object or array, insert it as a field in the parent document
                Document document = new Document();
                document.put(key, value);
                collection.insertOne(document);
            }
        }
    }

    public void processNestedJsonRecursive(Object data, String keyPrefix) throws JSONException {
        if (data instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) data;
            if (jsonObject.has("component") && !"containers/EmptyContainer.vue".equals(jsonObject.getString("component"))) {
                Document doc = new Document();
                for (Iterator<?> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = (String) it.next();
                    Object value = jsonObject.get(key);
                    String newKey = keyPrefix + key;
                    if (value instanceof JSONObject || value instanceof JSONArray) {
                        processNestedJsonRecursive(value, newKey + ".");
                    } else {
                        doc.put(newKey, value);
                    }
                }
                collection.insertOne(doc);
            } else {
                for (Iterator<?> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = (String) it.next();
                    Object value = jsonObject.get(key);
                    String newKey = keyPrefix + key;
                    if (value instanceof JSONObject || value instanceof JSONArray) {
                        processNestedJsonRecursive(value, newKey + ".");
                    }
                }
            }
        } else if (data instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) data;
            for (int i = 0; i < jsonArray.length(); i++) {
                Object value = jsonArray.get(i);
                processNestedJsonRecursive(value, keyPrefix);
            }
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
//                return;
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
//            collection.insertOne(doc);
        } else if (data instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) data;
            for (int i = 0; i < jsonArray.length(); i++) {
                Object value = jsonArray.get(i);
                processNestedJsonRecursive(value, doc);
            }
        }
    }
}