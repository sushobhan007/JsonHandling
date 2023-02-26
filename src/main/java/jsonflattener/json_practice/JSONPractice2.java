package jsonflattener.json_practice;

import jsonflattener.component_store_gid.JSONMapTest;
import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONPractice2 {

    public static void main(String[] args) throws JSONException {
        JSONArray jsonArray = JSONMapTest.makeJSON();
        JSONObject jsonObject = buildHierarchy(jsonArray);
        System.out.println(jsonObject);
    }

    public static JSONObject buildHierarchy(JSONArray jsonArray) throws JSONException {
        Map<String, JSONObject> map = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            map.put(jsonObject.getString("id"), jsonObject);
        }

        JSONObject root = new JSONObject();
        root.put("name", "world");
        root.put("children", new JSONArray());

        for (JSONObject jsonObject : map.values()) {
            String pid = jsonObject.getString("pid");
            if (pid.isEmpty()) {
                // add root node
                JSONObject node = new JSONObject();
                node.put("name", jsonObject.getString("name"));
                node.put("children", new JSONArray());
                root.getJSONArray("children").put(node);
            } else {
                // add node to parent
                JSONObject parentNode = map.get(pid);
                JSONArray children = parentNode.getJSONArray("children");
                JSONObject node = new JSONObject();
                node.put("name", jsonObject.getString("name"));
                node.put("children", new JSONArray());
                children.put(node);
            }
        }
        return root;
    }
}
