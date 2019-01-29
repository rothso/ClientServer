import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static final String SERVER_IP = "192.168.101.128";

    public static void main(String[] args) throws IOException {
        // Listening socket
        Socket socket = new Socket("127.0.0.1", 8000);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("1");
    }
}