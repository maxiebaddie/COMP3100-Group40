import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Client {
	 private Socket s;                                                                      
	    private DataInputStream in;
	    private DataOutputStream out;
	    byte[] bytes;

	    private static final String HELO = "HELO";
	    private static final String AUTH = "AUTH " + System.getProperty("user.name");
	    private static final String xmlPath = "ds-system.xml";
	    private static final String REDY = "REDY";
	    private static final String OK = "OK";
	    private static final String SCHD = "SCHD ";                                        
	    private static final String sOK = "OK";
	    private static final String JOBN = "JO";
	    private static final String DATA = "DA";
	    private static final String JCPL = "JC";
	    private static final String NONE = "NO";
	    private static final String ERR = "ER";
	    private static final String sQUIT = "QU";
	    private static final String QUIT = "QUIT";
	    private static String SERVER;
	    private static String RCVD;
	    private static String CMD;
	    private static int jobID;

	    public Client (String address, int port) {                                                                         
	        try {
	            s = new Socket (address, port);                                         
	            in = new DataInputStream(s.getInputStream());                            
	            out = new DataOutputStream(s.getOutputStream());
	            bytes = new byte[1024];                                                 
	        } catch (IOException i) {
	            System.out.println(i);
	        }
	    }

	    private void sendMsg (String msg) {                                            
	        try {                                                                      
	            byte[] bytes = msg.getBytes();
	            out.write(bytes);
	        } catch (IOException i) {
	            System.out.println(i);
	        }
	    }

	    private String getMsg() {                                                      
	        try {
	            in.read(bytes);
	            String msg = new String(bytes);
	            return msg;
	        } catch (IOException i) {
	            System.out.println(i);
	            return null;
	        }
	    }

	    private void readSysInfo() {                                                                    // Read ds-system.xml by using JAVA parsing functions.
	        try {
	            File inputFile = new File(xmlPath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
	            doc.getDocumentElement().normalize();
	            NodeList nList = doc.getElementsByTagName("server");                             // Looks for target type of server
	            int max = 0;
	            for (int i = 0; i < nList.getLength(); i++) {                                    // Stores servers as nodes inside a list
	                Node n = nList.item(i);
	                Element e = (Element) n;
	                int cCount = Integer.parseInt(e.getAttribute("coreCount"));                  // checks coreCount attribute of each element and sorts
	                if (cCount > max) {
	                    SERVER = e.getAttribute("type");
	                    max = cCount;
	                }
	            }
	            SERVER = " " + SERVER + " 0";                                                    //Return target server type and ID as a string object
	        } catch (Exception i) {
	            System.out.println(i);
	        }
	    }
	    private void exit() {                                                                    
	        try {
	            in.close();
	            out.close();
	            s.close();
	        } catch (Exception i) {
	            System.out.println(i);
	        }
	    }
	    
	    public static void main(String[] args) {
	       
	       
	    }
}
