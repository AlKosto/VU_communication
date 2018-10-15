package com.example.imran.vucommunication;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebPages extends AppCompatActivity {

    private ImageView VuImageView;
    private WebView VuWebView;
    private ProgressBar VuPrograssBar;
    private String myCurrentUrl;
    private LinearLayout VuLinearLayout;

    private Toolbar webToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_pages);


        VuImageView = (ImageView) findViewById(R.id.vuImageView);
        VuWebView = (WebView) findViewById(R.id.vuWebViw);
        VuPrograssBar = (ProgressBar) findViewById(R.id.vuProgressbar);
        VuLinearLayout =(LinearLayout) findViewById(R.id.vuLinearLayout) ;

        webToolBar = (Toolbar) findViewById(R.id.webToolBar);
        setSupportActionBar(webToolBar);



        VuPrograssBar.setMax(100);
        VuWebView.loadUrl("http://vu.edu.bd/");

        VuWebView.getSettings().setJavaScriptEnabled(true);

        VuWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                VuLinearLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                VuLinearLayout.setVisibility(View.GONE);

                super.onPageFinished(view, url);

                myCurrentUrl = url;
            }
        });



        VuWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                VuPrograssBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                VuImageView.setImageBitmap(icon);
            }
        });


        VuWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request vuRequest = new DownloadManager.Request(Uri.parse(url));
                vuRequest.allowScanningByMediaScanner();
                vuRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager vuManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                vuManager.enqueue(vuRequest);

                Toast.makeText(WebPages.this, "File is downloading", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(VuWebView.canGoBack()){
            VuWebView.goBack();
        }
        else {
            sendUserToMainActivity();
        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(WebPages.this, MainActivity.class);
        startActivity(mainIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.vu_manu, menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_back:
                onBackPressed();
                break;

            case R.id.menu_forward:
                onForwardPressed();
                break;

            case R.id.menu_referresh:
                VuWebView.reload();
                break;

            case R.id.menu_share:
                goToSharePage();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToSharePage() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, myCurrentUrl);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Copied URL");
        startActivity(Intent.createChooser(shareIntent, "Share URL"));

    }

    private void onForwardPressed() {
        if(VuWebView.canGoForward()){
            VuWebView.goForward();
        }else {
            Toast.makeText(this, "There's no forward pages", Toast.LENGTH_SHORT).show();
        }

    }


}
