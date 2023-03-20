package traversejson;


import org.apache.activemq.artemis.utils.json.JSONArray;
import org.apache.activemq.artemis.utils.json.JSONException;
import org.apache.activemq.artemis.utils.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TraverseJSON {

    public static void main(String[] args) throws JSONException {
        JSONObject jsonObject = makeJSON();
//        System.out.println(jsonObject);
        String path = "Ingredients.Fruits.Papaya.Papaya";
        JSONArray children = jsonObject.getJSONArray("children");
//        traverseJSONV3(children, path);
        JSONArray response = traverseJSONV3(children, path);
        System.out.println(response);
    }

    static JSONArray traverseJSONV2(JSONArray children, String path) throws JSONException {
        JSONArray traversedArray = new JSONArray();
        String[] split = path.split("\\.");
//        System.out.println(Arrays.toString(split));
        List<String> list = Arrays.asList(split);
//        System.out.println(list);
        for (String listItem : list) {
            for (int i = 0; i < children.length(); i++) {
                Object values = children.get(i);
                if (values instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) values;
                    if (jsonObject.get("name").equals(listItem)) {
                        if (jsonObject.has("children")) {
                            list.remove(listItem);
                            String s = list.toString();
                            traverseJSONV2(jsonObject.getJSONArray("children"), s);
                        } else {
                            traversedArray.put(jsonObject);
                        }
                    } else if (jsonObject.has("children")) {
                        JSONArray children1 = jsonObject.getJSONArray("children");
                        JSONArray childObject = traverseJSONV2(children1, path);
                        if (childObject.length() > 0) {
                            traversedArray.put(childObject);
                        }
                    }
                }
            }
        }
        return traversedArray;
    }

    static JSONArray traverseJSON(JSONArray children, String path) throws JSONException {
        JSONArray traversedArray = new JSONArray();
        String substring = getSubString(path);
        for (int i = 0; i < children.length(); i++) {
            Object values = children.get(i);
            if (values instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) values;
                if (jsonObject.get("name").equals(substring)) {
                    traversedArray.put(jsonObject);
                    break;
                } else if (jsonObject.has("children")) {
                    JSONArray children1 = jsonObject.getJSONArray("children");
                    JSONArray childObject = traverseJSON(children1, path);
                    if (childObject.length() > 0) {
                        traversedArray.put(childObject);
                    }
                }
            }
        }
//        if (traversedArray.length() > 0) {
//            return traversedArray;
//        }
//        else {
//            return null;
//        }
        return traversedArray;
    }

    static JSONArray traverseJSONV3(JSONArray children, String path) throws JSONException {
        JSONArray traversedArray = new JSONArray();
        String substring = getSubString(path);
        for (int i = 0; i < children.length(); i++) {
            Object values = children.get(i);
            if (values instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) values;
                if (jsonObject.get("name").equals(substring)) {
                    if (jsonObject.has("children")) {
                        JSONArray childArray = traverseJSONV3(jsonObject.getJSONArray("children"), path);
                        for (int j = 0; j < childArray.length(); j++) {
                            traversedArray.put(childArray.get(j));
                        }
                    } else {
                        traversedArray.put(jsonObject);
                    }
                } else if (jsonObject.has("children")) {
                    JSONArray childArray = traverseJSONV3(jsonObject.getJSONArray("children"), path);
                    if (childArray.length() > 0) {
                        traversedArray.put(childArray);
                    }
                }
            }
        }
        return traversedArray;
    }

    private static String getSubString(String path) {
        int i1 = path.lastIndexOf(".");
        String substring = path.substring(i1 + 1);
        return substring;
    }

    static JSONObject makeJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray children = new JSONArray();

        JSONObject fruits = new JSONObject();
        JSONArray fruit = new JSONArray();
        JSONObject orange = new JSONObject();
        orange.put("name", "Orange");
        orange.put("quantity", "4 pcs");
        orange.put("price", "60");
        JSONObject papaya = new JSONObject();
        JSONArray papayaChild = new JSONArray();
        JSONObject papayaChildObject = new JSONObject();
        papaya.put("name", "Papaya");
        papaya.put("quantity", "2 pcs");
        papaya.put("price", "120");
        papayaChildObject.put("name", "Papaya");
        papayaChildObject.put("size", "Big");
        papayaChildObject.put("price", "60");
        papayaChild.put(papayaChildObject);
        papaya.put("children", papayaChild);
        JSONObject mango = new JSONObject();
        mango.put("name", "Mango");
        mango.put("quantity", "10 pcs");
        mango.put("price", "300");
        fruit.put(orange);
        fruit.put(papaya);
        fruit.put(mango);
        fruits.put("name", "Fruits");
        fruits.put("children", fruit);

        JSONObject groceries = new JSONObject();
        JSONArray grocery = new JSONArray();
        JSONObject rice = new JSONObject();
        rice.put("name", "Rice");
        rice.put("quantity", "10 kg");
        rice.put("price", "500");
        JSONObject sugar = new JSONObject();
        sugar.put("name", "Sugar");
        sugar.put("quantity", "5 kg");
        sugar.put("price", "215");
        JSONObject salt = new JSONObject();
        salt.put("name", "Salt");
        salt.put("quantity", "2 kg");
        salt.put("price", "60");
        grocery.put(rice);
        grocery.put(sugar);
        grocery.put(salt);
        groceries.put("name", "Grocery");
        groceries.put("children", grocery);

        children.put(fruits);
        children.put(groceries);
        jsonObject.put("name", "Ingredients");
        jsonObject.put("children", children);

        return jsonObject;
    }
}
