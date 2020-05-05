package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AdminSettingsActivity extends AppCompatActivity {

    private Reviews reviews;
    private Accounts accounts;
    private UserInformation userInformation;
    private Context context;
    private EditText userDelete;
    private EditText deleteReview;
    private EditText deleteReviewUser;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        context = AdminSettingsActivity.this;
        reviews = Reviews.getInstance();
        accounts = Accounts.getInstance();
        userInformation = UserInformation.getInstance();
        userDelete = (EditText) findViewById(R.id.editText5);
        deleteReview = (EditText) findViewById(R.id.editText6);
        deleteReviewUser = (EditText) findViewById(R.id.editText7);
    }

    public void deleteUser(View v) {
        username = userDelete.getText().toString();
        if (accounts.deleteAccount(context, username)) {
            Toast.makeText(context, "Account: " + username + " deleted.", Toast.LENGTH_SHORT).show();
            userInformation.deleteUserInfo(context, username);
        }
        else {
            Toast.makeText(context, "Given account not found.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteReviews(View v) {
        if (reviews.deleteReviews(context)) {
            Toast.makeText(context, "All reviews successfully deleted.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "No reviews found.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteReviewByTag(View v) {
        String tag = deleteReview.getText().toString();
        String user = deleteReviewUser.getText().toString();
        if (reviews.deleteReviewByTag(context, tag, user)) {
            Toast.makeText(context, "Review: "+tag+" deleted.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Given review not found.", Toast.LENGTH_SHORT).show();
        }
    }
}
