package my.app.foodreviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private CurrentUser currentUser;
    private UserInformation userInformation;
    private String user;
    private String name = "";
    private String age = "";
    private String email = "";
    private TextView userInfo;
    private Context context;
    private EditText nameInput;
    private EditText ageInput;
    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = SettingsActivity.this;
        userInfo = (TextView) findViewById(R.id.userInfo);
        nameInput = (EditText) findViewById(R.id.editText2);
        ageInput = (EditText) findViewById(R.id.editText3);
        emailInput = (EditText) findViewById(R.id.editText4);
        currentUser = CurrentUser.getInstance();
        userInformation = UserInformation.getInstance();
        user = currentUser.getUser();
        userInformation.setUser(user);
        userInfo.setText("User: "+user+"\n"+userInformation.getUserInformation(context));
    }

    public void onChangeInfoButtonPressed(View v) {
        name = nameInput.getText().toString();
        age = ageInput.getText().toString();
        email = emailInput.getText().toString();
        userInformation.setUserInformation(context, name, age, email);
        userInfo.setText("User: "+user+"\n"+userInformation.getUserInformation(context));
    }
}
