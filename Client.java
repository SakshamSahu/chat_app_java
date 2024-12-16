import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;;

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
        String serverIP = JOptionPane.showInputDialog(this, "Enter Server IP Address:", "Server IP",
                JOptionPane.PLAIN_MESSAGE);

        if (serverIP != null && !serverIP.isEmpty()) {
            try {

                System.out.println("Sending request to the Server");
                socket = new Socket(serverIP, 7778);
                System.out.println("Connection Established");

                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                createGUI();
                handleEvents();
                startReading();
                // startWriting();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "IP Address is required to connect to the server.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Close the application if IP is not provided
        }

    }

    private void createGUI() {
        this.setTitle("Client Messenger [END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null); // ye center me kar dega apki window ko
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Jab cross button click kroge aapko program band ho
                                                             // jayega.

        ImageIcon originalIcon = new ImageIcon("chatlogo.png"); // Your original image
        Image img = originalIcon.getImage(); // Get the image from the icon
        Image resizedImage = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize image
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        heading.setIcon(resizedIcon);
        heading.setPreferredSize(new Dimension(100, 100));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messagearea.setEditable(false);
        // coding for component
        // font set
        messageInput.setFont(font);
        messagearea.setFont(font);
        heading.setFont(font);

        // frame layout set
        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagearea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key Released" +e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    System.out.println("You Have Pressed Entered Button");
                    String contentToSend = messageInput.getText();
                    messagearea.append("Me : " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
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
                        JOptionPane.showMessageDialog(this, "Server Terminated The Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    System.out.println("Server : " + msg);
                    messagearea.append("Server : " + msg + "\n");

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
                }
                System.out.println("Connection is Closed.");
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
