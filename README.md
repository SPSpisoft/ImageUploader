# ImageUploader

A library for uploading images to the server determined path & save path to the field on server DB :)


![A](https://user-images.githubusercontent.com/11540724/82868439-e57bae00-9f41-11ea-9600-b05ca5bc3a0f.gif)


  # How to Use;
      
    allprojects {
      repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
    }
  
  	dependencies {
	        implementation 'com.github.SPSpisoft:ImageUploader:Tag'
	  }

# sample : 

	xml:
    <com.spisoft.ivuploader.imageUploader
	android:id="@+id/iuLogo"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:paddingStart="3dp"
	android:paddingEnd="3dp"
	app:CornerRadius="6dp"
	app:StrokeColor="@android:color/darker_gray"
	app:StrokeWidth="2"
	app:SubTitle="@string/IuDescription"
	app:Title="@string/logo" />
		
	Code:		
      imageUploader vIuLogo = (imageUploader) findViewById(R.id.iuLogo);
      vIuLogo.url(MyUrlFiles+"api/").preview(MyStoreActivity.this, YOUR_PREVIEW_URL)
                .fileName(YOUR_FILE_NAME) // if not used fileName func save&set base fie name into server.
                .requestCode(new imageUploader.RequestMap(1, YOUR_REQUEST_ID), YOUR_CONDITION_TO_UPDATE_FIELD);



# Sample server code [c# - web api];


    [System.Web.Http.Route("api/upload")]
    public async Task<HttpResponseMessage> PostImage(string mName, string mCode, string mWhere)
    {
        string mPath = "";
        string mTbl = "", mFld = "";

      switch (mCode)
      {
          case YOUR_REQUEST_ID:
              mPath = Image_Banner_Path;
              mTbl = Tbl_Store;
              mFld = Tbl_Store_Fld_Banner;
              break;

          default:
              mCode = null;
              break;
      }

      try
      {
          if (!Request.Content.IsMimeMultipartContent() || mName == null)
              throw new HttpResponseException(HttpStatusCode.UnsupportedMediaType);

          var adPath = "_Files/Images/" + mPath; // images path
          var uploadPath = Path.Combine(HttpContext.Current.Server.MapPath("~/"+adPath));
          var multipartFormDataStreamProvider = new CustomUploadMultipartFormProvider(uploadPath, mName);
          await Request.Content.ReadAsMultipartAsync(multipartFormDataStreamProvider);
          foreach (var key in multipartFormDataStreamProvider.FormData.AllKeys)
          {
              foreach (var val in multipartFormDataStreamProvider.FormData.GetValues(key))
              {
                  Console.WriteLine(string.Format("{0}: {1}", key, val));
              }
          }

          var localFileName = multipartFormDataStreamProvider.FileData.ToString();
          if (mCode != null)
          {
              // UpdateWhere >> a function to update DB .. return (True/False)
              // mName >> YOUR_FILE_NAME
              // mWhare >> YOUR_CONDITION_TO_UPDATE_FIELD
              if(UpdateWhere(ConStr, mTbl, mFld + "='" + adPath + "/" + mName + "'", mWhare))
                  return new HttpResponseMessage(HttpStatusCode.OK);
          }

          return new HttpResponseMessage(HttpStatusCode.Accepted);
      }
      catch (Exception e)
      {
          return new HttpResponseMessage(HttpStatusCode.NotImplemented)
          {
              Content = new StringContent(e.Message)
          };
      }
      
      
