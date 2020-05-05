package my.app.foodreviewer;

/* this class is used to transfer the username from login-activity to other activities */
public class CurrentUser {
    private String user = "";
    private static CurrentUser currentUser = new CurrentUser();

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        return(currentUser);
    }

    public void setUser(String username) {
        user = username;
    }

    public String getUser() {
        return(user);
    }
}
