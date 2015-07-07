package mouselab.projectcriticsandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


public class Login extends ActionBarActivity {

    private Button Login, Subscribe;
    private EditText Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup the login view elements
        Email = (EditText) findViewById(R.id.login_email);
        Password = (EditText) findViewById(R.id.login_email);
        Login = (Button) findViewById(R.id.login_validate_button);
        Subscribe = (Button) findViewById(R.id.login_subscribe_button);

        // Add the listeners on login and subscribe buttons

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

    OnClickListener buttonListener = new View.OnClickListener() {
        boolean clicked = false;
        int numClicks = 0;

        @Override
        public void onClick(View v) {
            if(numClicks > 5) {
                button.setText("STOP IT");
            }
            numClicks++;
            if(clicked == false){
                clicked = true;
                tv2.setText("Text Changed on Button Click");
            }
            else
            {
                clicked = false;
                tv2.setText("Click again");
            }
        }
    };
}
