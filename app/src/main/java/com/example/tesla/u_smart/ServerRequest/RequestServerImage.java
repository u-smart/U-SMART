package com.example.tesla.u_smart.ServerRequest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tesla.u_smart.Login;
import com.example.tesla.u_smart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestServerImage extends AppCompatActivity {
    ImageView imageview;
    String url=null;
    String path=null;
    ServerRequestAll all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_teacher_nav);
        imageview  =(ImageView)findViewById(R.id.imageView2);
        all = new ServerRequestAll();
        Intent intent =getIntent();
       String  name =intent.getStringExtra(Login.USERNAME);
 //      ImageViewShow(name);
    }
    public  void  ImageViewShow(String name){
        class Hicheel extends AsyncTask<String, Void, String> {
            String h;
            ArrayList<String> arrayList1=  new ArrayList<String>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
            public static final String tname = "bagsh";
            public static final String lh = "images";
            JSONArray jsonArray=null;
            @Override
            protected String doInBackground(String... params) {
                String  name  =params[0];
                List<NameValuePair> list  = new ArrayList<NameValuePair>();
                try{
                    list.add(new BasicNameValuePair("name", name));
                    JSONObject jsonObject = all.getmake("http://192.168.43.81/config/image.php", list);
                    jsonArray = jsonObject.getJSONArray(tname);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject  j = jsonArray.getJSONObject(i);
                        String  hs= j.getString(lh);
                        arrayList1.add(hs);
                    }
                }catch (Exception e){Log.e("aldaa","garsan");}
                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                for (int k=0;k<arrayList1.size();k++){
                    path =arrayList1.get(k).toString();
                    url="http://192.168.43.81/images/"+path;
                   // Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).build();
                    ImageLoaderConfiguration config  =  new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options).build();
                    ImageLoader.getInstance().init(config);
                    try{

                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(url, imageview);
                        ImageSize targetSize = new ImageSize(80, 50); // result Bitmap will be fit to this size
                        Toast.makeText(getApplicationContext(),"image",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.e("зураг  ","алдаа");
                    }
                }
            }
        }
        Hicheel h = new Hicheel();
        h.execute(name);
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}

