package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Context context;
    private Accounts accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        accounts = Accounts.getInstance(context);
    }

    /* Method used to send account credentials to create new account when create account button is pressed*/
    public void createAccount(View v) {
        String user, pword;
        Toast toast = null;
        int result;
        user = username.getText().toString();
        pword = password.getText().toString();
        result = accounts.createNewAccount(user, pword);
        switch (result) {
            case -1:
                toast = toast.makeText(context, "Password needs to be 12 characters long.", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 0:
                toast = toast.makeText(context, "Username or password is empty", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 1:
                toast = toast.makeText(context, "Account created.", Toast.LENGTH_SHORT);
                toast.show();
                break;
            default:
                break;
        }
    }

    /* Method used for checking for existing account*/
    public void login(View v) {
        //TODO existing account login
    }
}
