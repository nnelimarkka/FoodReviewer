package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    private ReviewedFood reviewedFood;
    private CurrentUser currentUser;
    private UserReviews userReviews;
    private Reviews reviews;
    private SeekBar tasteBar;
    private SeekBar tempBar;
    private TextView tasteView;
    private TextView tempView;
    private EditText feedback;
    private ListView reviewList; /* muista notifyDataSetChanged */
    private Context context;

    private String user;
    private String food;
    private String restaurant;
    private ArrayList<String> reviewListContent;
    private ArrayAdapter<String> reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        reviewList = (ListView) findViewById(R.id.reviewList);
        tasteView = (TextView) findViewById(R.id.foodTasteView);
        tempView = (TextView) findViewById(R.id.foodTempView);
        feedback = (EditText) findViewById(R.id.feedback);
        tasteBar = (SeekBar) findViewById(R.id.seekBar1);
        tempBar = (SeekBar) findViewById(R.id.seekBar2);
        context = ReviewActivity.this;

        userReviews = UserReviews.getInstance();
        reviews = Reviews.getInstance();
        reviewedFood = ReviewedFood.getInstance();
        currentUser = CurrentUser.getInstance();
        food = reviewedFood.getFood();
        restaurant = reviewedFood.getRestaurant();
        user = currentUser.getUser();


        tasteBar.setMax(5);
        tempBar.setMax(5);

        ArrayList<String> userReview = userReviews.getReview(context, user, restaurant, food);
        if (userReview.size() == 3) {
            updateUserReview(userReview);
        }

        tasteView.setText("Food was tasty (0-5): "+tasteBar.getProgress());
        tempView.setText("Temperature of food (0-5): "+tempBar.getProgress());

        tasteBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tasteView.setText("Food was tasty (0-5): "+tasteBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tempBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempView.setText("Temperature of food (0-5): "+tempBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        reviewListContent = new ArrayList<>();
        reviewListContent = reviews.getReviews(context, restaurant + food);

        reviewAdapter = new ArrayAdapter<String>(this, R.layout.list_view_layout1, reviewListContent);
        reviewList.setAdapter(reviewAdapter);
    }

    /* Used to update the review activity if the user has saved a review */
    public void updateUserReview(ArrayList<String> review) {
        tasteBar.setProgress(Integer.valueOf(review.get(0)));
        tempBar.setProgress(Integer.valueOf(review.get(1)));
        feedback.setText(review.get(2));
    }

    public void onSaveButtonClicked(View v) {
        ArrayList<String> review = new ArrayList<>();
        review.add(String.valueOf(tasteBar.getProgress()));
        review.add(String.valueOf(tempBar.getProgress()));
        review.add(feedback.getText().toString());
        userReviews.saveReview(context, review, user, restaurant, food);
    }

    public void onSendButtonClicked(View v) {
        String review;
        String tag;
        review = "Reviewer: " + user + "\n" + restaurant + "\n" + food + "\nFood taste: " + tasteBar.getProgress() + "\nFood temperature: " + tempBar.getProgress() + "\nFeedback: " + feedback.getText().toString();
        tag = restaurant + food;
        reviews.saveReview(context, review, tag);
        userReviews.deleteSavedReview(context, user + restaurant + food.replaceAll("\\s+", "") + ".csv");
        reviewListContent.add(review);
        reviewAdapter.notifyDataSetChanged();
    }
}
