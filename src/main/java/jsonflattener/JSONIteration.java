package jsonflattener;

import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JSONIteration {

    public static void main(String[] args) throws IOException, JSONException {
        File filePath = new File("C:\\Users\\susho\\Downloads\\transparency.json");
        String jsonData = new String(Files.readAllBytes(filePath.toPath()));
        test(new JSONObject(jsonData));
    }

    public static void test(JSONObject jsonObject) throws JSONException {
        JSONArray key = jsonObject.names();
        JSONObject document = new JSONObject();
        for (int i = 0; i < key.length(); i++) {
            String keys = key.getString(i);
            String value = jsonObject.getString(keys);
            document.put(keys, value);
        }
        System.out.print(document);
    }
}
