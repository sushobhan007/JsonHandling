package jsonflattener.component_store_gid;

import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONMapTest {
    public static JSONArray makeJSON() throws JSONException {
        JSONArray rootArray = new JSONArray();
        JSONObject obj1 = new JSONObject();
        JSONObject obj2 = new JSONObject();
        JSONObject obj3 = new JSONObject();
        JSONObject obj4 = new JSONObject();
        JSONObject obj5 = new JSONObject();
        JSONObject obj6 = new JSONObject();
        JSONObject obj7 = new JSONObject();

        obj1.put("id", "0");
        obj1.put("name", "World");
        obj1.put("pid", "");

        obj2.put("id", "1");
        obj2.put("name", "Asia");
        obj2.put("pid", "0");

        obj3.put("id", "2");
        obj3.put("name", "Europe");
        obj3.put("pid", "0");

        obj4.put("id", "3");
        obj4.put("name", "India");
        obj4.put("pid", "1");

        obj5.put("id", "4");
        obj5.put("name", "Pakistan");
        obj5.put("pid", "1");

        obj6.put("id", "5");
        obj6.put("name", "WB");
        obj6.put("pid", "3");

        obj7.put("id", "6");
        obj7.put("name", "Italy");
        obj7.put("pid", "2");

        rootArray.put(obj1);
        rootArray.put(obj2);
        rootArray.put(obj3);
        rootArray.put(obj4);
        rootArray.put(obj5);
        rootArray.put(obj6);
        rootArray.put(obj7);

        return rootArray;
    }

}
