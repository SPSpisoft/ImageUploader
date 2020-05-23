package com.spisoft.ivuploader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class UploadSync extends AsyncTask<String, String, Boolean> {

    private final static String TAG = UploadSync.class.getSimpleName().toString();
    ProgressDialog mProgress;
    private Activity activity;
    private RestClient restClient;
    private String  zipPath=  Environment.getExternalStorageDirectory().toString() + "/instinctcoder/zip/";;

    public UploadSync(Activity activity, String url){
        this.activity = activity;
        restClient = new RestClient(url);
    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(activity);
        mProgress.setMessage("Uploading files...");
        mProgress.setCancelable(true);
        mProgress.setButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        mProgress.show();
    }


    @Override
    protected Boolean doInBackground(String... params) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName =dateFormat.format(new Date()) + ".zip";

            if (!FileHelper.zip(params[0], zipPath, fileName, false) ) {
                Toast.makeText(activity, "Failed Zip file", Toast.LENGTH_LONG).show();
                return false;
            }

            File zipFile = new File(zipPath + fileName);
            if (zipFile==null) {
                Toast.makeText(activity,"Please take photo first", Toast.LENGTH_LONG).show();
                return false;
            }

            TypedFile typedFile = new TypedFile("multipart/form-data", zipFile);
            Response response = restClient.getService(null,0,null).uploadSync(typedFile);

            if (response == null){
                Log.e(TAG, "success send server - failed");
                return  false;
            }
            if (response.getStatus()==200) {
                Log.e(TAG, "success send server - 200 status");
            } else {
                Log.e(TAG, "success send server - fail status - " + response.toString());
            }

        } catch (Exception e) {
            Log.e(TAG,e.getMessage().toString());
            return false;
        }

        return true;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mProgress.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mProgress.dismiss();
        if (result)
            Toast.makeText(activity,"Upload zip successfully", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(activity,"Upload zip failed", Toast.LENGTH_LONG).show();

    }
}