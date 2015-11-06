package com.example.tesla.u_smart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Animation scale ;
    ImageView launcher_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread(){
                public void run() {

                try
                {

                    sleep(3000);
                }catch (InterruptedException e){
                e.printStackTrace();

                }
                    finally {

                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity (intent);
                }
                }
        };


        scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        launcher_logo = (ImageView)findViewById(R.id.launcher_logo);
        launcher_logo.startAnimation(scale);
        t.start();

    }


}
