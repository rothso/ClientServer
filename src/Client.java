import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Closeable {
    private final PrintWriter writer;
    private final Scanner reader;

    public static class Response {
        public final String body;
        public final double time;

        public Response(String body, double time) {
            this.body = body;
            this.time = time;
        }
    }

    public Client(String address, int port) throws IOException {
        // Open a connection connection to the server
        Socket socket = new Socket(address, port);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
    }

    public Response request(int data) {
        long initialTime = System.nanoTime();

        // Send the request (#) to the server
        writer.println(data);

        // Get the response from the server
        String serverResponse;
        StringBuilder body = new StringBuilder();
        while (!(serverResponse = reader.nextLine()).equals("ACK")) {
            body.append(serverResponse + "\n");
        }

        // Calculate the response time
        double responseTime = (double) (System.nanoTime() - initialTime) / 100000000;

        // Return a tuple of the response body and response time
        return new Response(body.toString(), responseTime);
    }

    @Override
    public void close() {
        writer.close();
        reader.close();
    }
}
