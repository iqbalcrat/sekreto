package app.com.sekreto.Models;

public class Question {


    private String id;
    private String question;
    private String name;
    private int profilePic;

    public Question(String id, String question, String name, int profilePic) {
        this.id = id;
        this.question = question;
        this.name = name;
        this.profilePic = profilePic;
    }

    public Question(String question, String name, int profilePic) {
        this.question = question;
        this.name = name;
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }
}
