import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCP_Client {

    public static String Address;

    public static boolean Connect(String address){
        try {
            Socket socket = new Socket(address, 8088);

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write("test connection");
            pw.flush();
            socket.shutdownOutput();

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info;
            while((info = br.readLine())!=null){
                System.out.println("Client receive message from server: "+info);
            }

            br.close();
            is.close();
            os.close();
            pw.close();
            socket.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static Socket Login(String address, String username, String password){
        try {
            Socket socket = new Socket(address, 8088);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write("l0g1nRequest\n");
            pw.flush();
            pw.write(username+"\n");
            pw.flush();
            pw.write(password+"\n");
            pw.flush();
            //socket.shutdownOutput();

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info;
            while((info = br.readLine())!=null){
                System.out.println(info);
                if(info.matches("Login Success"))return socket;
            }

            br.close();
            is.close();
            os.close();
            pw.close();
            socket.close();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Socket Socket_Connect(Socket socket, String message){
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write(message+"\n");
            pw.flush();
            if(message.matches("quit"))return null;
            return socket;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void SendMessage(String address, String message){
        try {
            Socket socket = new Socket(address, 8088);

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write(message);
            pw.flush();
            socket.shutdownOutput();

            os.close();
            pw.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            Socket socket = new Socket("localhost", 8088);

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            System.out.print("Input your message: ");
            Scanner input = new Scanner(System.in);
            String message = input.nextLine();
            pw.write(message);
            pw.flush();

            socket.shutdownOutput();

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while((info = br.readLine())!=null){
                System.out.println("Client receive message from server: "+info);
            }

            br.close();
            is.close();
            os.close();
            pw.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
