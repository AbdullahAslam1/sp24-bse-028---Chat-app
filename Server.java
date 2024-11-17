import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("===========  Welcome to Chat Application  ============");
        ServerSocket serverSocket = new ServerSocket(3333);
        System.out.println("Server started. Waiting for connections...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress());
            new Thread(() -> handleClient(socket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        Contact contact = new Contact();
        MessageManager messageManager = new MessageManager(socket);
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String clientName = in.readLine();  // Read client name
            System.out.println("---> Received connection request from: " + clientName);

            if (contact.isContactExists(clientName)) {
                out.println("===== Connection accepted from Server ======");
                do {
                    System.out.println("\n========== Main Menu ==========");
                    System.out.println("1. Start messaging");
                    System.out.println("2. Display the messages");
                    System.out.println("3. Search messages by ID");
                    System.out.println("4. Delete all the messages");
                    System.out.println("5. Exit");
                    System.out.println("================================");
                    System.out.print("Enter your choice: ");
                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            messageManager.startReading();
                            messageManager.startWriting();
                            out.println("Server wants to chat. Enter choice 1 to start messaging");

                            while (!socket.isClosed()) {
                                Thread.sleep(1000);
                            }
                            break;

                        case 2:
                            int choice2;
                            do {
                                System.out.println("-------------------------------");
                                System.out.println("1. Check the sent messages");
                                System.out.println("2. Check the received messages");
                                System.out.println("3. Exit");
                                System.out.println("-------------------------------");
                                choice2 = sc.nextInt();
                                sc.nextLine();

                                switch (choice2) {
                                    case 1:
                                        messageManager.displaySentMessages();
                                        break;
                                    case 2:
                                        messageManager.displayReceivedMessages();
                                        break;
                                    default:
                                        System.out.println("Exited");
                                }
                            } while (choice2 != 3);
                            break;

                        case 3:
                            System.out.print("Enter the message ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            messageManager.searchMessageById(id);
                            break;

                        case 4:
                            messageManager.deleteMessages();
                            break;

                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } while (choice != 5);
            } else {
                out.println("==== Connection rejected: Unauthorized client ID ===");
                System.out.println("---> Connection rejected for client ID: " + clientName);
                socket.close();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
