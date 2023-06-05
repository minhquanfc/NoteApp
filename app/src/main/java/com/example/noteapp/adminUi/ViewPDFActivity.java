package com.example.noteapp.adminUi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ViewPDFActivity extends AppCompatActivity {
    PDFView pdfView;
    ImageButton imageButton;
    TextView tv_title_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdfactivity);
        pdfView = findViewById(R.id.pdfView);
        imageButton = findViewById(R.id.btnback_viewpdf);
        tv_title_pdf = findViewById(R.id.tv_title_pdf);

        String url = "https://docs.google.com/document/d/1OMQPtzCJn8GG5u7gAu6feqXFb98uLcA8ZuwyJvh9xI4/edit?usp=sharing";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage("com.google.android.apps.docs");
        startActivity(intent);

//        Intent intent = getIntent();
//        String pdfurl = intent.getStringExtra("linkPdf");
//        String title = intent.getStringExtra("title");
//        tv_title_pdf.setText(title);
//
//        new RetrievePDFfromUrl().execute(pdfurl);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }
}