import java.io.File;
import java.io.FileInputStream;

public class CodingChallenge {

    public static void main(String[] args) throws Exception {
        StringBuilder one = new StringBuilder();
        FileInputStream fis1 = new FileInputStream(new File("full-path.json"));
         
        byte b;
        while ((b = (byte) fis1.read()) != -1) {
            one.append((char) b);
        }
        JsonParser parse = new JsonParser(one.toString());
        JSONObject o = parse.getJSONObject();
        o.outputPath();
        
        System.out.println(o.getValue("itemList.items[6].id"));
        
        FileInputStream fis2 = new FileInputStream(new File("main.json"));
        one = new StringBuilder();
        while ((b = (byte) fis2.read()) != -1) {
            one.append((char)b);
        }
        JsonParser second = new JsonParser(one.toString());
        JSONObject oo = second.getJSONObject();
        
        FileInputStream ssn = new FileInputStream(new File("personalinfo-ssn.json"));
        one = new StringBuilder();
        while ((b = (byte) ssn.read()) != -1) {
            one.append((char)b);
        }
        JsonParser social = new JsonParser(one.toString());
        JSONObject insert = social.getJSONObject();
        System.out.println(oo);
        
        FileInputStream phone = new FileInputStream(new File("personalinfo-phone.json"));
        one = new StringBuilder();
        while((b = (byte) phone.read()) != -1) {
            one.append((char) b);
        }
        JsonParser phoneInfo = new JsonParser(one.toString());
        JSONObject pInfo = phoneInfo.getJSONObject();
        
        oo.setValue("itemList.items[2]", insert.toString());
        oo.setValue("itemList.items[6]", pInfo.toString());
        
        System.out.println(oo);
        
    }
}
