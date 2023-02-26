package jsonflattener.component_store_gid;

import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

import java.util.*;



public class JSONConverter {

    public static void main(String[] args) throws JSONException {

        // The flat JSON array
        JSONArray flatJSON = JSONMapTest.makeJSON();
        // The nested JSON object
        JSONObject nestedJSON = new JSONObject();

        // Create a map to hold references to each object
        Map<String, JSONObject> objectMap = new HashMap<>();

        // Loop through the flat JSON array and create objects for each node
        for (int i = 0; i < flatJSON.length(); i++) {
            JSONObject node = flatJSON.getJSONObject(i);
            JSONObject object = new JSONObject();
            object.put("name", node.getString("name"));
            object.put("children", new JSONArray());
            object.put("id", node.getString("id"));
            object.put("pid", node.getString("pid"));
            objectMap.put(node.getString("id"), object);
//            System.out.println(node);
        }

        // Loop through the objects again and add each child to its parent
        for (JSONObject object : objectMap.values()) {
            String pid = object.getString("pid");
            if (pid.isEmpty()) {
                nestedJSON.put(object.getString("name"), object);
            } else {
                JSONObject parent = objectMap.get(pid);
                JSONArray children = parent.getJSONArray("children");
                children.put(object);
            }
        }

        // Print the nested JSON object
        System.out.println(nestedJSON);
    }
}