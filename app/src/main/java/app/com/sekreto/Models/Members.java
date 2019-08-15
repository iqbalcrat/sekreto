package app.com.sekreto.Models;

public class Members {

    private String id;
    private String userId;

    public Members(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public Members() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
