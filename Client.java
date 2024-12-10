import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.Font;
import java.awt.font.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;;

public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // Declare Components for GUI
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messagearea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // constructor
    public Client() {
        try {

            // System.out.println("Sending request to the Server");
            // socket = new Socket("192.168.2.29", 7777);
            // System.out.println("Connection Established");

            // br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // out = new PrintWriter(socket.getOutputStream());

            createGUI();
            // startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createGUI() {
        this.setTitle("Client Messenger [END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null); // ye center me kar dega apki window ko
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Jab cross button click kroge aapko program band ho
                                                             // jayega.

        // coding for component
        // font set
        messageInput.setFont(font);
        messagearea.setFont(font);
        heading.setFont(font);

        // frame layout set
        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        this.add(messagearea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    // start reading method
    public void startReading() {
        // thread - read krke deta rhega
        Runnable r1 = () -> {
            System.out.println("Reader Started");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("quit")) {
                        System.out.println("Server Terminated The Chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server : " + msg);

                }
            } catch (Exception e) {

                // e.printStackTrace();
                System.out.println("Connection is Closed.");
            }

        };
        new Thread(r1).start();
    }

    // start writing method
    public void startWriting() {
        // thread - data user se lega and then send krega client tak
        Runnable r2 = () -> {
            System.out.println("Writer Started");
            try {
                while (true && !socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                    System.out.println("Connection is Closed.");

                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        };
        new Thread(r2).start();
    }

    // main method
    public static void main(String[] args) {
        System.out.println("This is client....");
        new Client();
    }
}
