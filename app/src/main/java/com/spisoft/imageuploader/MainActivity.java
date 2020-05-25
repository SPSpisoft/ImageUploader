package com.spisoft.imageuploader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;

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
        String storeCode = "CMASIH2";
        IV0.url(MySever).preview(MainActivity.this, "http://199.166.1.111/DgStore/_Files/Images/_Banner/ic_tools_logo.png")
                .requestCode(new imageUploader.RequestMap(1,"StoreLogo")).where("UID = '"+storeCode+"'");
        IV.url(MySever).requestCode(new imageUploader.RequestMap(2,"StoreLogo")).where("UID = '"+storeCode+"'");
        IV2.url(MySever).title("test0000").extension(".PNG").preview(MainActivity.this, "http://199.166.1.111/DgStore/_Files/Images/_Banner/IMG_20191017_201555_325.jpg")
                .requestCode(new imageUploader.RequestMap(2,"StoreBanner"))
                .where("UID = '"+storeCode+"'").titleTypeFace(TF_Tahoma);
    }
}
