package jsonflattener.component_store_gid;

import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONMapTest3 {
    public static void main(String[] args) throws JSONException {
        JSONArray jsonArray = JSONMapTest.makeJSON();
        JSONObject jsonObject = withoutRecursion(jsonArray);
        System.out.println(jsonObject);
    }

    static JSONObject withoutRecursion(JSONArray jsonArray) throws JSONException {
        Map<String, JSONObject> idNameMap = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject nodeJson = jsonArray.getJSONObject(i);
            String id = nodeJson.getString("id");
            String name = nodeJson.getString("name");
            JSONObject node = new JSONObject();
            node.put("name", name);
            node.put("children", new JSONArray());
            idNameMap.put(id, node);
        }
        JSONObject rootNode = new JSONObject();
        rootNode.put("root", "world");
        rootNode.put("children", new JSONArray());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject nodeJson = jsonArray.getJSONObject(i);
            String id = nodeJson.getString("id");
            String pid = nodeJson.getString("pid");
            JSONObject node = idNameMap.get(id);
            if (pid.isEmpty()) {
                rootNode.getJSONArray("children").put(node);
            } else {
                idNameMap.get(pid).getJSONArray("children").put(node);
            }
        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("root", rootNode);
        return rootNode;
    }


    static JSONObject recursionReturn(JSONArray jsonArray) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getString("pid").equals("")) {
                jsonObject.put("root", object.getString("name"));
                jsonObject.put("children", new JSONArray());
            } else {
                JSONArray children = new JSONArray();
                JSONObject childObject = new JSONObject();
                childObject.put("name", object.getString("name"));
                JSONArray children1 = children.put(jsonObject.getJSONArray("children").put(childObject));
//                recursionReturn(children1);
//                jsonObject.getJSONArray("children").put(children);
            }

        }
        return jsonObject;
    }


}
