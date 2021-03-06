import javax.swing.*;import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TCP_ServerWindow {
    private JButton startButton;
    private JButton stopButton;
    private JTextField port;
    private JTextField status;
    private JPanel Server_Panel;
    private JTextField command;
    private JTextArea response;
    private static TCP_ServerThread server_thread;
    private static TCP_Server server;



    public TCP_ServerWindow() { startButton.addMouseListener(new MouseAdapter() { } );
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                status.setText("Running");
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                port.setEnabled(false);
                command.setEnabled(true);
                // 运行一个服务器线程，这样就不会卡死单线程的UI
                new Thread() {
                    @Override
                    public void run(){
                        super.run();
                        server = new TCP_Server();
                        server_thread = new TCP_ServerThread(server, Integer.valueOf(port.getText()));
                        server_thread.run();
                    }
                }.start();
                super.mouseClicked(e);
            }
        });
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(server_thread != null){
                    System.out.println("try to stop server");
                    // 终结服务器线程
                    server_thread.interrupt();
                    status.setText("Stopped");
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    command.setEnabled(false);
                    command.setText("");
                    response.setText("");
                    server_thread = null;
                }
            }
        });
    command.addKeyListener(new KeyAdapter() { } );
        command.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(e.getKeyChar() == KeyEvent.VK_ENTER){
                    if(server_thread != null){
                        if(command.getText().equals("stop")){
                            response.setText("Server Stopped");
                            System.out.println("try to stop server");
                            server_thread.interrupt();
                            status.setText("Stopped");
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);
                            command.setEnabled(false);
                            command.setText("");
                            response.setText("");
                            server_thread = null;
                            return;
                        }
                        response.setText(server_thread.command_line(command.getText()));
                    } else {
                        response.setText("Server Stopped");
                    }
                    command.setText("");
                }
            }
        });
    }public static void main(String[] args) {
        JFrame frame = new JFrame("TCP_ServerWindow");
        frame.setContentPane(new TCP_ServerWindow().Server_Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,200);
        frame.setVisible(true);
    }
}
