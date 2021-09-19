import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCP_Client {

    public static String Address;

    public static boolean test_connection(Socket socket){
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write("TesT\n");
            pw.flush();
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = br.readLine();
            return info.equals("alive");
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    public static Socket Login(String address, String username, String password,int port){
        try {
            Socket socket = new Socket(address, port);
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
                if(info.matches("Login Success")){
                    pulse(socket,2000);
                    return socket;
                }
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

    // intend for testing whether the session got an interruption
    private static void pulse(Socket socket,int interval){
        new Thread() {
            @Override
            public void run(){
                super.run();
                while(test_connection(socket)){
                    try {
                        sleep(interval);
                        //System.out.println("send test");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static Socket talk(Socket socket, String message){
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write(message+"\n");
            pw.flush();
            if(message.matches("quit")){
                if(pw!=null)pw.close();
                if(os!=null)os.close();
                if(!socket.isClosed())socket.close();
                return null;
            }
            return socket;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
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
