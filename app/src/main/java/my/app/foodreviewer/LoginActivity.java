package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Context context;
    private Accounts accounts;
    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = LoginActivity.this;
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        accounts = Accounts.getInstance();
        currentUser = CurrentUser.getInstance();
    }

    /* Method used to send account credentials to create new account when create account button is pressed */
    public void createAccount(View v) {
        String user, pword;
        Toast toast = null;
        int result;
        user = username.getText().toString();
        pword = password.getText().toString();
        result = accounts.createNewAccount(context, user, pword);

        switch (result) {
            case -3:
                toast = toast.makeText(context, "Account with given name already created, pleasy try different username.", Toast.LENGTH_LONG);
                toast.show();
                break;
            case -2:
                toast = toast.makeText(context, "Can't create account named 'Admin', please try again.", Toast.LENGTH_LONG);
                toast.show();
                break;
            case -1:
                toast = toast.makeText(context, "Password needs to be at least 12 characters long and have at least one capital letter and 1 number.", Toast.LENGTH_LONG);
                toast.show();
                break;
            case 0:
                toast = toast.makeText(context, "Username or password is empty.", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 1:
                toast = toast.makeText(context, "Account created, login to proceed.", Toast.LENGTH_SHORT);
                toast.show();
                break;
            default:
                break;
        }
    }

    /* Logs into existing account */
    public void login(View v) {
        String user, pword;
        Toast toast = null;
        int result;
        user = username.getText().toString();
        pword = password.getText().toString();
        result = accounts.loginToAccount(context, user, pword);

        switch (result) {
            case -2:
                toast = Toast.makeText(context, "Unknown account.", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case -1:
                toast = Toast.makeText(context, "No accounts found, create a new account to login.", Toast.LENGTH_LONG);
                toast.show();
                break;
            case 0:
                toast = Toast.makeText(context, "Incorrect password.", Toast.LENGTH_SHORT);
                toast.show();
                password.setText("");
                break;
            case 1:
                toast = Toast.makeText(context, "Login successful.", Toast.LENGTH_SHORT);
                toast.show();
                loadActivity(user);
                break;
            case 2:
                toast = Toast.makeText(context, "Admin login successful.", Toast.LENGTH_SHORT);
                toast.show();
                loadActivity(user);
                break;
            default:
                break;
        }
    }

    /* loads KeyCodeSimulation activity */
    public void loadActivity(String username) {
        currentUser.setUser(username);
        Intent intent = new Intent(LoginActivity.this, KeyCodeSimulation.class);
        startActivity(intent);
    }
}
