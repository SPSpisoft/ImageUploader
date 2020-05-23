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
        IV.url(MySever).requestCode(1);
        IV2.url(MySever).title("test0000").extension(".PNG").requestCode(2).titleTypeFace(TF_Tahoma);
    }
}
