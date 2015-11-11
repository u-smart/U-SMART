package com.example.tesla.u_smart.ServerRequest;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tesla.u_smart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.tesla.u_smart.ServerRequest.ServerRequestAll;
/**
 * Created by Sk_m4_gg on 15.11.11.
 */
public class RequestServerImage extends AppCompatActivity {
    ImageView imageview;
    String url=null;
    String path=null;
    ServerRequestAll all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_teacher_nav);
        imageview  =(ImageView)findViewById(R.id.headerImageView);
        all = new ServerRequestAll();


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
                    JSONObject jsonObject = all.getmake("http://10.2.201.4/config/image.php", list);
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
                    url="http://10.2.201.4/images/"+path;
                    Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
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
