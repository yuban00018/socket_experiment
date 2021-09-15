import Bean.User;
import service.userService;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UDP_Server {

    public static DatagramPacket userCheck(DatagramPacket packet, User user) {
        try {
            String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
            String[] info = s.split("\n", 4);
            if (info[0].matches("l0g1nRequest")) {
                user.setUsername(info[1]);
                user.setPassword(info[2]);
                userService userService = new userService();
                String result = userService.login(user);
                if (!result.equals("fail")) {
                    byte[] data = "Login Success\n".getBytes(StandardCharsets.UTF_8);
                    packet.setData(data);
                    return packet;
                } else {
                    byte[] data = "Login Failure\n".getBytes(StandardCharsets.UTF_8);
                    packet.setData(data);
                    return packet;
                }
            }
            //String result = userService.login(user);
        } catch (Exception e) {
            byte[] data = "Login Failure\n".getBytes(StandardCharsets.UTF_8);
            packet.setData(data);
            return packet;
        }
        return packet;
    }

    public static void main(String args[]) {
        // ArrayList<User> userList = new ArrayList<User>();
        try {
            DatagramSocket ds = new DatagramSocket(6666); // 监听指定端口
            for (; ; ) {
                // 数据缓冲区:
                byte[] buffer = new byte[32768];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet); // 收取一个UDP数据包
                String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                String[] info = s.split("\n", 4);
                User user = new User();
                packet = userCheck(packet, user);
                // 发送数据:
                ds.send(packet);
                if (new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8).matches("Login Success\n")) {
                    if(!info[3].matches("l0g1nRequest"))System.out.println("User "+info[1]+" said: "+info[3]);
                } else {
                    System.out.println("unknown user access");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
