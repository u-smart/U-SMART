package com.example.tesla.u_smart;

import android.app.Fragment;
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
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesla.u_smart.ServerRequest.ServerRequestAll;
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

public class TeacherNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener{
    String names;
    ListView lv,lv1;
    ImageView headerImage;
    ServerRequestAll  all;
    String name=null;
    TextView text1;

    Bitmap bitmap;
    ArrayList<HashMap<String, String>> productsList=new ArrayList<HashMap<String, String>>();
    JSONArray  array=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_nav);
     //   setContentView(R.layout.nav_header_teacher_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
      lv  =  (ListView)findViewById(R.id.lessonlistView);
        lv1  =  (ListView)findViewById(R.id.lessonlistView);
       //// headerImage  =(ImageView)findViewById(R.id.imageVieView inf  =  navigationView.inflateHeaderView(R.layout.nav_header_teacher_nav);
       View   inf  =  navigationView.inflateHeaderView(R.layout.nav_header_teacher_nav);
        headerImage  =(ImageView)inf.findViewById(R.id.imageView2);
        text1  =(TextView)inf.findViewById(R.id.email);
      text1.setText("email");
        lv.setOnItemClickListener(this);

      //  new Drawer().withHeader(R.drawable.dun).build();
       // headerImage = (ImageView)findViewById(R.id.headerImageView);
 //        navigationView.inflateHeaderView(getResources().getDrawable(R.drawable.dun),null);

        Intent intent =getIntent();
        name =intent.getStringExtra(Login.USERNAME);
        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();

        all =  new ServerRequestAll();
        ImageViewShow(name);

     }
    private boolean pressTwice = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(pressTwice == true){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            System.exit(0);
        }

        pressTwice = true;
        Toast.makeText(this, "Гархыг хүсвэл дахин дарна уу", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pressTwice = false;
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment=null;
        int id = item.getItemId();

        if (id == R.id.syllabus) {
          getHicheel(name);

        } else if (id == R.id.res) {
              getData(name);

        } else if (id == R.id.irts) {

        } else if (id == R.id.home) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void  getData(String username){
        class  Server extends AsyncTask<String,Void,String> {
            public static final String tname = "hicheel";
            public static final String lnames = "hicheeliin_ner";
            public static final String lcode= "hicheeliin_code";
            public static final String lcount= "n";
            JSONArray jsonArray=null;
            @Override
            protected String doInBackground(String... params) {
                String  name  =params[0];
                List<NameValuePair>  list  = new ArrayList<NameValuePair>();
                try{
                    list.add(new BasicNameValuePair("name", name));
                    JSONObject  jsonObject = all.getmake("http://192.168.0.105/config/hicheel.php",list);
                    jsonArray = jsonObject.getJSONArray(tname);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject  j = jsonArray.getJSONObject(i);
                        names= j.getString(lnames);
                        String h  = j.getString(lcode);
                        String  count  =j.getString(lcount);
                        HashMap<String,String>  map  = new  HashMap<String,String>();
                        map.put(lnames, names);
                        map.put(lcode, h);
                        map.put(lcount,count);
                        productsList.add(map);
                    }
                }catch (Exception e){Log.e("aldaa","garsan");}
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                ListAdapter listAdapter = new SimpleAdapter(TeacherNav.this,productsList,R.layout.list_item,new String[]{lcode,lnames,lcount},new int[]{R.id.lcode,R.id.lname,R.id.lcount});
              lv.setAdapter(listAdapter);
            }
        }
   Server  s  =  new Server();
        s.execute(username);
    }


    private void getHicheel(String  username ){
     class Hicheel extends AsyncTask<String, Void, String> {
            ArrayList<String>  arrayList1=  new ArrayList<String>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
            public static final String tname = "hicheeliin_huwaari";
            public static final String lh = "hicheeliin_oroh_helber";
            public static final String group = "hicheeliin_huwaariin_code";
            public static final String lnames = "hicheeliin_ner";
            public static final String date= "create_date";
            JSONArray jsonArray=null;
            @Override
            protected String doInBackground(String... params) {
                  String  name  =params[0];
                List<NameValuePair>  list  = new ArrayList<NameValuePair>();
             try{
                 list.add(new BasicNameValuePair("name", name));
                 JSONObject  jsonObject = all.getmake("http://192.168.0.105/config/huvaari.php", list);
                 jsonArray = jsonObject.getJSONArray(tname);
                 for (int i=0;i<jsonArray.length();i++){
                     JSONObject  j = jsonArray.getJSONObject(i);
                   String  n= j.getString(lnames);
                    String h  = j.getString(lh);
                     String gr  = j.getString(group);
                     String d  = j.getString(date);
                     HashMap<String,String>  map  = new  HashMap<String,String>();
                     map.put(lnames, n);
                     map.put(lh, h);
                     map.put(group, gr);
                     map.put(date, d);
                     arrayList.add(map);
                 }
             }catch (Exception e){Log.e("aldaa","garsan");}
                return null;
            }

         @Override
         protected void onPostExecute(String s) {
              //   ArrayAdapter adapter =  new ArrayAdapter(TeacherNav.this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList1);
              //   lv.setAdapter(adapter);
             ListAdapter listAdapter =  new SimpleAdapter(TeacherNav.this,arrayList,R.layout.hicheel_layout,new String[]{lnames,lh,group,date}, new int[]{R.id.lessonname,R.id.helber,R.id.group,R.id.date});
             lv1.setAdapter(listAdapter);

         }
         //       Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

     }

        Hicheel h = new Hicheel();
        h.execute(username);
    }
