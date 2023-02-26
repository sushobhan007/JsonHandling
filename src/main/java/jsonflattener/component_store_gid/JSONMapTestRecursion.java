package jsonflattener.component_store_gid;

import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

public class JSONMapTestRecursion {
    public static void main(String[] args) throws JSONException {
        JSONArray jsonArray = JSONMapTest.makeJSON();
        JSONObject rootObject = new JSONObject();
        rootObject.put("name", "world");
        rootObject.put("children", getChildren(jsonArray, "0"));
        JSONObject resultObject = new JSONObject();
        resultObject.put("root", rootObject);

        String resultJsonString = resultObject.toString();
        System.out.println(resultJsonString);
    }

    private static JSONArray getChildren(JSONArray jsonArray, String id) throws JSONException {
        JSONArray childrenArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("pid").equals(id)) {
                JSONObject childObject = new JSONObject();
                childObject.put("name", jsonObject.getString("name"));
                JSONArray children = getChildren(jsonArray, jsonObject.getString("id"));
                childObject.put("children", children);
                childrenArray.put(childObject);
            }
        }
        return childrenArray;
    }
}
