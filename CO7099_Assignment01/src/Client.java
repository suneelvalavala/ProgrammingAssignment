import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        // Check arguments: host, port, userid
        if (args.length < 3) {
            System.out.println("Usage: java Client <host> <port> <userid>");
            System.exit(0);
        }

        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String userId = args[2];

            // Create socket connection to server
            Socket socket = new Socket(host, port);

            // Create writer to send data
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Send userid as one line
            writer.println(userId);

            System.out.println("Userid sent to server.");

            // Close resources
            writer.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

