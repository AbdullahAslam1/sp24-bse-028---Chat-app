import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("===========  Welcome to Chat Application  ============\n");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Contact name to Confirm the Connection: ");
        String name = sc.nextLine();

        try (Socket socket = new Socket("192.168.100.73", 3333);  // Ensure the correct IP address and port
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(name);  // Send the client name to the server
            String response = in.readLine();  // Read the server's response
            System.out.println("Response from server: " + response);

            if ("===== Connection accepted from Server ======".equals(response)) {
                System.out.println("===== You are connected. Let's Start the Chat ======");
                MessageManager messageManager = new MessageManager(socket);
                int choice;
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
                            out.println(name + " wants to chat. Enter choice 1 to start messaging");

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
                                        System.out.println("Invalid choice. Please try again.");
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
                            System.out.println("Exited");
                    }
                } while (choice != 5);
            } else {
                System.out.println("Connection failed or unauthorized client.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
