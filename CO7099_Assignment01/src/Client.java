import javax.crypto.Cipher;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

            try {

                File clientPrivateKey = new File(userId+".prv");
                if (clientPrivateKey.exists()) {
                    System.out.println("Client private key loaded");
                } else {
                    System.out.println("alice.prv not found");
                }

                File serverPublicKey = new File("server.pub");
                if (serverPublicKey.exists()) {
                    System.out.println("Server public key loaded");
                } else {
                    System.out.println("server.pub not found");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            SecureRandom secureRandom = new SecureRandom();
            byte[] nonce = new byte[16];
            secureRandom.nextBytes(nonce);

            String nonceBase64 = Base64.getEncoder().encodeToString(nonce);
            System.out.println("Generated Random Bytes (Nonce): " + nonceBase64);
            // Combine userid + nonce
            byte[] userBytes = userId.getBytes();
            byte[] combined = new byte[userBytes.length + nonce.length];

            System.arraycopy(userBytes, 0, combined, 0, userBytes.length);
            System.arraycopy(nonce, 0, combined, userBytes.length, nonce.length);

            System.out.println("Combined data length: " + combined.length);


// Load server public key
            byte[] serverKeyBytes = Files.readAllBytes(Paths.get("server.pub"));

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(serverKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey serverPublicKeyObj = keyFactory.generatePublic(keySpec);


// Encrypt combined data
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, serverPublicKeyObj);

            byte[] encryptedBytes = cipher.doFinal(combined);

            System.out.println("Encrypted data length: " + encryptedBytes.length);



            System.out.println("Userid sent to server.");

            // Close resources
            writer.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

