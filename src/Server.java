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
        Socket clientSocket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter printOnClient = new PrintWriter(clientSocket.getOutputStream());
        Scanner scanner = new Scanner(reader);

        String command = scanner.next();
        String realCommand="";
        int c = Integer.parseInt(command);
        switch (c){
            case 1:
                realCommand="date";
                break;
            case 2:
                realCommand="uptime";
                break;
            case 3:
                realCommand="free";
                break;
            case 4:
                realCommand="netstat";
                break;
            case 5:
                realCommand="who";
                break;
            case 6:
                realCommand="top";
                        break;
            default:
                realCommand="quit";
        }
        if (!realCommand.equals("quit")) {
            Process process = Runtime.getRuntime().exec(realCommand);
            BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Scanner commandScanner = new Scanner(commandReader);
            while (commandScanner.hasNext()) {
                printOnClient.write(commandScanner.next());
            }
        }

    }
}
