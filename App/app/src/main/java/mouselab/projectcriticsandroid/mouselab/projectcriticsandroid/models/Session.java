package mouselab.projectcriticsandroid.mouselab.projectcriticsandroid.models;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.util.ArrayList;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import mouselab.projectcriticsandroid.constant.Global;

/**
 * Created by benjamin on 07.07.15.
 */
public class Session {

    public boolean login(String email, String password) {

        new Request().execute(email, password);
        return true;
    }

}
