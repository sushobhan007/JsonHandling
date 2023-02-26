package jsonflattener.component_store_gid;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONMapTest2 {
    public static void main(String[] args) {
        String jsonString = "[\n" +
                "    {\"name\":\"World\",\"pid\":\"\",\"id\":\"0\"},\n" +
                "    {\"name\":\"Asia\",\"pid\":\"0\",\"id\":\"1\"},\n" +
                "    {\"name\":\"Europe\",\"pid\":\"0\",\"id\":\"2\"},\n" +
                "    {\"name\":\"India\",\"pid\":\"1\",\"id\":\"3\"},\n" +
                "    {\"name\":\"Pakistan\",\"pid\":\"1\",\"id\":\"4\"},\n" +
                "    {\"name\":\"WB\",\"pid\":\"3\",\"id\":\"5\"},\n" +
                "    {\"name\":\"Italy\",\"pid\":\"2\",\"id\":\"6\"}\n" +
                "]";

        JSONArray jsonArray = new JSONArray(jsonString);
        JSONObject rootObject = new JSONObject();
        rootObject.put("name", "world");
        rootObject.put("children", getChildren(jsonArray, "0"));
        JSONObject resultObject = new JSONObject();
        resultObject.put("root", rootObject);

        String resultJsonString = resultObject.toString();
        System.out.println(resultJsonString);
    }

    private static JSONArray getChildren(JSONArray jsonArray, String id) {
        JSONArray childrenArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("pid").equals(id)) {
                JSONObject childObject = new JSONObject();
                childObject.put("name", jsonObject.getString("name"));
                childObject.put("children", getChildren(jsonArray, jsonObject.getString("id")));
                childrenArray.put(childObject);
            }
        }
        return childrenArray;
    }

    public static JSONObject convertToHierarchy(JSONArray flatJson) {

        // Create a map to hold the nodes by their IDs
        Map<String, JSONObject> idToNode = new HashMap<>();

        // Create nodes for each element in the flat JSON array, and add them to the map by their IDs
        for (int i = 0; i < flatJson.length(); i++) {
            JSONObject nodeJson = flatJson.getJSONObject(i);
            String id = nodeJson.getString("id");
            String name = nodeJson.getString("name");
            JSONObject node = new JSONObject();
            node.put("name", name);
            node.put("children", new JSONArray());
            idToNode.put(id, node);
        }

        // Create the root node and its children array
        JSONObject rootNode = new JSONObject();
        rootNode.put("name", "world");
        rootNode.put("children", new JSONArray());

        // Add each node to its parent's children array, or to the root node's children array if it has no parent
        for (int i = 0; i < flatJson.length(); i++) {
            JSONObject nodeJson = flatJson.getJSONObject(i);
            String id = nodeJson.getString("id");
            String pid = nodeJson.optString("pid");
            JSONObject node = idToNode.get(id);
            if (pid.isEmpty()) {
                rootNode.getJSONArray("children").put(node);
            } else {
                idToNode.get(pid).getJSONArray("children").put(node);
            }
        }

        // Create the hierarchical JSON object and return it
        JSONObject hierarchy = new JSONObject();
        hierarchy.put("root", rootNode);
        return hierarchy;
    }
}