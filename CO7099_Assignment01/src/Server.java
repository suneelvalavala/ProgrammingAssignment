import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: java Server <port>");
            System.exit(0);
        }

        try {
            int port = Integer.parseInt(args[0]);

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server waiting...");

            while (true) {

                // Accept client connection
                Socket socket = serverSocket.accept();

                System.out.println("Connection from IP: " +
                        socket.getInetAddress());

                // Create reader
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                // Read userid
                String userId = reader.readLine();

                // Null check
                if (userId != null) {
                    try {
                        // Load server private key
                        File serverPrivateKey = new File("server.prv");
                        if (serverPrivateKey.exists()) {
                            System.out.println("Server private key loaded");
                        } else {
                            System.out.println("server.prv not found");
                        }

                        // Load client's public key
                        File clientPublicKey = new File(userId + ".pub");
                        if (clientPublicKey.exists()) {
                            System.out.println("Client public key loaded: " + clientPublicKey.getName());
                        } else {
                            System.out.println("Client public key not found");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("Client disconnected before sending userid");
                }

                // Close resources
                reader.close();
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

