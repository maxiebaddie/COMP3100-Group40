import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Client {
	 private Socket s;                                                                      
	    private DataInputStream in;
	    private DataOutputStream out;
	    byte[] bytes;
	    private static final Boolean server_newline = true;

	    private static final String HELO = "HELO";
	    private static final String AUTH = "AUTH " + System.getProperty("user.name");
	    private static final String xmlPath = "ds-system.xml";
	    private static final String REDY = "REDY";
	    private static final String OK = "OK";
	    private static final String SCHD = "SCHD ";                                        //Initialise all variable commands into 2 chars 
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
	            s = new Socket (address, port);                                         // Initialises the socket object with inputs
	            in = new DataInputStream(s.getInputStream());                            // Initialises data stream objects
	            out = new DataOutputStream(s.getOutputStream());
	            bytes = new byte[1024];                                                 // Initialises byte array for transmitting messages
	        } catch (IOException i) {
	            System.out.println(i);
	        }
	    }

	    private void sendMsg (String msg) {                                            // Communicate with the server by making conversions between string
	        try {                                                                      // objects and byte arrays
	            if (server_newline) { msg = msg + "\n"; }
	            byte[] bytes = (msg).getBytes();
	            out.write(bytes);
	        } catch (IOException i) {
	            System.out.println(i);
	        }
	    }

	    private String getMsg() {                                                      // Gets the messages from the server in bytes and converts to strings.
	        try {
	            in.read(bytes);
	            String msg = new String(bytes);
	            return msg.trim();
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
	    private void exit() {                                                                    //Ends simulation when an error is recieved or quit message from server
	        try {
	            in.close();
	            out.close();
	            s.close();
	        } catch (Exception i) {
	            System.out.println(i);
	        }
	    }
	    
	    public static void main(String[] args) {
	        Client c = new Client("localhost", 50000);
	        c.sendMsg(HELO);
	        c.getMsg();
	        c.sendMsg(AUTH);
	        c.readSysInfo(); 
	        while(true) {
	        	RCVD = c.getMsg();
	        	CMD = RCVD.substring(0,2);                                    //Substrings the incoming messages so it can decipher what scheduling or return message is required
	        	if(CMD.equals(sOK)) c.sendMsg(REDY);                          // It looks at the first 2 characters
	        	else if (CMD.equals(NONE)) c.sendMsg(QUIT);
	        	else if (CMD.equals(sQUIT)) {
	        		c.exit();
	        		break;
	        		}
	        	else if (CMD.equals(DATA)) c.sendMsg(OK);                     // Follows the ds-client documentation required responses
	        	else if (CMD.equals(JCPL)) c.sendMsg(REDY);               
	        	else if (CMD.equals(ERR)) c.sendMsg(QUIT);
	        	else if (CMD.equals(JOBN)) {
	        	 c.sendMsg(SCHD + jobID + SERVER);                           // If jobn is sent the client will schedule with the jobid and server info
	        	 jobID++;
	        	 }
	        	
	        }
	       
	    }
}
