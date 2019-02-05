import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final String SERVER_IP = "192.168.101.128";

    public static void main(String[] args) throws IOException {
        // Listening socket
        Socket socket = new Socket("127.0.0.1", 8000);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while(true)
        {
            int userInput=displayMenu();    // displays menu and takes care of the invalid input
            writer.print(userInput);        //if the inp=7, we will tell the server to stop!
            if(userInput==7)
                break;
            Scanner scan= new Scanner(reader);
            while(scan.hasNext())
            {
                System.out.println(scan.next());
            }
        }

    }
    public static int displayMenu() {
        Scanner usrInput = new Scanner(System.in);
        System.out.println("Enter a choice(Number) from the following menu:");
        System.out.println("1. Host current Date and Time");
        System.out.println("2. Host uptime");
        System.out.println("3. Host memory use");
        System.out.println("4. Host Netstat");
        System.out.println("5. Host current users");
        System.out.println("6. Host running processes");
        System.out.println("7. Quit");
        int inp = usrInput.nextInt();
        if (inp > 0 && inp < 8) {
            return inp;
        } else {
            System.out.println("Invalid option! Try again");
            return displayMenu();
        }
    }
}