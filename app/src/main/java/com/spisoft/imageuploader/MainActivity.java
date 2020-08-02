package com.spisoft.imageuploader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.spisoft.ivuploader.imageUploader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Typeface TF_Tahoma = Typeface.createFromAsset(MainActivity.this.getAssets(), "BTitr.ttf" + "");

        imageUploader IV0 = findViewById(R.id.iv0);
        imageUploader IV = findViewById(R.id.iv);
        imageUploader IV2 = findViewById(R.id.iv2);
        String MySever = "http://199.166.1.111/DgStore/api/";
//        String MySever = "http://79.127.104.70:1025/ChorderWS/api/";
        String storeCode = "CMASIH2";
        IV0.url(MySever).preview(MainActivity.this, "http://199.166.1.111/DgStore/_Files/Images/_Banner/ic_tools_logo.png")
                .requestCode(new imageUploader.RequestMap(1,"StoreLogo"), "UID = '"+storeCode+"'");
        IV.url(MySever).requestCode(new imageUploader.RequestMap(2,"StoreLogo"),"UID = '"+storeCode+"'");
//        IV.url(MySever).requestCode(null, null);
        IV2.url(MySever).tinyPng("NWw2YzxkWLxjnBxn1sGk4fC97Z990HGR").fileName("test0000").extension(".PNG").preview(MainActivity.this, "http://199.166.1.111/DgStore/_Files/Images/_Banner/IMG_20191017_201555_325.jpg")
                .requestCode(new imageUploader.RequestMap(2,"StoreBanner"), "UID = '"+storeCode+"'").titleTypeFace(TF_Tahoma)
                .setOnCallBack(new imageUploader.OnCallBack() {
                    @Override
                    public void onEvent(int status) {
                        Toast.makeText(MainActivity.this, "aaaaaaa "+status, Toast.LENGTH_LONG).show();
                    }
                });

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bb ="";
                Toast.makeText(MainActivity.this, bb, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
