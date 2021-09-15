import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDP_Client {
    private static String name;
    private static String passwd;
    private static Scanner sc = new Scanner(System.in);

    public static DatagramSocket sendMessage(DatagramSocket ds, String message){
        message = "l0g1nRequest\n" + name + "\n" + passwd +"\n"+ message;
        System.out.println("send "+message);
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length);
        try {
            ds.send(packet);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ds;
    }

    public static DatagramSocket Login(String username, String pwd) {
        try {
            DatagramSocket ds = new DatagramSocket();
            ds.setSoTimeout(1000);
            ds.connect(InetAddress.getByName("localhost"), 6666); // 连接指定服务器和端口
            // 发送:
            byte[] data = ("l0g1nRequest\n" + username + "\n" + pwd +"\n"+"l0g1nRequest").getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            ds.send(packet);
            // 接收:
            byte[] buffer = new byte[32768];
            packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet);
            String resp = new String(packet.getData(), packet.getOffset(), packet.getLength());
            System.out.print(resp);

            if (resp.matches("Login Success\n")) {
                name = username;
                passwd = pwd;
                return ds;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String args[]){
        try {
            DatagramSocket ds = new DatagramSocket();
            ds.setSoTimeout(1000);
            ds.connect(InetAddress.getByName("localhost"), 6666); // 连接指定服务器和端口
// 发送:
            System.out.print("login: ");
            String username = sc.nextLine();
            System.out.print("pwd: ");
            String pwd = sc.nextLine();
            username = "test";
            pwd = "testpwd";
            byte[] data = ("l0g1nRequest\n"+username+"\n"+pwd).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            ds.send(packet);
// 接收:
            byte[] buffer = new byte[32768];
            packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet);
            String resp = new String(packet.getData(), packet.getOffset(), packet.getLength());
            System.out.print(resp);

            if(resp.matches("Login Success\n")) {
                byte[] input = new byte[32768];
                while (true) {
                    if (sc.hasNextLine()) {
                        String Input = sc.nextLine();
                        input = Input.getBytes();
                        packet = new DatagramPacket(input, input.length);
                        ds.send(packet);
                    }
                }
            }
            ds.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
