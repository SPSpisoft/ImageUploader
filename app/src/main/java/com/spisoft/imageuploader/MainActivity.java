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

        imageUploader IV = findViewById(R.id.iv);
        imageUploader IV2 = findViewById(R.id.iv2);
        String MySever = "http://199.166.1.111/DgStore/api/";
        String storeCode = "CMASIH2";
        IV.url(MySever).requestCode(new imageUploader.RequestMap(1,"StoreLogo")).where("UID = '"+storeCode+"'");
        IV2.url(MySever).title("test0000").extension(".PNG")
                .requestCode(new imageUploader.RequestMap(2,"StoreBanner"))
                .where("UID = '"+storeCode+"'").titleTypeFace(TF_Tahoma);
    }
}
