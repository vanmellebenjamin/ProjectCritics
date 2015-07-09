package mouselab.projectcriticsandroid.mouselab.projectcriticsandroid.models;

import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.protocol.HttpContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.client.protocol.ClientContext;

import mouselab.projectcriticsandroid.constant.Global;

/**
 * Created by benjamin on 07.07.15.
 */
public class Request extends AsyncTask<String, Void, String> {
    static CookieStore cookieStore = new BasicCookieStore();;
    static HttpContext httpContext =  new BasicHttpContext();;
    HttpClient httpclient;


    public Request() {
        super();
        httpclient = new DefaultHttpClient();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    @Override
    protected String doInBackground(String... params) {
       return postData(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("info: " + result);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("net", result);
        return result;
    }

}
