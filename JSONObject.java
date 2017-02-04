import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class JSONObject extends HashMap<String, Object> {
     TreeMap<String, Object> path;
     
    public boolean hasKey(String key) {
        return this.path.containsKey(key);
    }
     
     public Object getValue(String key) {
        Object o = null;
        if (hasKey(key)) {
            String[] components = key.split("\\.");
            if (components != null) {
                for (String s : components) {
                    if (o == null) {
                        o = this.get(s);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        String prefix = null;
                        if (s.charAt(s.length()-1) == ']') {
                            int x = s.length()-2;
                            while ((s.charAt(x) != '[') && (x > 0)) {
                                sb.append(s.charAt(x--));
                            }
                            sb = sb.reverse();
                            prefix = s.substring(0, x);
                        }
                        if (prefix != null) {
                            o = ((JSONArray) ((JSONObject) o).get(prefix));
                        }
                        if (o.getClass().equals(JSONObject.class)) {
                            o = ((JSONObject) o).get(s);
                        } else if (o.getClass().equals(JSONArray.class)) {                            
                            try {
                                System.out.println(sb.toString());
                                o = ((JSONArray) o).set.get(Integer.parseInt(sb.toString()));
                            } catch (NumberFormatException nfe) {
                                return o;
                            }
                        } else { 
                            return null;
                        }
                    }
                }
            }
        }
        return o;
    }
     
     public void setValue(String key, Object value) {
        Object o = null;
        if (hasKey(key)) {
            String[] components = key.split("\\.");
            if (components != null) {
                for (String s : components) {
                    if (o == null) {
                        o = this.get(s);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        String prefix = null;
                        if (s.charAt(s.length()-1) == ']') {
                            int x = s.length()-2;
                            while ((s.charAt(x) != '[') && (x > 0)) {
                                sb.append(s.charAt(x--));
                            }
                            sb = sb.reverse();
                            prefix = s.substring(0, x);
                        }
                        if (prefix != null) {
                            o = ((JSONArray) ((JSONObject) o).get(prefix));
                        }
                        if (o.getClass().equals(JSONObject.class)) {
                            o = ((JSONObject) o).get(s);
                        } else if (o.getClass().equals(JSONArray.class)) {                            
                            try {
                                System.out.println(sb.toString());
                                o = ((JSONArray) o).set.get(Integer.parseInt(sb.toString()));
                            } catch (NumberFormatException nfe) {
                                return;
                            }
                        } else { 
                            return;
                        }
                    }
                }
            }
        }
        return;
    }
     
     public void init() {
         TreeMap<String, Object> map = new TreeMap();
         for (String s : this.keySet()) { 
             map.put(s, this.get(s));
         }
         
         boolean iterate = false;
         TreeMap<String, Object> other;
         int xx = 0;
         do {
             iterate = false;
             other = new TreeMap();
             for (String s : map.keySet()) {
                 Object o = map.get(s);
                 //System.out.println(o.getClass());
                 if (o.getClass().equals(JSONObject.class) || o.getClass().equals(JSONArray.class)) {
                     if (o.getClass().equals(JSONObject.class)) {
                         //System.out.println("runs");
                         JSONObject obj = (JSONObject) map.get(s);
                         for (String key : obj.keySet()) {
                             Object inner = obj.get(key);
                             if (inner.getClass().equals(JSONObject.class)) {
                                 String idx = new StringBuilder(s).append(".").append(key).toString();
                                 Object jsonObj = map.get(idx);
                                 if (jsonObj == null) {
                                    other.put(idx, inner);
                                    iterate = true;
                                 }
                             } else if (inner.getClass().equals(JSONArray.class)) {
                                 JSONArray a = (JSONArray) inner;
                                 for (int x = 0; x < a.set.size(); x++) {
                                     String idx = new StringBuilder(s).append(".").append(key).append("[").append(x).append("]").toString();
                                     Object arrayObj = map.get(idx);
                                     if (arrayObj == null) {
                                        other.put(idx, a.set.get(x));
                                        iterate = true;
                                     }
                                 }
                             } else {
                                 String idx = new StringBuilder(s).append(".").append(key).toString();
                                 other.put(idx, inner);
                             }
                             other.put(new StringBuilder(s).append(".").append(key).toString(), inner);
                         }
                     } else {
                         JSONArray a = (JSONArray) map.get(s);
                         for (int x = 0; x < a.set.size(); x++) {
                             String idx = new StringBuilder(s).append("[").append(x).append("]").toString();
                             Object jarry = map.get(idx);
                             if (jarry == null) {
                                other.put(idx, a.set.get(x));
                                iterate = true;
                             }
                         }
                     }
                 }
                 other.put(s, map.get(s));
             }
             map = new TreeMap();
             for (String s : other.keySet()) {
                 map.put(s, other.get(s));
             }
         } while (iterate);
         
         path = other;
         
     }
     
    public void outputPath() {
        for (String s : path.keySet()) {
            System.out.println(s + " : " + path.get(s));
        }
    }
     
     /*
     public String objectToString(Object o) {
         StringBuilder sb = new StringBuilder();
         if (o.getClass() == String.class) {
             return sb.append("\"").append(o).append("\"").toString();
         } else if (o.getClass() == JSONObject.class) {
             return ((JSONObject)o).toJson();
         } else if (o.getClass() == JSONArray.class) {
             return ((JSONArray)o).toJson();
         } else
             return String.valueOf(o);
     }
     public String toJson() {
         StringBuilder sb = new StringBuilder("{");
         for (String s : this.keySet()) {
             sb.append('"').append(s).append('"').append(":").append(this.get(s).toString()).append(",");
         }
         sb.substring(0, sb.length()-1);
         return sb.append("}").toString();
     }
     */
     public String toString() {
         StringBuilder sb = new StringBuilder("{");
         for (String s : this.keySet()) {
             Object o = this.get(s);
             String value = null;
             //System.out.println(o + " " + o.getClass());
             if (o.getClass().equals(String.class)) {
                 value = new StringBuilder("\"").append(o).append("\"").toString();
             } else if (o.getClass().equals(Double.class)) {
                 value = Double.toString(((Double)o));
             } else if (o.getClass().equals(Boolean.class)) {
                 value = Boolean.toString(((Boolean) o));
             } else {
                 value = o.toString();
             }
             sb.append('"').append(s).append('"').append(":").append(value).append(",");
         }
         sb = new StringBuilder(sb.substring(0, sb.length()-1));
         return sb.append("}").toString();
     }
     
     public static void a(String...args) {
         String s = "abcdaefg";
         String[] strs = s.split("\\.");
         for (String ss : strs) {
             System.out.println(ss);
         }
     }
     
     public static void main (String...args) throws Exception {
        StringBuilder one = new StringBuilder();
        FileInputStream fis1 = new FileInputStream(new File("src/luiz/coding/challenge/full-path.json"));
         
        byte b;int x = 0;
        while ((b = (byte) fis1.read()) != -1) {
            //System.out.println(x++ +"-"+(char)b);
            one.append((char) b);
        }
        JsonParser parse = new JsonParser(one.toString());
        JSONObject o = parse.getJSONObject();
        
        System.out.println(o.getValue("itemList.items[6].id"));
        
        FileInputStream fis2 = new FileInputStream(new File("src/luiz/coding/challenge/main.json"));
        one = new StringBuilder();
        while ((b = (byte) fis2.read()) != -1) {
            one.append((char)b);
        }
        JsonParser second = new JsonParser(one.toString());
        JSONObject oo = second.getJSONObject();
        System.out.println(oo);

	/*
        FileInputStream ssn = new FileInputStream(new File("src/luiz/coding/challenge/personalinfo-phone.json"));
        one = new StringBuilder();
        while ((b = (byte) ssn.read()) != -1) {
            one.append((char)b);
        }
        JsonParser social = new JsonParser(one.toString());
        JSONObject insert = social.getJSONObject();
        System.out.println(oo);
        
        //oo.setValue("itemList.items[2]", insert.toString());
        
        System.out.println(oo);
        */
     }
}
