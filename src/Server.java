import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
        // Start the server listening on port 8000
        ServerSocket serverSocket = new ServerSocket(8000);

        // This loop lets the server accept a new client when the current client disconnects
        while (true) {
            System.out.println("Waiting for client to connect...");

            // Accept the new client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            Thread childServer = new Thread(() -> {
                try {
                    // Set up readers and writers for the socket
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    Scanner scanner = new Scanner(reader);

                    // Read each input line from the client
                    String realCommand;
                    while (scanner.hasNextLine()) {
                        String command = scanner.nextLine();
                        System.out.println("Command received");

                        // Parse the command number we received from the client
                        int c = Integer.parseInt(command);
                        switch (c) {
                            case 1:
                                realCommand = "date";
                                break;
                            case 2:
                                realCommand = "uptime";
                                break;
                            case 3:
                                realCommand = "free";
                                break;
                            case 4:
                                realCommand = "netstat -an";
                                break;
                            case 5:
                                realCommand = "who";
                                break;
                            case 6:
                                realCommand = "ps -e";
                                break;
                            default:
                                realCommand = "quit";
                        }

                        // Exit the loop if the command was invalid (outside 0-6)
                        if (realCommand.equals("quit"))
                            break;

                        // Run the command if it was a valid request
                        System.out.println("Running command " + realCommand);
                        Process process = Runtime.getRuntime().exec(realCommand);
                        BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        Scanner commandScanner = new Scanner(commandReader);

                        // Write the output to the client
                        while (commandScanner.hasNextLine()) {
                            String line = commandScanner.nextLine();
                            writer.println(line);
                        }

                        // Tell the client to stop listening
                        writer.println("ACK");
                    }

                    // Client is done sending input, close the socket on the server
                    System.out.println("Client disconnected.");
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Note: use start() for concurrent and run() for sequential
            childServer.start();
        }
    }
}
