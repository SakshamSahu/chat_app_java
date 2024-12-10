import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    Socket socket;
    ServerSocket server;
    BufferedReader br;
    PrintWriter out;

    public Server() {

        try {

            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void startReading() {

        // thread - read krke deta rhega
        Runnable r1 = () -> {
            System.out.println("Reader Started");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("quit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);

                }
            } catch (Exception e) {
                // e.getStackTrace();
                System.out.println("Connection is Closed.");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {

        // thread - data user se lega and then send krega client tak
        Runnable r2 = () -> {
            System.out.println("Writer Started...");
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
                e.getStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is server....going to start server");
        new Server();

    }

}