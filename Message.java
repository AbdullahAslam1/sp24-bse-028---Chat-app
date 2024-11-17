import java.time.LocalDate;

public class Message {
    private int id;
    private String content;
    LocalDate localDate;
    Status status;

    // Constructor
    public Message(int id, String content) {
        this.id = id;
        this.content = content;
        this.localDate = LocalDate.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Message: " + content + " |   Date: " + localDate;
    }

}
