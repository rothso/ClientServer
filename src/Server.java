import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
        // Server socket
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket clientSocket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Scanner scanner = new Scanner(reader);

        String command = scanner.next();

        if (command.equals("1")) {
            Process process = Runtime.getRuntime().exec("git --version");
            BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Scanner commandScanner = new Scanner(commandReader);

            while (commandScanner.hasNext()) {
                System.out.println(commandScanner.next());
            }
        }
    }
}
