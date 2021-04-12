import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Client {

    private Socket s;
    private DataInputStream in;
    private DataOutputStream out;
    byte[] bytes;

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

    private void readSysInfo() {
        
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