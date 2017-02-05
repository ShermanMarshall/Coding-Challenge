import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class CodingChallenge {

    public static void one() throws IOException {
        StringBuilder one = new StringBuilder();
        FileInputStream fis1 = new FileInputStream(new File("full-path.json"));
         
        byte b;
        while ((b = (byte) fis1.read()) != -1) {
            one.append((char) b);
        }
        JsonParser parse = new JsonParser(one.toString());
        JSONObject o = parse.getJSONObject();
        o.outputPath();
        
        Scanner in = new Scanner(System.in);
        String input = "";
        do {
            System.out.print("Input a value to search for\nEnter PATH to output available paths\nOr, enter EXIT to terminate: ");
            input = in.nextLine().trim();
            if ((input != null) && (input.equals("PATH"))) {
                o.outputPath();
            }
            if ((input != null) && (!input.equals("EXIT"))) {
                System.out.println(o.getValue(input));
            }
        } while ((input != null) && (!input.equals("EXIT")));
    }
    
    public static void two() throws IOException {
        StringBuilder one = new StringBuilder();
        
        FileInputStream fis2 = new FileInputStream(new File("main.json"));
        
        byte b;
        while ((b = (byte) fis2.read()) != -1) {
            one.append((char) b);
        }
        
        JsonParser parse = new JsonParser(one.toString());
        JSONObject o = parse.getJSONObject();
        o.outputPath();        
        
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
        
    public static void three() throws IOException {
	StringBuilder one = new StringBuilder();
        FileInputStream fis3 = new FileInputStream(new File("findValue.json")); 
        
        byte b;
        while ((b = (byte) fis3.read()) != -1) {
            one.append((char) b);
        }
        JsonParser parser = new JsonParser(one.toString());
        JSONObject object = parser.getJSONObject();
        
        System.out.println(object);
        
        String path, element, idValue;
        Scanner in = new Scanner(System.in);
        System.out.println("Showing output for path=itemList.items.subItems, element=label, id=subItem1Item2");
        
        System.out.println(object.findValue(path = "itemList.items.subItems", element = "label", idValue = "subItem1Item2"));
    }
    
    public static void main(String[] args) throws Exception {
        StringBuilder one = new StringBuilder();
        try {
            System.out.println("-------------------------------------------");
            System.out.println("The following is output for problem 1");
            System.out.println("-------------------------------------------");
            one();
            
            System.out.println("\n\n\n");
            System.out.println("-------------------------------------------");
            System.out.println("The following is output for problem 2");
            System.out.println("-------------------------------------------");
                        
            two();
            
            System.out.println("\n\n\n");
            System.out.println("-------------------------------------------");
            System.out.println("The following is output for problem 3");
            System.out.println("-------------------------------------------");
            
            three();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
