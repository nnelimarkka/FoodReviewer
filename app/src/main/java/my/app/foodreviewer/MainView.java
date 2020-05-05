package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainView extends AppCompatActivity {

    private CurrentUser currentUser;
    private CurrentRestaurant currentRestaurant;
    private Button restaurant1;
    private Button restaurant2;
    private Button restaurant3;

    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        currentUser = CurrentUser.getInstance();
        user = currentUser.getUser();
        currentRestaurant = CurrentRestaurant.getInstance();
        restaurant1 = (Button) findViewById(R.id.Restaurant1);
        restaurant2 = (Button) findViewById(R.id.Restaurant2);
        restaurant3 = (Button) findViewById(R.id.Restaurant3);

        /* Onclicklisteners are used to start menu-activity, and define which restaurants menu to load via currentRestaurant class */
        restaurant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRestaurant.setRestaurant("Restaurant1");
                Intent intent = new Intent(MainView.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        restaurant2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRestaurant.setRestaurant("Restaurant2");
                Intent intent = new Intent(MainView.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        restaurant3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRestaurant.setRestaurant("Restaurant3");
                Intent intent = new Intent(MainView.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        if (user.matches("Admin")) {
            Fragment fragment = new adminFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.adminFrame, fragment);
            transaction.commit();
        }
        }

    public void loadSettingsActivity(View v) {
        Intent intent = new Intent(MainView.this, SettingsActivity.class);
        startActivity(intent);
    }

    /* Used so that pressing back button hops over the keyCodeSimulation-activity */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainView.this, LoginActivity.class);
        startActivity(intent);
    }

    /* Method used to load admin settings activity */
    public void loadAdminSettings(View v) {
        Intent intent = new Intent(MainView.this, AdminSettingsActivity.class);
        startActivity(intent);
    }

}
