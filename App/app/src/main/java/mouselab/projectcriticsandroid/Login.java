package mouselab.projectcriticsandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import mouselab.projectcriticsandroid.mouselab.projectcriticsandroid.models.Session;


public class Login extends ActionBarActivity implements View.OnClickListener {

    private Button Login, Subscribe;
    private EditText Email, Password;
    private Session UserSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup the login view elements
        Email = (EditText) findViewById(R.id.login_email);
        Password = (EditText) findViewById(R.id.login_password);
        Login = (Button) findViewById(R.id.login_validate_button);
        Subscribe = (Button) findViewById(R.id.login_subscribe_button);
        UserSession = new Session();
        // Add the listeners on login and subscribe buttons
        Login.setOnClickListener(this);
        Subscribe.setOnClickListener(this);

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
           try {
               if(UserSession.login(email, password)) {
                   // logged
                   Context context = getApplicationContext();
                   CharSequence text = "Hello toast! logged on";
                   int duration = Toast.LENGTH_SHORT;

                   Toast toast = Toast.makeText(context, text, duration);
                   toast.show();
               } else {
                   // not logged
                   Context context = getApplicationContext();
                   CharSequence text = "Hello toast! not logged on";
                   int duration = Toast.LENGTH_SHORT;

                   Toast toast = Toast.makeText(context, text, duration);
                   toast.show();
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
}
