package my.app.foodreviewer;

public class ReviewedFood {

    private static ReviewedFood reviewedFood = new ReviewedFood();
    private String restaurant;
    private String food;

    private ReviewedFood() {
    }

    public static ReviewedFood getInstance() {
        return(reviewedFood);
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
