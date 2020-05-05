package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private CurrentRestaurant currentRestaurant;
    private ReviewedFood reviewedFood;
    private Context context;
    private Menus menus;
    private ListView listView;
    private TextView textView;
    private String restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);
        currentRestaurant = CurrentRestaurant.getInstance();
        reviewedFood = ReviewedFood.getInstance();
        context = MenuActivity.this;
        restaurant = currentRestaurant.getRestaurant();
        menus = Menus.getInstance();
        textView.setText(restaurant+" menu:\n(Click on menu item to review it.)");

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(this, R.layout.list_view_layout1, menus.getRestaurantMenu(context, restaurant));
        listView.setAdapter(menuAdapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedFood = parent.getItemAtPosition(position).toString();
        if (selectedFood.matches("")) {
            return;
        }
        reviewedFood.setRestaurant(restaurant);
        reviewedFood.setFood(selectedFood);

        Intent intent = new Intent(MenuActivity.this, ReviewActivity.class);
        startActivity(intent);
    }
}
