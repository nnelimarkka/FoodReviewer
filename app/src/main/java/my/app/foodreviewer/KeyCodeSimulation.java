package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class KeyCodeSimulation extends AppCompatActivity {

    private String keycode;
    private TextView textView;
    private EditText editText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_code_simulation);
        context = KeyCodeSimulation.this;
        textView = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText);
        textView.setText("Input keycode to continue:\n"+generateKeyCode());
    }

    /* generates random 6-digit keycode */
    public String generateKeyCode() {
        int max = 10;
        int number;
        keycode = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            number = random.nextInt(max);
            keycode = keycode + number;
        }
        return(keycode);
    }

    /* Checks if keycode user inputs is correct */
    public void onOkButtonPressed(View v) {
        String userInput;
        userInput = editText.getText().toString();
        if (userInput.matches(keycode)) {
            loadActivity();
        }
        else {
            Toast toast = Toast.makeText(context, "Incorrect keycode, please try again.", Toast.LENGTH_SHORT);
            toast.show();
            textView.setText("Input keycode to continue:\n"+generateKeyCode());
            editText.setText("");
        }
    }

    /* Loads the MainView activity */
    public void loadActivity() {
        Intent intent = new Intent(KeyCodeSimulation.this, MainView.class);
        startActivity(intent);
    }


}
