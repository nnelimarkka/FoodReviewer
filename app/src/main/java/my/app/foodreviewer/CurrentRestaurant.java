package my.app.foodreviewer;

public class CurrentRestaurant {

    private static CurrentRestaurant currentRestaurant = new CurrentRestaurant();
    private String restaurant ="";

    private CurrentRestaurant(){
    }

    public static CurrentRestaurant getInstance() {
        return(currentRestaurant);
    }

    public void setRestaurant(String s) {
        restaurant = s;
    }

    public String getRestaurant() {
        return(restaurant);
    }
}
