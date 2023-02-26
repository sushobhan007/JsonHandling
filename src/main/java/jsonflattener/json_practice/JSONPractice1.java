package jsonflattener.json_practice;

import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

public class JSONPractice1 {
    public static void main(String[] args) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject nextJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("numbers", new JSONArray());
        System.out.println(jsonObject);

        nextJson.put("name", "Sushobhan");
        nextJson.put("address", "Kolkata");
        jsonArray.put(nextJson);
        jsonObject.getJSONArray("numbers").put(jsonArray);

        System.out.println(jsonObject);
    }
}
