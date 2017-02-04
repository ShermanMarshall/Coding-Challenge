import java.io.File;
import java.io.FileInputStream;

public class CodingChallenge {

    public static void main(String[] args) throws Exception {
        StringBuilder one = new StringBuilder();
        StringBuilder two = new StringBuilder();
        StringBuilder three = new StringBuilder();
        
        FileInputStream fis1 = new FileInputStream(new File("src/luiz/coding/challenge/full-path.json"));
        FileInputStream fis2 = new FileInputStream(new File("src/luiz/coding/challenge/main.json"));
        //FileInputStream fis3 = new FileInputStream(new File("personalinfo-ssn.json"));
        
        byte b;int x = 0;
        while ((b = (byte) fis1.read()) != -1) {
            //System.out.println(x++ +"-"+(char)b);
            one.append((char) b);
        }
	System.out.println(one.toString());
        fis1.close();
        
        JsonParser test1 = new JsonParser(one.toString());
        
        while((b = (byte) fis2.read()) != -1) {
            two.append((char) b);
        }
 	System.out.println(two.toString());
        fis2.close();
        JsonParser test2 = new JsonParser(two.toString());
    }
}
