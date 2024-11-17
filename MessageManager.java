import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;
    private int messageIdCounter = 1;
    List<Message> sendMessages = new ArrayList<>();
    List<Message> receiveMessages = new ArrayList<>();

    public MessageManager(Socket socket) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        Runnable readerTask = () -> {
            System.out.println("Reader started...");
            try {
                String message;
                while ((message = br.readLine()) != null) {
                    Message receivedMessage = new Message(messageIdCounter++, message);
                    receiveMessages.add(receivedMessage);
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Server terminated the chat.");
                        socket.close();
                        break;
                    }

                    System.out.println("Enter message: "+receivedMessage.getContent()+"   -->MessageID: "+messageIdCounter);
                }
            } catch (IOException e) {
                System.out.println("Reader error: " + e.getMessage());
            }
        };
        new Thread(readerTask).start();
    }

    public void startWriting() {
        Runnable writerTask = () -> {
            System.out.println("Writer started...");
            try {
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = userInput.readLine()) != null) {
                    Message sentMessage = new Message(messageIdCounter++, message);
                    sendMessages.add(sentMessage);
                    out.println(sentMessage.getContent());
                    System.out.println("Enter message: "+sentMessage.getContent()+"   -->Message Id: "+messageIdCounter);
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Client terminated the chat.");
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Writer error: " + e.getMessage());
            }
        };
        new Thread(writerTask).start();
    }

    public void searchMessageById(int id) {
        boolean found = false;
        for (Message message : sendMessages) {
            if (message.getId() == id) {
                System.out.println("-------------------------------");
                System.out.println(message);
                System.out.println("-------------------------------");
                found = true;
            }
        }
        for (Message message : receiveMessages) {
            if (message.getId() == id) {
                System.out.println("-------------------------------");
                System.out.println(message);
                System.out.println("-------------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("--------- Message not found --------------");
        }

    }

    public void displaySentMessages() {
        System.out.println("\n===== Sent Messages =====");
        for (Message message : sendMessages) {
            System.out.println(message);
        }
        if (sendMessages.isEmpty()) {
            System.out.println("----- Sent message list is empty -------");
        }
    }

    public void displayReceivedMessages() {
        System.out.println("\n==== Received Messages =====");
        for (Message message : receiveMessages) {
            System.out.println(message);
        }
        if (receiveMessages.isEmpty()) {
            System.out.println("---- Received message list is empty -----");
        }

    }

    public void deleteMessages(){
        if (sendMessages.isEmpty() && receiveMessages.isEmpty()) {
            System.out.println("-------- Both send and receive message lists are already empty ---------");
        }
        else {
            sendMessages.clear();
            receiveMessages.clear();
            System.out.println("==== All the messages are Deleted ====");
        }
    }

}
