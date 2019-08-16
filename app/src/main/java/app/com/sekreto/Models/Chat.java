package app.com.sekreto.Models;

public class Chat {


    private String id;
    private String title;
    private String admin;

    public Chat(String id, String title, String admin) {
        this.id = id;
        this.title = title;
        this.admin = admin;
    }

    public Chat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", admin='" + admin + '\'' +
                '}';
    }
}
