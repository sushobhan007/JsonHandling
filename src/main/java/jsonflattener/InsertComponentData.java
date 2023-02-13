/*
package jsonflattener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class InsertComponentData {

    public static void main(String[] args) throws IOException {
        insertInMongo();
    }

    enum Types {
        DATABASE,
        FILES
    }

    public static void insertInMongo() throws IOException {
        try (MongoConnection connection = MongoUtility.getMongoConnection()) {

            HashMap<Types, String> map = new HashMap<>();
            map.put(Types.DATABASE, "databases");
            map.put(Types.FILES, "files");

            File file = new File("C:\\Users\\Sushobhan\\Downloads\\transparency.json");
            String jsonData = new String(Files.readAllBytes(file.toPath()));

            JSONObject jsonObject = new JSONObject(jsonData);
//            KLogger.info("jsonData: " + jsonData);
//            KLogger.info("jsonObject: " + jsonObject);

            JSONArray rootNode = jsonObject.getJSONObject(map.get(Types.DATABASE)).getJSONArray("children");
            List<InsertOneModel<Document>> userList = new ArrayList<>();
            Document data = null;
            int i = 0;
            while (i < rootNode.length()) {
                String component = rootNode.getJSONObject(0).getString("component");
                if (!component.equals("containers/EmptyContainer.vue")) {
//                    KLogger.info(rootNode.getJSONObject(0));
//                    getLeafNodeNames(children);
                    data = new Document();
//                    Object o = rootNode.get(i);
                    data.put("name", rootNode.getJSONObject(i).getString("name"));
                    data.put("path", rootNode.getJSONObject(i).getString("help"));

                    userList.add(new InsertOneModel<>(data));
//                    data.put(rootNode.get(0)).getString("path"));
//                    data.put("component", rootNode.getJSONObject(i).getString("component"));
//                    data.put("help", rootNode.getJSONObject(i)
                    if (!userList.isEmpty()) {
                        MongoCollection<Document> mongoCollection = connection.getDatabase("globalid").getCollection("demoCollSM");
                        mongoCollection.bulkWrite(userList);
                        userList.clear();
                    }
                }
                i++;
            }

            JSONArray jsonArray = JsonFlattener.flattenData(jsonObject.toString(), new HashMap<>());


            KLogger.info(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map) {
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

    //Get all the leaf nodes
    public static void getLeafNodeNames(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            Object element = array.get(i);
            if (element instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) element;
                // traverse the object and look for leaf nodes
                for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                    String key = (String) it.next();
                    Object value = jsonObject.get(key);
                    if (value instanceof JSONArray) {
                        getLeafNodeNames((JSONArray) value);
                    } else {
                        // leaf node found
                        System.out.println("Leaf node: " + key);
                    }
                }
            }
        }
    }
}


*/
