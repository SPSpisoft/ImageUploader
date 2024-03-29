package com.spisoft.ivuploader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tinify.Tinify;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class imageUploader extends RelativeLayout {
    private View rootView;
    private CardView vCardBorder;
    private ProgressBar vProgress;
    private ImageView vImageView, vCheck1, vCheck2;
    private TextView vTitle, vSubTitle, vTitleTrip;
    private String mWhere = null;
    private String mUrl = null;
    private String mTitle = null;
    private String mExtension = null;
    private RequestMap mResultRequest = null;
    private int MTextColor;
    private String MTitle, MSubTitle = null;
    private int MTxtSize;
    private boolean MShowTitleTrip = false;
    private Typeface mTypeface = null;
    private int MScaleMode = 0;
    private Context MyContext;
    private String MPreview;
    private boolean mTiyPng = false;
    private OnCallBack mCallBack;
    private boolean isSetClickable = true;
    private List<RestClient.RequestPara> mReqList = null;

    public imageUploader(Context context) {
        super(context);
    }

    public imageUploader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public imageUploader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public imageUploader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {

        MyContext = context;

        rootView = inflate(context, R.layout.iu_view, this);

        vCardBorder = rootView.findViewById(R.id.cardBorder);
        vProgress = rootView.findViewById(R.id.cProgress);

        vImageView = rootView.findViewById(R.id.imageView);

        vCheck1 = rootView.findViewById(R.id.check1);
        vCheck2 = rootView.findViewById(R.id.check2);

        vTitle = rootView.findViewById(R.id.txtTitle);
        vTitleTrip = rootView.findViewById(R.id.txtTitleTrip);
        vSubTitle = rootView.findViewById(R.id.txtSubTitle);


        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.imageUploader, 0, 0);

            MTextColor = typedArray.getColor(R.styleable.imageUploader_android_textColor, Color.GRAY);
            MTitle = typedArray.getString(R.styleable.imageUploader_Title);

            isSetClickable = typedArray.getBoolean(R.styleable.imageUploader_IsClickable, true);

            MPreview = typedArray.getString(R.styleable.imageUploader_Preview);

            MSubTitle = typedArray.getString(R.styleable.imageUploader_SubTitle);
            vSubTitle.setTextColor(typedArray.getColor(R.styleable.imageUploader_SubTitleColor, Color.GRAY));
            MShowTitleTrip = typedArray.getBoolean(R.styleable.imageUploader_ShowTitleTop, false);

            float radius = typedArray.getDimension(R.styleable.imageUploader_CornerRadius, 6);
            vCardBorder.setRadius(radius);
            vCardBorder.setCardBackgroundColor(typedArray.getColor(R.styleable.imageUploader_StrokeColor, Color.GRAY));
            vCardBorder.setBackgroundColor(typedArray.getColor(R.styleable.imageUploader_BoxBackColor, Color.WHITE));
            vCardBorder.setMinimumWidth(typedArray.getInt(R.styleable.imageUploader_StrokeWidth, 2));

            vProgress.setPadding((int)radius,0,0,(int)radius);

            vTitle.setTextSize(typedArray.getDimensionPixelSize(R.styleable.imageUploader_TitleSize, 14));
            vTitleTrip.setTextSize(typedArray.getDimensionPixelSize(R.styleable.imageUploader_TitleTopSize, 14));

            MScaleMode = typedArray.getInt(R.styleable.imageUploader_ScaleMode, 0);
            switch (MScaleMode){
                case 0:
                    vImageView.setScaleType(ImageView.ScaleType.CENTER);
                    break;
                case 1:
                    vImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    break;
            }

            int margin = typedArray.getDimensionPixelSize(R.styleable.imageUploader_ImageMargin , 0);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.setMargins(margin, margin, margin, margin);
            vImageView.setLayoutParams(params);

            typedArray.recycle();
        }

        vImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSetClickable)
                    OpenDialogGetImage(context);
            }
        });


        vTitle.setText(MTitle);
        vTitleTrip.setText(MTitle);
        if(MSubTitle != null) vSubTitle.setText(MSubTitle);
        vTitle.setTextColor(MTextColor);
        vTitleTrip.setTextAppearance(context, R.style.TextStyleShadow_1);
        if(mTypeface != null){
            vTitle.setTypeface(mTypeface);
            vTitleTrip.setTypeface(mTypeface);
        }


        SetPreview(context);
