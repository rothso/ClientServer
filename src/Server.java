import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    static int threadCount = 0;

    public static void main(String[] args) throws IOException {
        // Start the server listening on port 8000
        ServerSocket serverSocket = new ServerSocket(8000);
        //This loop ets the server accept a new client when the current client disconnects
        while (true) {
            System.out.println("Waiting for client to connect...");

            // Accept the new client connection
            threadServer service = new threadServer(serverSocket.accept());
            System.out.println("Client #" + ++threadCount + "connected - " );
            Thread aThread = new Thread(service);
            aThread.start();
            System.out.println("Thread #" + threadCount + "started - " );


            // Set up readers and writers for the socket
            }


    }
}




class threadServer implements Runnable{

    private Socket clientSocket;

    threadServer(Socket client){
        clientSocket = client;
    }
    public void run(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    realCommand = "hostname"; //was "date"
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
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(realCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

