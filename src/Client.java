import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final String SERVER_IP = "192.168.101.128";

    public static void main(String[] args) throws IOException {
        // Listening socket
        Socket socket = new Socket("127.0.0.1", 8000);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner scan = new Scanner(reader);

        // Create a scanner specifically for reading the user input
        Scanner usrInput = new Scanner(System.in);
        // Display the menu and take care of invalid input
        int userInput = displayMenu(usrInput);

        // If the input is 7, we will tell the server to stop!
        while (userInput != 7) {
            long initialTime = System.nanoTime();

            // Send the request (#) to the server
            writer.println(userInput);

            // Print the response from the server
            String res;
            while (!(res = scan.nextLine()).equals("ACK")) {
                System.out.println(res);
            }

            // Calculate the response time
            double responseTime = (double) (System.nanoTime() - initialTime) / 100000000;
            System.out.printf("Response time: %.3fs\n", responseTime);

            // Ask the user for more input
            userInput = displayMenu(usrInput);
        }
    }

    private static int displayMenu(Scanner scanner) {
        System.out.println("Enter a choice(Number) from the following menu:");
        System.out.println("1. Host current Date and Time");
        System.out.println("2. Host uptime");
        System.out.println("3. Host memory use");
        System.out.println("4. Host Netstat");
        System.out.println("5. Host current users");
        System.out.println("6. Host running processes");
        System.out.println("7. Quit");
        int inp = scanner.nextInt();
        if (inp > 0 && inp < 8) {
            return inp;
        } else {
            System.out.println("Invalid option! Try again");
            return displayMenu(scanner);
        }
    }
}