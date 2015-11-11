package com.example.tesla.u_smart.ServerRequest;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Sk_m4_gg on 15.11.11.
 */
public class ServerRequestAll {
    public JSONObject getmake( String url,List<NameValuePair> lnames){

        String str="";
        JSONObject jsonObject=null;
        InputStream st=null;
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost post  =  new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(lnames));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            st   =  entity.getContent();
        }catch (Exception e){

            Log.e(" not Connect", "not  connect");
        }
        try{
            BufferedReader reader   =  new BufferedReader(new InputStreamReader(st,"UTF-8"),8);
            StringBuilder sb =  new StringBuilder();
            String line  ="";
            while((line =reader.readLine())!=null){
                sb.append(line + "\n");
            }
            st.close();
            str= sb.toString();

        }catch (Exception e1) {
            Log.e("Exception","Exception");
        }
        try{
            jsonObject = new JSONObject(str);
        }catch (Exception r){}
        return  jsonObject;
    }

}
