import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
        // Server socket
        ServerSocket serverSocket = new ServerSocket(8000);

        // This loop lets the server accept a new client when the current client disconnects
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printOnClient = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner scanner = new Scanner(reader);
            String realCommand;

            while (scanner.hasNextLine()) {
                System.out.println("running/ Waiting");
                String command = scanner.nextLine();
                System.out.println(command);

                int c = Integer.parseInt(command);
                switch (c) {
                    case 1:
                        realCommand = "git --version"; // Change it to "date" on Linux
                        break;
                    case 2:
                        realCommand = "uptime";
                        break;
                    case 3:
                        realCommand = "free";
                        break;
                    case 4:
                        realCommand = "netstat";
                        break;
                    case 5:
                        realCommand = "who";
                        break;
                    case 6:
                        realCommand = "top";
                        break;
                    default:
                        realCommand = "quit";
                }
                if (!realCommand.equals("quit")) {
                    Process process = Runtime.getRuntime().exec(realCommand);
                    BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    Scanner commandScanner = new Scanner(commandReader);

                    while (commandScanner.hasNextLine()) {
                        String line = commandScanner.nextLine();
                        printOnClient.println(line);
                        System.out.println(line);
                    }

                    // Tell the client to stop listening
                    printOnClient.println("ACK");
                } else {
                    break;
                }
            }
            System.out.println("Client disconnected.");
        }
    }
}
