import java.util.HashMap;

public class JSONObject extends HashMap<String, Object> {
       
     public String findPathForElement(String element) {
         for (String s : this.keySet()) {
            Object obj = this.get(s);
            Class c = obj.getClass();
            if (c.equals(JSONObject.class) || c.equals(JSONArray.class)) {               
                if (c.equals(JSONObject.class)) {
                    JSONObject o = (JSONObject) this.get(s);
                    
                } else {

                }
            }
            
         }
         return null;
     }
     
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
}
