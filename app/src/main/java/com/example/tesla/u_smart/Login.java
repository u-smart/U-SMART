package com.example.tesla.u_smart;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity  {


    public EditText etUsername, etPassword;
    Button loginb;
   public static final String USERNAME ="";
   TeacherNav nav  ;
    TextInputLayout namelayout,passlayout;
    ListView lvk;
    TeacherNav tnav=  new TeacherNav();
    ImageView  headerimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        loginb =(Button)findViewById(R.id.bLogin);
       // headerimage=  (ImageView)findViewById(R.id.headerImageView);
        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    login();
            }

            public void login() {
                invokelogin(getName(), getPass());


            }
        });

    }
    public void invokelogin(final String username, final String password) {

        class LoginAsyc  extends AsyncTask<String ,Void ,String> {
            Dialog dialog;
            public static  final  String SUCCESS="success";
            public static  final  String tname="bagsh";
            public static  final  String columncode="bagshiin_code";
            public static  final  String columnpass="passwords";
            public static  final  String stuenttable="oyutan";
            public static  final  String studentcode="oyutanii_code";
            public static  final  String studentpass="passwords";
            public static  final  String columnimage="images";
            ArrayList<String> list =  new ArrayList<>();
            ArrayList<String> list1 =  new ArrayList<>();
           JSONArray jsonArray=null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               dialog = ProgressDialog.show(Login.this, "Түр  хүлээнэ  үү...", "Уншиж байна...");
            }

            @Override
            protected void onPostExecute(final String s) {
              dialog.dismiss();

                runOnUiThread(new Runnable() {
                    public void run() {
                        for (int k=0;k<list.size();k++){

                          if(list.get(k).toString().equalsIgnoreCase(getName()) || list.get(k).toString().equalsIgnoreCase(getPass())) {
                              if (isName(getName()) ) {
                                 //  Toast.makeText(getApplicationContext(),list1.get(i).toString(),Toast.LENGTH_SHORT).show();
                                  Intent n = new Intent(Login.this, TeacherNav.class);
                                   n.putExtra(USERNAME,getName());
                                  startActivity(n);
                                 
                              }
                              else
                              if (isStudent(getName()) ||  list.get(k).toString().equalsIgnoreCase(getPass())){
                                  Intent ns  = new Intent(Login.this,Oyutan_menu.class);
                                  startActivity(ns);                              }
                              else Toast.makeText(getApplicationContext(),"алдаатай байна",Toast.LENGTH_LONG).show();
                          }

                      }

                            }
                });
                 }

            @Override
            protected String doInBackground(String... params) {

             //      Toast.makeText(getApplicationContext(),"irlee",Toast.LENGTH_SHORT).show();
                String  username  = params[0];
                String  password = params[1];

                List<NameValuePair> lname =  new ArrayList<NameValuePair>();
                lname.add(new BasicNameValuePair("username",username));
                lname.add(new BasicNameValuePair("password",password));
                  JSONObject object=getmake(lname);
                JSONObject studentjson=getmake(lname);
                try{
                  int  s=object.getInt(SUCCESS);
                    if (s==1){
                       jsonArray= object.getJSONArray(tname);
                    for (int  i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String code = jsonObject.getString(columncode);
                        String pass  =jsonObject.getString(columnpass);
                        String image  = jsonObject.getString(columnimage);
                        list.add(code);
                        list.add(pass);
                        list1.add(image);
                        //"ip/images/"+imgPath;
                    }
                    }
                }catch (Exception r){}
                try{
                    int  s=studentjson.getInt(SUCCESS);
                    if (s==1){
                        jsonArray= object.getJSONArray(stuenttable);
                        for (int  i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String scode = jsonObject.getString(studentcode);
                            String spass  =jsonObject.getString(studentpass);
                            list.add(scode);
                            list.add(spass);
                        }
                    }
                }catch (Exception r){
                    Log.e("Login not  connect",r.getMessage());
                }

                return  null;

            }

        }

        LoginAsyc asyc =  new LoginAsyc();
        asyc.execute(username, password);
    }
    public  String  getName(){return etUsername.getText().toString();}
    public String getPass(){return etPassword.getText().toString();}
    public  boolean  isName(String name){
        Pattern    p = Pattern.compile("[A-Za-z]{1}.[A-Za-z]{2}[0-9]{2}");
        return  p.matcher(name).matches();
    }
    public  boolean  isStudent(String studentname ){
        Pattern  pl  =  Pattern.compile("[A-Za-z]{1}.[A-Za-z]{2}[0-9]{2}[A-Za-z]{1}[0-9]{3}");
        return   pl.matcher(studentname).matches();
    }



    public JSONObject getmake(List<NameValuePair>  lnames){

      String str="";
        JSONObject jsonObject = null;
        InputStream st=null;
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost post  =  new HttpPost("http://192.168.43.81/config/login.php");
            post.setEntity(new UrlEncodedFormEntity(lnames));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            st   =  entity.getContent();
        }catch (Exception e){

            Log.e(" Login not Connect", "not  connect");
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
            Log.e("Login Exception","Exception");
        }
        try{
            jsonObject = new JSONObject(str);
        }catch (Exception r){}
return  jsonObject;
    }

}

