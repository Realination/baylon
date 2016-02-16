/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baylon.app;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
 
/**
 *
 * @author DarkMatter
 */
public class HttpRequest {
     
    private final String baseUrl = Settings.api;
    Constants constants;
    
    
    public String ServiceGet(String url) throws URISyntaxException, HttpException {
        constants = Constants.getInstance();
        String respon = null;
        HttpGet httpGet = new HttpGet(baseUrl+url);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            httpGet.setHeader(new BasicHeader("Prama", "no-cache"));
            httpGet.setHeader(new BasicHeader("Cache-Control", "no-cache"));
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            respon = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            httpGet.abort();
        }
        return respon;
    }
    
    public String ServicePost(String url, ArrayList nameValuePairs) throws URISyntaxException, HttpException {
        String respon = null;
        constants = Constants.getInstance();
        System.out.println(baseUrl);
        HttpPost httppost = null;
    	try {
            httppost = new HttpPost(baseUrl+url);
            HttpClient httpclient = new DefaultHttpClient();
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse res = httpclient.execute(httppost);
            try {
                HttpEntity entity = res.getEntity();
                respon = EntityUtils.toString(entity);
                entity.consumeContent();
                constants.addValue("netStat","Online");

            }catch (Exception e){}
            System.out.println(respon);
            return respon;
        }catch(IOException e) {
            httppost.abort();
            System.out.println("Aborted");
            constants.addValue("netStat","Offline");
	         return "error";
        }

    }
   
  
   
    
}
