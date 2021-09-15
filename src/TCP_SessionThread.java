import Bean.User;
import service.userService;

import java.io.*;
import java.net.Socket;

public class TCP_SessionThread extends Thread{
    private Socket socket = null;
    private User user;

    public TCP_SessionThread(Socket socket, User user){
        this.user = user;
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        String username = user.getUsername();
        try{
            String info = null;
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
                while(true) {
                    while ((info = br.readLine()) != null) {
                        if(info.matches("quit")){
                            System.out.println("User: "+username+" disconnected");
                            disconnect(is, isr, br, os, pw);
                            return;
                        }
                        System.out.println("Message from " + username + " said: " + info);
                        pw.write("Received "+info+"\n");
                        pw.flush();
                    }
                }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            try{
                disconnect(is, isr, br, os, pw);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void disconnect(InputStream is, InputStreamReader isr, BufferedReader br, OutputStream os, PrintWriter pw) throws IOException {
        if(pw!=null)
            pw.close();
        if(os!=null)
            os.close();
        if(br!=null)
            br.close();
        if(isr!=null)
            isr.close();
        if(is!=null)
            is.close();
        if(socket!=null)
            socket.close();
    }
}