//        Picasso.with(context).load(MPreview).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                vImageView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        });
    }

    public void OpenDialogGetImage(Context context) {
        if(GetPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            IntentImage(mResultRequest.getrCode());
        }else
            Toast.makeText(context ,"STORAGE ACCESS DENIED !" ,Toast.LENGTH_SHORT).show();
    }

    private void SetPreview(Context context) {
        Picasso.with(context)
                .load(MPreview)
                .fit().centerInside()
                .into(vImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if(MShowTitleTrip) vTitleTrip.setVisibility(VISIBLE);
                        vTitle.setVisibility(GONE);
                        vSubTitle.setVisibility(GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void IntentImage(int mResultRequest) {
//        final Context context = (FragmentActivity) getContext();

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select picture"), mResultRequest );
        myStartActivityForResult((FragmentActivity) getContext(), Intent.createChooser(intent, "Select picture"), mResultRequest, new OnActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {

                if(resultCode == Activity.RESULT_OK && data != null){
                    // SDK < API11
                    String realPath;
                    if (Build.VERSION.SDK_INT < 11) {
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11((FragmentActivity) getContext(), data.getData());
                    }
                    // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19) {
                        realPath = RealPathUtil.getRealPathFromURI_API11to18((FragmentActivity) getContext(), data.getData());
                    }
                    // SDK > 19 (Android 4.4)
                    else {
                        realPath = RealPathUtil.getRealPathFromURI_API19((FragmentActivity) getContext(), data.getData());
                    }
                    vImageView.setImageDrawable(null);
                    uploadImage((FragmentActivity) getContext(), new File(realPath), requestCode);

                    System.out.println("Image Path : " + realPath);
                }
            }
        });
    }

    private void uploadImage(final Context context, final File mFile, int requestCode){
        if (mFile == null) {
            Toast.makeText(context,"Please take photo first", Toast.LENGTH_LONG).show();
            return;
        }

//        String name = mFile.getName();
        String extension = mFile.getAbsolutePath().substring(mFile.getAbsolutePath().lastIndexOf("."));
//        String directory = mFile.getAbsolutePath().substring(0,mFile.getAbsolutePath().lastIndexOf(name));
//        File from      = new File(directory, mFile.getName());
//        File to        = new File(directory, "AQW111" + extension);
//        boolean success = mFile.renameTo(to);
//        String n2 = mFile.getName();

        TypedFile typedFile = new TypedFile("multipart/form-data", mFile);
        vProgress.setVisibility(VISIBLE);
        vCheck1.setVisibility(GONE);
        vCheck2.setVisibility(GONE);
        vTitleTrip.setVisibility(GONE);

        String fileName = mFile.getName();
        if(mExtension != null){
            extension = mExtension;
        }
        if(mTitle != null) fileName = mTitle+extension;

        RestClient restClient = new RestClient(mUrl, mReqList);
        final String finalFileName = fileName;
        restClient.getService(fileName, mResultRequest.getrRequest(), mWhere).upload(typedFile, new CancelableCallback<Response>() {
            @Override
            public void onSuccess(Response response, Response response2) {
                vProgress.setVisibility(GONE);

                if(MShowTitleTrip) vTitleTrip.setVisibility(VISIBLE);
                vTitle.setVisibility(GONE);
                vSubTitle.setVisibility(GONE);

                Picasso.with(context)
                        .load(mFile)
                        .fit().centerInside()
                        .into(vImageView, new Callback() {
                            @Override
                            public void onSuccess() {
//                                Toast.makeText(context,"ok",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError() {
//                                Toast.makeText(context,"fail",Toast.LENGTH_LONG).show();
                            }
                        });
                if(response.getStatus() == 202)
                    vCheck1.setVisibility(VISIBLE);
                if(response.getStatus() == 200) {
                    vCheck1.setVisibility(VISIBLE);
                    vCheck2.setVisibility(VISIBLE);
                }
                if(mCallBack != null) mCallBack.onEvent(response.getStatus(), finalFileName);
//                Toast.makeText(context,"Upload successfully",Toast.LENGTH_LONG).show();
                Log.e("Upload", "success");
            }

            @Override
            public void onFailure(RetrofitError error) {
                vImageView.setImageBitmap(null);
                vTitle.setVisibility(VISIBLE);
                vSubTitle.setVisibility(VISIBLE);

                vProgress.setVisibility(GONE);
                Toast.makeText(context,"Upload failed",Toast.LENGTH_LONG).show();
//                Log.e("Upload", error.getMessage().toString());
            }
        });

    }

    //TODO: -------------------------------------------- Function Attributes ----------------------------------------------------

    public imageUploader url(String myUrl){
        this.mUrl = myUrl;
        return this;
    }

    public imageUploader preview(Context context, String url){
        this.MPreview = url;
        SetPreview(context);
//        Picasso.with(context)
//                .load(url)
//                .fit().centerInside()
//                .into(vImageView);

//        Picasso.with(context).load(MPreview).fit().into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                vImageView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        });
        return this;
    }

    public imageUploader fileName(String myTitle){
        this.mTitle = myTitle;
        return this;
    }

    public imageUploader extension(String myExtension){
        this.mExtension = myExtension;
        return this;
    }

    public imageUploader tinyPng(String apiKey){
        Tinify.setKey(apiKey);
        this.mTiyPng = true;
        return this;
    }

    public imageUploader requestCode(RequestMap myCode, String myWhere){
        this.mResultRequest = myCode;
        this.mWhere = myWhere;
        return this;
    }

    public imageUploader requestParameters(List<RestClient.RequestPara> requestParas){
        this.mReqList = new ArrayList<>();
        this.mReqList = requestParas;
        return this;
    }

//    public imageUploader where(String myWhere){
//        this.mWhere = myWhere;
//        return this;
//    }

    public imageUploader titleTypeFace(Typeface myTypeface){
        this.mTypeface = myTypeface;
        vTitle.setTypeface(mTypeface);
        vTitleTrip.setTypeface(mTypeface);
        return this;
    }

    public void setIsClickable(boolean setClickable) {
        this.isSetClickable = setClickable;
    }

    public interface OnCallBack {
        void onEvent(int status, String finalFileName);
    }

    public void setOnCallBack(OnCallBack eventListener) {
        mCallBack = eventListener;
    }

//    public interface OnPlusClickListener {
//        void onEvent();
//    }

//    public void setOnPlusClickListener(OnClickListener eventListener) {
//        mPlusListener = eventListener;
//    }


    //TODO: --------------------------------------- Activity Result ----------------------------------------------------

    private static void myStartActivityForResult(FragmentActivity act, Intent in, int requestCode, OnActivityResult cb) {
        Fragment aux = new FragmentForResult(cb);
        FragmentManager fm = act.getSupportFragmentManager();
        fm.beginTransaction().add(aux, "FRAGMENT_TAG").commit();
        fm.executePendingTransactions();
        aux.startActivityForResult(in, requestCode);
    }

    private interface OnActivityResult {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    @SuppressLint("ValidFragment")
    public static class FragmentForResult extends Fragment {
        private OnActivityResult cb;
        FragmentForResult(OnActivityResult cb) {
            this.cb = cb;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (cb != null) cb.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }


    //TODO : ----------------------------------------- Get Permission -------------------------------------------

    public static boolean GetPermission(final Context context, String permission){
        final boolean[] ret = {false};
        Dexter.withContext(context)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { ret[0] = true; }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) { ret[0] = false; }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { ret[0] = false;}
                }).check();
        return ret[0];
    }
    private void GetPermission1(final Context context){
        String pREAD_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
        Collection<String> ListPermission = new ArrayList<>();

        Dexter.withContext(context)
                .withPermissions(
                        ListPermission
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                List<PermissionDeniedResponse> ListDenide = report.getDeniedPermissionResponses();
                for(int i = 0; i < ListDenide.size(); i++){
//                    if(ListDenide.get(i).getPermissionName().toUpperCase().contains("CAMERA"))
//                        MBtnQrCode.setVisibility(GONE);
//                    if(ListDenide.get(i).getPermissionName().toUpperCase().contains("RECORD_AUDIO"))
//                        MBtnVoice.setVisibility(GONE);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken permissionToken) {
                for(int i = 0; i < permissions.size(); i++){
//                    if(permissions.get(i).getName().toUpperCase().contains("CAMERA"))
//                        if(UseSpeechToText) {
//                            Toast.makeText(context,"CAMERA ACCESS DENIED !",Toast.LENGTH_SHORT).show();
//                        }
//                    MBtnQrCode.setVisibility(GONE);
//                    if(permissions.get(i).getName().toUpperCase().contains("RECORD_AUDIO"))
//                        if(UseBarcodeScanner) {
//                            Toast.makeText(context,"AUDIO ACCESS DENIED !",Toast.LENGTH_SHORT).show();
//                        }
//                    MBtnVoice.setVisibility(GONE);
                }
            }

        }).check();
    }

    public static class RequestMap {
        private int rCode;
        private String rRequest;

        public RequestMap(int code, String request){
            this.rCode = code;
            this.rRequest = request;
        }

        public int getrCode() {
            return rCode;
        }

        public void setrCode(int rCode) {
            this.rCode = rCode;
        }

        public String getrRequest() {
            return rRequest;
        }

        public void setrRequest(String rRequest) {
            this.rRequest = rRequest;
        }
    }
}
