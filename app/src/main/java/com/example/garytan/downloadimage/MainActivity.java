package com.example.garytan.downloadimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Button download;
    String SERVER_URL="http://120.125.83.185/download/test.jpg";
    static ImageView image;
    public static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download=(Button)findViewById(R.id.button);
        image=(ImageView)findViewById(R.id.imageView);
        download.setOnClickListener(this);
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Log.d(TAG,"go GCM");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                downloadimage downloadimage =new downloadimage(SERVER_URL);
                downloadimage.execute();
                break;
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
class downloadimage extends AsyncTask <String, Integer, String>{

    String SERVER_URL=null;
    String FilePath=null;
    final static String TAG="downloadimage";
    public downloadimage(String SERVER_URL) {
        this.SERVER_URL=SERVER_URL;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard, "test.jpg");
            FilePath=file.getPath();
            Log.d(TAG,"PATH"+FilePath);
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        if(res!=null){
                Log.d(TAG,"Error"+res);
        }else{
            File imgFile = new  File(FilePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                MainActivity.image.setImageBitmap(myBitmap);
            }
        }

    }


}