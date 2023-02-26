package jsonflattener;


import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class WriteReadJson {

    public static void main(String[] args) throws JSONException, IOException {
        WriteReadJson obj = new WriteReadJson();
        File file = new File("D:\\Programs\\JsonHandling\\JsonHandling\\person.json");
        String jsonData = new String(Files.readAllBytes(file.toPath()));
        JSONObject jsonObject = new JSONObject(jsonData);
//        obj.readJson(jsonObject, new Document());
        obj.insertIntoMongo(jsonObject);

//        obj.writeJson();
    }

    void writeJson() throws JSONException, IOException {
        JSONObject rootObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject nestedObject1 = new JSONObject();
        JSONObject nestedObject2 = new JSONObject();
        nestedObject1.put("Subject", "Java");
        nestedObject1.put("Grade", "A+");
        nestedObject2.put("Subject", "Data Structure");
        nestedObject2.put("Grade", "A");
        jsonArray.put(nestedObject1);
        jsonArray.put(nestedObject2);
        rootObject.put("Name", "John Doe");
        rootObject.put("Occupation", "Student");
        rootObject.put("Result", jsonArray);

        System.out.println(rootObject);

        try (FileWriter fileWriter = new FileWriter("person.json")) {
            fileWriter.write(rootObject.toString());
            fileWriter.flush();
        }
    }

    void readJson(JSONObject jsonObject, Document document) throws JSONException {
        for (Iterator keys = jsonObject.keys(); keys.hasNext(); ) {
            String key = (String) keys.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
//                System.out.println(key + ": " + value);
                Document subDocument = new Document();
//                readJson((JSONObject) value, subDocument);
                document.put(key, subDocument);
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object childValue = jsonArray.get(i);
                    if (childValue instanceof JSONObject || childValue instanceof JSONArray) {
                        Document subDocument = new Document();
                        readJson((JSONObject) childValue, subDocument);
//                        document.put(key, subDocument);
                    }
                }
            } else {
//                System.out.println(key + ": " + value);
                document.put(key, value);
            }
        }
        Document doc = new Document();
        doc.append("root", document);
        System.out.println("Document: " + document);
        System.out.println("Doc: " + doc);

    }


    ArrayList<Document> parseJson(Object source) throws JSONException {
        ArrayList<Document> documentArrayList = new ArrayList<>();
        if (source instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) source;
            Document document = new Document();
            for (Iterator keys = jsonObject.keys(); keys.hasNext(); ) {
                String key = (String) keys.next();
                Object value = jsonObject.get(key);
                document.put(key, value);
            }
            documentArrayList.add(document);
        }
        return documentArrayList;
    }

    void insertIntoMongo(Object source) throws JSONException {
        ArrayList<Document> documentArrayList = parseJson(source);
        JSONObject sourceObject = (JSONObject) source;
        Document document = new Document();
        for (Document documents : documentArrayList) {
            Set<String> keys = documents.keySet();
            for (String key : keys) {
//                System.out.println(key);
                Object value = sourceObject.get(key);
                document.append(key, value);
            }
            System.out.println(document);
        }
//        System.out.println(sourceObject);
    }
}
