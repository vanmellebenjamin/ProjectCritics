package mouselab.projectcriticsandroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mouselab.projectcriticsandroid.R;
import mouselab.projectcriticsandroid.models.Requests;


public class Login extends Activity implements View.OnClickListener {
    private Button Login, Subscribe;
    private EditText Email, Password;
    private TextView Error;
    private Requests Req;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Req = new Requests();

        // Setup the login view elements
        Email = (EditText) findViewById(R.id.login_email);
        Password = (EditText) findViewById(R.id.login_password);
        Login = (Button) findViewById(R.id.login_validate_button);
        Subscribe = (Button) findViewById(R.id.login_subscribe_button);
        Error = (TextView) findViewById(R.id.login_error);
        Error.setVisibility(View.GONE);
        // Add the listeners on login and subscribe buttons
        Login.setOnClickListener(this);
        Subscribe.setOnClickListener(this);

        // check if the user's credentials have been stored
        Context context = this.getApplicationContext();
        SharedPreferences pref = context.getSharedPreferences("CRITIC_PREFS", 0);
        String email = pref.getString("userEmail", "EMPTY");
        String password = pref.getString("userPassword", "EMPTY");

        if (! email.equals("EMPTY")
                && ! password.equals("EMPTY")) {
            Email.setText(email);
            Password.setText(password);
            tryLoggin(email, password);
        }
    }

   @Override
   public void onClick(final View v) {
       if (Subscribe.getId() == ((Button) v).getId()) {
           //Start subscribe activity
       } else if (Login.getId() == ((Button) v).getId()) {
           String email = Email.getText().toString();
           String password = Password.getText().toString();
           tryLoggin(email, password);
       }
   }


    public void tryLoggin(String email, String password) {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Connection with the server...");
        progress.show();
        (Req.new LoginRequest(this)).execute(email, password);
    }

    public void loginSuccess() {
        progress.dismiss();
        Error.setVisibility(View.GONE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loginFailure() {
        progress.dismiss();
        Error.setVisibility(View.VISIBLE);
    }
}
