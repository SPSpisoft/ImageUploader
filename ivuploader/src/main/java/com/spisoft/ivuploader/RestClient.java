package com.spisoft.ivuploader;

import android.provider.SyncStateContract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {
    private UploadService uploadService;
//    private String URL = MyUrlFiles+"api/";
    private String myCode = "";
    private String myName = "";
    private String myWhere = "";
    public final static String Authorization_User = "SP" ;
    public final static String Authorization_Pass = "SP";

    public RestClient(final String myUrl, final List<RequestPara> myListPara){
        Gson localGson = new GsonBuilder().create();

        final String baseAuthStr = Authorization_User + ":" + Authorization_Pass;

        this.uploadService = ((UploadService)new RestAdapter.Builder()
                .setEndpoint(myUrl)
                .setConverter(new GsonConverter(localGson))
                .setRequestInterceptor(new RequestInterceptor()
                {
                    public void intercept(RequestFacade requestFacade)
                    {   //By adding header to the request will allow us to debug into .Net code in server
                        if (myUrl.contains("199.166.1.111") ) {
                            requestFacade.addHeader("Host", "localhost");
                        }

                        if(myListPara != null){
                            requestFacade.addHeader("Authorization", "Basic " + baseAuthStr);

                            for (RequestPara requestPara : myListPara)
                                requestFacade.addQueryParam(requestPara.getReqPara(), requestPara.getReqValue());
                        }else {
                            requestFacade.addQueryParam("mCode", myCode);
                            requestFacade.addQueryParam("mName", myName);
                            requestFacade.addQueryParam("mWhere", myWhere);
                        }
                    }
                })
                .build().create(UploadService.class));
    }

    public UploadService getService(String mName, String mCode, String mWhere)
    {
        if(mName != null) this.myName = mName;
        this.myCode = mCode;
        if(mWhere != null) this.myWhere = mWhere;
        return this.uploadService;
    }

    public static class RequestPara {
        private String reqPara;
        private String reqValue;

        public RequestPara(String reqPara, String reqValue) {
            this.reqPara = reqPara;
            this.reqValue = reqValue;
        }

        public String getReqPara() {
            return reqPara;
        }

        public void setReqPara(String reqPara) {
            this.reqPara = reqPara;
        }

        public String getReqValue() {
            return reqValue;
        }

        public void setReqValue(String reqValue) {
            this.reqValue = reqValue;
        }
    }
}