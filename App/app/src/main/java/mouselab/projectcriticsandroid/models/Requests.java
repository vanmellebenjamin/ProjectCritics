package mouselab.projectcriticsandroid.models;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import mouselab.projectcriticsandroid.activities.Login;
import mouselab.projectcriticsandroid.constant.Global;
import mouselab.projectcriticsandroid.models.Request;

/**
 * Created by benjamin on 10.07.15.
 */
public class Requests {

    /**
     * Login
     */
    public class LoginRequest extends Request {
        private Login activity;

        public LoginRequest (Login activity) {
            super();
            httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            return postData(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.split(",")[0].equals("200")) {
                // create the new intent
                activity.loginSuccess();
            } else {
                activity.loginFailure();
            }
        }

        public String postData(String email, String password) {
            // Create a new HttpClient and Post Header

            HttpPost httppost = new HttpPost(Global.SERVER_ADDRESS + "/login");
            String result = "failure";
            try {
                // Add your data
                ArrayList<NameValuePair> postParameters;
                postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("email", email));
                postParameters.add(new BasicNameValuePair("password", password));
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost, httpContext);
                result = "" + response.getStatusLine().getStatusCode()+",";
                result += EntityUtils.toString(response.getEntity(), "UTF-8");

                // Manage autologin
                Context context = activity.getApplicationContext();
                SharedPreferences pref = context.getSharedPreferences("CRITIC_PREFS", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                //If http success, save the logins in the shared preferences
                if (response.getStatusLine().getStatusCode() == 200) {
                    editor.putString("userEmail", email);
                    editor.putString("userPassword", password);
                    editor.commit();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}
