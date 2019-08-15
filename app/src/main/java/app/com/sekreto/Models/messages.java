package app.com.sekreto.Models;

public class messages {

    private String id;
    private String message;
    private String author;
    private String timeStamp;

    public messages(String id, String message, String author, String timeStamp) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.timeStamp = timeStamp;
    }

    public messages() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
