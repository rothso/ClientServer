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
            System.out.println("Waiting to connect...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printOnClient = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("running/Waiting for the command");
            Scanner scanner = new Scanner(reader);
            String realCommand;

            while (scanner.hasNextLine()) {
//                System.out.println("running/Waiting for the command");
                String command = scanner.nextLine();
                System.out.println("Command received");
//                System.out.println(command);

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
                    System.out.println("Running the command on the machine");
                    Process process = Runtime.getRuntime().exec(realCommand);
                    BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    Scanner commandScanner = new Scanner(commandReader);
                    System.out.println("Sending the information to the client");
                    while (commandScanner.hasNextLine()) {
                        String line = commandScanner.nextLine();
                        printOnClient.println(line);
                        System.out.println(line);
                    }

                    // Tell the client to stop listening
                    System.out.println("Ack sent");
                    printOnClient.println("ACK");
                    System.out.println("running/Waiting for the command");
                } else {
                    break;
                }
            }
            System.out.println("Client disconnected.");
        }
    }
}
