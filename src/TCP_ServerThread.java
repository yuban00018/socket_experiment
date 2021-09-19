import Bean.User;

import java.net.*;
import java.util.ArrayList;

public class TCP_ServerThread extends Thread {

    private final TCP_Server server;
    private final int port;
    private ArrayList<TCP_SessionThread> thread_list;

    public TCP_ServerThread(TCP_Server server,int port){
        this.server = server;
        this.port = port;
        thread_list = new ArrayList<>();
    }

    // 查找正在运行的会话
    private String show_all_sessions(){
        if(thread_list.isEmpty())return "no session running\n";
        StringBuilder name_list = new StringBuilder();
        name_list.append("Running Sessions: \n");
        for(TCP_SessionThread thread:thread_list){
            name_list.append(thread.user.getUsername()+"\n");
        }
        return name_list.toString();
    }

    // 终结会话
    private boolean find_and_kill(String username) {
        for(TCP_SessionThread thread : thread_list) {
            if(thread.user.getUsername().equals(username)) {
                thread_list.remove(thread);
                thread.interrupt();
                return true;
            }
        }
        return false;
    }

    // 显示帮助信息
    private static String help(){
        return """
                commands:
                ls : list all sessions
                kill <id> : kill specified session
                stop : stop the server
                help : show manual""";
    }

    // 接受服务器控制命令
    public String command_line(String Command){
        String[] command = Command.split(" ");
        boolean success = false;
        switch (command[0]){
            case "kill":
                success = find_and_kill(command[1]);
                if(success)return "Session: "+command[1]+" killed\n";
                break;
            case "ls":
                return show_all_sessions();
            case "help":
                return help();
            default:
                break;
        }
        if(!success)return "failed";
        else return "done";
    }

    @Override
    public void run(){
        System.out.println("TCP/IP Server running");
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1);
            Socket socket;
            while(!isInterrupted()) {
                try {
                    socket = serverSocket.accept();
                    InetAddress address = socket.getInetAddress();
                    User user = new User();
                    socket = TCP_Server.userCheck(socket, user);
                    if (socket == null) {
                        System.out.println("an invalid user from IP: " + address.getHostAddress() + " try to login");
                    } else {
                        System.out.println("user " + user.getUsername() + " login success");
                        TCP_SessionThread thread = new TCP_SessionThread(socket, user);
                        thread.start();
                        thread_list.add(thread);
                        // System.out.println(thread_list);
                    }
                } catch (SocketTimeoutException e) {
                    // just continue
                }
            }
            for(TCP_SessionThread thread : thread_list){
                System.out.println("Try to stop thread: "+thread.user.getUsername());
                thread.interrupt();
            }
            System.out.println("TCP/IP Server Stopped");
            if(!serverSocket.isClosed())serverSocket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
