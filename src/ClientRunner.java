import java.io.IOException;
import java.util.stream.DoubleStream;

class ClientRunner {

    public static void main(String[] args) {
        // We will be running client batches with these numbers of clients
        int[] numClients = new int[]{1, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

        // Run the light loads
        System.out.println("Light Load (date)");
        for (int numClient : numClients) {
            double avgResponse = runClients(numClient, 1);
            System.out.printf("%3d clients - %.5fs\n", numClient, avgResponse);
        }

        // Run the heavy loads
        System.out.println("\nHeavy Load (netstat)");
        for (int numClient : numClients) {
            double avgResponse = runClients(numClient, 4);
            System.out.printf("%3d clients - %.5fs\n", numClient, avgResponse);
        }
    }

    public static double runClients(int numClients, int request) {
        double[] responseTimes = new double[numClients];
        Thread[] threads = new Thread[numClients];

        // Create a Thread for each client
        for (int i = 0; i < numClients; i++) {
            final int n = i;
            threads[i] = new Thread(() -> {
                try (Client client = new Client(InteractiveRunner.SERVER_IP, 8000)) {
                    Client.Response res = client.request(request);
                    responseTimes[n] = res.time;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // Start all the threads in parallel
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for all the threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Return average response time
        return DoubleStream.of(responseTimes).average().orElse(0);
    }
}
