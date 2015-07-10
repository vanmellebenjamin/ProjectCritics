package mouselab.projectcriticsandroid.mouselab.projectcriticsandroid.models;


import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.CookieStore;
import org.apache.http.protocol.HttpContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;


/**
 * Created by benjamin on 07.07.15.
 */
public abstract class Request extends AsyncTask<String, Void, String> {
    protected static CookieStore cookieStore = new BasicCookieStore();;
    protected static HttpContext httpContext =  new BasicHttpContext();;
    protected static HttpClient httpclient = new DefaultHttpClient();

}
