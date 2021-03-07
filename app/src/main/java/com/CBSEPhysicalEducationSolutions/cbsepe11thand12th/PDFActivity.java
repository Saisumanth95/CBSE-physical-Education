package com.CBSEPhysicalEducationSolutions.cbsepe11thand12th;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.ads.AdView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class PDFActivity extends AppCompatActivity {


    public static ProgressBar progressBar;
    public static ImageButton pdf_download;
    private static PDFView pdf;
    String TAG = "This is to test";
    String pdfUrl;
    private AdView mAdView;
    public String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f);
        getSupportActionBar().hide();



        mAdView = (AdView) this.findViewById(R.id.adView);
        mAdView.loadAd(SplashActivity.adRequest);


        pdf = findViewById(R.id.ViewPdf);
        progressBar = findViewById(R.id.progressBar);
        pdf_download = findViewById(R.id.pdf_download);

        progressBar.setVisibility(View.VISIBLE);
        pdf_download.setVisibility(View.GONE);


        Intent intent = getIntent();

        int s = intent.getIntExtra("s",0);

        if (s == 1) {

            int position = intent.getIntExtra("pos",0);

            pdfUrl = MainActivity.item.get(position).getPdf();

            filename = MainActivity.item.get(position).getTitle() + "_Class-" + MainActivity.item.get(position).getGrade() + ".pdf";

        }else{

            pdfUrl = intent.getStringExtra("url");

            filename = "CBSE_Syllabus.pdf";

        }


        try{
            new RetrievePdfStream().execute(pdfUrl);

        }
        catch (Exception e){
            Toast.makeText(this, "Failed to load Url :" + e.toString(), Toast.LENGTH_SHORT).show();

        }

        pdf_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(PDFActivity.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                        ActivityCompat.requestPermissions(PDFActivity.this, new String[] {WRITE_EXTERNAL_STORAGE }, 1);


                    }else{

                        downloadFile(getApplicationContext(),filename,pdfUrl);

                    }


                }else{

                    downloadFile(getApplicationContext(),filename,pdfUrl);

                }

            }
        });

    }



    static class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {


        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;


            try {
                URL url = new URL(strings[0]);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                if (urlConnection.getResponseCode() == 200) {


                    inputStream = new BufferedInputStream(urlConnection.getInputStream());


                }

            } catch (IOException e) {
                return null;

            }


            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdf.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    progressBar.setVisibility(View.GONE);
                    pdf_download.setVisibility(View.VISIBLE);
                }
            }).load();


        }
    }


    public void downloadFile(Context context,String fileName,String url)
    {

        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

        dm.enqueue(request);

        Toast.makeText(getApplicationContext(),"Downloading...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                downloadFile(getApplicationContext(),filename,pdfUrl);

            }else{

                Toast.makeText(getApplicationContext(),"Storage access denied",Toast.LENGTH_SHORT).show();

            }


        }

    }
}