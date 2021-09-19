import Bean.User;
import service.userService;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCP_Server {

    private static Scanner sc = new Scanner(System.in);

    // 用户验证
    public static Socket userCheck(Socket socket, User user){
        InputStream is;
        InputStreamReader isr;
        BufferedReader br;
        OutputStream os;
        PrintWriter pw;
        String username = "unknown";
        try {
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            String info;
            info = br.readLine();
            // 符合登录验证头，否则是非法请求
            if (info != null && info.matches("l0g1nRequest")) {
                // 调用用户服务去查询用户合法性
                userService userService = new userService();
                username = br.readLine();
                String password = br.readLine();
                System.out.println("User: " + username + " try to login");
                user.setUsername(username);
                user.setPassword(password);
                String result = userService.login(user);
                if (!result.equals("fail")) {
                    pw.write("Login Success\n");
                    pw.flush();
                }
                else {
                    pw.write("Login Failure\n");
                    pw.flush();
                    socket.close();
                    return null;
                }
            } else {
                pw.write("Invalid Session\n");
                pw.flush();
                socket.close();
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return socket;
    }

    // 单线程服务器，不应该被使用
    public static void runServer(int port){
        System.out.println("TCP/IP Server running");
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket=null;
            while(true){
                socket = serverSocket.accept();
                InetAddress address = socket.getInetAddress();
                User user = new User();
                socket = userCheck(socket,user);
                if(socket == null){
                    System.out.println("an invalid user from IP: "+address.getAddress()+" try to login");
                } else {
                    System.out.println("user "+user.getUsername()+" login success");
                    TCP_SessionThread thread = new TCP_SessionThread(socket,user);
                    thread.start();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        // 运行单线程服务器
        // runServer(8088);
    }
}
