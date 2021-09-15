import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class TCP_ClientWindow {
    private JPanel MainPanel;
    private JButton sendButton;
    private JTextField textField1;
    private JPanel LoginPanel;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JTextField login_state;
    private JTextField login_address;
    private JPanel ClientPanel;
    private JTextField server_response;
    private static JFrame frame;
    private static String my_name;
    private String my_password;
    private static Socket socket;

    public TCP_ClientWindow() {
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(socket!=null){
                    // 发送消息
                    socket= TCP_Client.Socket_Connect(socket, textField1.getText());
                    if(socket==null){
                        System.out.println(my_name + " logout");
                        ClientPanel.setVisible(false);
                        // 退回到登陆面板
                        frame.setContentPane(new TCP_ClientWindow().LoginPanel);
                        frame.pack();
                    } else {
                        try{
                            InputStream is = socket.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            //因为是readline所以如果发的消息没有换行那就永远读不进去
                            String info = br.readLine();
                            server_response.setText(info);
                            server_response.setEditable(false);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
                else {
                    System.out.println(my_name + " logout");
                    ClientPanel.setVisible(false);
                    frame.setContentPane(new TCP_ClientWindow().LoginPanel);
                    frame.pack();
                }
            }
        });
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                my_name = username.getText();
                socket = TCP_Client.Login(login_address.getText(),username.getText(),new String(password.getPassword()));
                try {
                    if (socket!=null) {
                        LoginPanel.setVisible(false);
                        frame.setContentPane(new TCP_ClientWindow().ClientPanel);
                        frame.setSize(400,150);
                    } else {
                        login_state.setText("Login Failed");
                        login_state.setEditable(false);
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                super.mouseClicked(e);
            }
        });

    }

    public static void main(String[] args) {
        frame = new JFrame("ClientWindow");
        frame.setContentPane(new TCP_ClientWindow().LoginPanel);
        //frame.setContentPane(new ClientWindow().ClientPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
