package com.spisoft.ivuploader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {
    private UploadService uploadService;
//    private String URL = MyUrlFiles+"api/";
    private int myCode = 0;
    private String myName = "";
    private String myWhere = "";

    public RestClient(final String myUrl){
        Gson localGson = new GsonBuilder().create();

        this.uploadService = ((UploadService)new RestAdapter.Builder()
                .setEndpoint(myUrl)
                .setConverter(new GsonConverter(localGson))
                .setRequestInterceptor(new RequestInterceptor()
                {
                    public void intercept(RequestFacade requestFacade)
                    {   //By adding header to the request will allow us to debug into .Net code in server
                        if (myUrl.contains("199.166.1.111")) {
                            requestFacade.addHeader("Host", "localhost");
                        }
                        requestFacade.addQueryParam("mCode", String.valueOf(myCode));
                        requestFacade.addQueryParam("mName", myName);
                        requestFacade.addQueryParam("mWhere", myWhere);
                    }
                })
                .build().create(UploadService.class));
    }

    public UploadService getService(String mName, int mCode, String mWhere)
    {
        if(mName != null) this.myName = mName;
        this.myCode = mCode;
        if(mWhere != null) this.myWhere = mWhere;
        return this.uploadService;
    }

}