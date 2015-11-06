package com.example.tesla.u_smart;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ServerRequest extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void invokelogin(final String username, final String password, final String USER_NAME) {

        class LoginAsyc extends AsyncTask<String, Void, String> {
            Dialog dialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(ServerRequest.this, "Please wait...", "Loading...");
            }

            @Override
            protected void onPostExecute(String s) {
                String r = s.trim();
                dialog.dismiss();
                if (r.equalsIgnoreCase("success")) {
                    Intent n = new Intent(ServerRequest.this, Oyutan_menu.class);
                    n.putExtra(USER_NAME, username);
                    finish();
                    startActivity(n);
                    Toast.makeText(getApplicationContext(), "Амжилттай нэвтэрлээ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Таны нэр  эсвэл  нууц үг буруу байна", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                String username = params[0];
                String password = params[1];

                InputStream  st=null;
                List<NameValuePair> lname =  new ArrayList<NameValuePair>();
                lname.add(new BasicNameValuePair("username",username));
                lname.add(new BasicNameValuePair("password",password));
                String  res  ="";
                try{
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post  =  new HttpPost("http://127.0.0.1/login/login.php");
                    post.setEntity(new UrlEncodedFormEntity(lname));
                    HttpResponse response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    st   =  entity.getContent();
                }catch (Exception e){

                    Log.e(" not Connect", "not  connect");
                }
                try{
                    BufferedReader reader   =  new BufferedReader(new InputStreamReader(st,"UTF-8"),8);
                    StringBuilder sb =  new StringBuilder();
                    String line  = "";
                    while((line =reader.readLine())!=null){
                        sb.append(line + "\n");
                    }
                    st.close();
                    res = sb.toString();
                }catch (Exception e1) {
                    Log.e("Exception","Exception");
                }

                return  res;
            }
        }

        LoginAsyc asyc =  new LoginAsyc();
        asyc.execute(username, password);
    }
}