public  void  ImageViewShow(String name){
    class Hicheel extends AsyncTask<String, Void, String> {
        String h;
        ArrayList<String>  arrayList1=  new ArrayList<String>();
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        public static final String tname = "bagsh";
        public static final String lh = "images";
        JSONArray jsonArray=null;
        @Override
        protected String doInBackground(String... params) {
            String  name  =params[0];
            List<NameValuePair>  list  = new ArrayList<NameValuePair>();
            try{
                list.add(new BasicNameValuePair("name", name));
                JSONObject  jsonObject = all.getmake("http://192.168.0.105/config/image.php", list);
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
            String path=null;
            String  url=null;
            for (int k=0;k<arrayList1.size();k++){
                path =arrayList1.get(k).toString();
                url="http://192.168.0.105/images/"+path;
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).build();
                ImageLoaderConfiguration config  =  new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options).build();
                ImageLoader.getInstance().init(config);
                try{

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(url, headerImage);
                    ImageSize targetSize = new ImageSize(100, 50); // result Bitmap will be fit to this size
                   // Toast.makeText(getApplicationContext(),"image",Toast.LENGTH_SHORT).show();
    imageLoader.loadImage(url,targetSize,options,new SimpleImageLoadingListener(){
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                 headerImage.setImageBitmap(getCircleBitmap(loadedImage));
        }
    });
                }catch (Exception e){
                    Log.e("зураг  ",e.getMessage());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//          int  k  =  view.getId();
        LinearLayout lp = (LinearLayout)view;
        LinearLayout  lchild   =(LinearLayout)lp.getChildAt(1);
        TextView  t  =  (TextView) lchild.getChildAt(0);
        Toast.makeText(getApplicationContext(),t.getText().toString(),Toast.LENGTH_SHORT).show();
    }
    private void getGroup(String  username ){
        class Hicheel extends AsyncTask<String, Void, String> {
          //  ArrayList<String>  arrayList1=  new ArrayList<String>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
            public static final String tname = "hicheeliin_huwaari";
            public static final String lh = "hicheeliin_oroh_helber";
            public static final String group = "hicheeliin_huwaariin_code";

            JSONArray jsonArray=null;
            @Override
            protected String doInBackground(String... params) {
                String  name  =params[0];
                List<NameValuePair>  list  = new ArrayList<NameValuePair>();
                try{
                    list.add(new BasicNameValuePair("name", name));
                    JSONObject  jsonObject = all.getmake("http://192.168.0.105/config/group.php", list);
                    jsonArray = jsonObject.getJSONArray(tname);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject  j = jsonArray.getJSONObject(i);
                        String gr  = j.getString(group);
                        String h  = j.getString(lh);


                        HashMap<String,String>  map  = new  HashMap<String,String>();
                        map.put(group, gr);
                        map.put(lh, h);


                        arrayList.add(map);
                    }
                }catch (Exception e)
                {Log.e("group json aldaa",e.getMessage());}
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                //   ArrayAdapter adapter =  new ArrayAdapter(TeacherNav.this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList1);
                //   lv.setAdapter(adapter);
                ListAdapter listAdapter =  new SimpleAdapter(TeacherNav.this,arrayList,R.layout.group_list,new String[]{lh,group}, new int[]{R.id.groupname,R.id.group_helber});
                lv1.setAdapter(listAdapter);

            }
            //       Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

        }

        Hicheel h = new Hicheel();
        h.execute(username);
    }
    public void  getDate(){

    }
}
