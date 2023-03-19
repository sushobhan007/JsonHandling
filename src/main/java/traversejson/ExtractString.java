package traversejson;

import java.util.Arrays;

public class ExtractString {
    //    public static void main(String[] args) {
//        String path = "Ingredients.Grocery.Salt";
//        String s = Arrays.toString(extractString(path));
//        System.out.println("Extract: " + s);
//    }
    public static void main(String[] args) {
        String[] strings = ExtractString.extractString("Ingredients.Grocery.Salt");
        for (String string : strings) {
            System.out.println(string);
        }
    }

    static String[] extractString(String path) {
        String[] words = path.split("\\.");
        return words;
    }
}
