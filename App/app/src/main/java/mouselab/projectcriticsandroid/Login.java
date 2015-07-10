package mouselab.projectcriticsandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mouselab.projectcriticsandroid.mouselab.projectcriticsandroid.models.Requests;


public class Login extends ActionBarActivity implements View.OnClickListener {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
