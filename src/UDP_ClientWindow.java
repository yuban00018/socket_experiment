import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.DatagramSocket;

public class UDP_ClientWindow {
    private JButton loginButton;
    private JTextField username;
    private JPasswordField password;
    private JTextField sendText;
    private JButton sendButton;
    private JPanel MainPanel;
    private JTextField login_state;
    private UDP_Client client;
    private DatagramSocket ds;

    public UDP_ClientWindow() {
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ds = client.Login(username.getText(), new String(password.getPassword()));
                login_state.setEditable(false);
                if(ds==null) {
                    login_state.setText("login failure");
                }else
                {
                    login_state.setText("login success");
                    username.setEditable(false);
                    password.setEditable(false);
                }
                super.mouseClicked(e);
            }
        });
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(ds==null){
                    login_state.setEditable(false);
                    login_state.setText("Haven't login yet");
                    return;
                } else {
                    client.sendMessage(ds,sendText.getText());
                }
                super.mouseClicked(e);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("UDP_ClientWindow");
        frame.setContentPane(new UDP_ClientWindow().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
