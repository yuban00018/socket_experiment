import Bean.User;

import java.io.*;
import java.net.Socket;

public class TCP_SessionThread extends Thread{
    private final Socket socket;
    public final User user;

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
            String info;
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
                while(!isInterrupted()) {
                    while (!isInterrupted()&&(info = br.readLine()) != null) {
                        // 如果是连接检测程序，直接返回alive即可
                        if(info.matches("TesT")){
                            pw.write("alive\n");
                            pw.flush();
                            continue;
                        }
                        // 收到退出请求，关闭流并关闭连接
                        else if(info.matches("quit")){
                            System.out.println("User: "+username+" disconnected");
                            disconnect(is, isr, br, os, pw);
                            // 对于退出指令，需要主动从thread_list里清除本线程，否则线程关闭后还被存储在list中
                            TCP_ServerThread.thread_list.remove(this);
                            return;
                        }
                        // 否则直接返回收到的信息
                        System.out.println("Message from " + username + " said: " + info);
                        pw.write("Received "+info+"\n");
                        pw.flush();
                    }
                }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            try{
                System.out.println("Session: "+username+" is closed");
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
