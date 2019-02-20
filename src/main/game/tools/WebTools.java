package main.game.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
// Link http://hc.apache.org/
public class WebTools {

    public WebTools() {
    }

    public JSONObject sendPostRequest(String endOfURL) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(Consts.URI.concat(endOfURL));
        //Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            return new JSONObject(EntityUtils.toString(response.getEntity()));
        }
        return null;
    }

    public Object sendGetRequest(String endOfURL) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(Consts.URI.concat(endOfURL));
        //Execute and get the response.
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String entityString = EntityUtils.toString(entity);
            if (entityString.startsWith("[")) {
                return new JSONArray(entityString);
            }
            return new JSONObject(entityString);

        }
        return null;
    }
}
