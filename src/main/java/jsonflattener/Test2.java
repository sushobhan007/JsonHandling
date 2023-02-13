package jsonflattener;

import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Test2 {

    public static void main(String[] args) throws IOException, JSONException {
        JSONFlattenerV2 jsonFlattener = new JSONFlattenerV2("demoDatabase", "demoCollection");
        File filePath = new File("C:\\Users\\susho\\Downloads\\transparency.json");
        String jsonData = new String(Files.readAllBytes(filePath.toPath()));

        JSONObject jsonObject = new JSONObject(jsonData);

        Document document = new Document();

        jsonFlattener.processNestedJsonRecursive(jsonObject, document);
        System.out.println("Populated..");
    }

}
