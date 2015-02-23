package com.github.securecell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Browser extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.requestFocusFromTouch();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.loadUrl("http://monip.org");
        Initialize.setProxyToWebView(webView, Initialize.VPS_DOMAIN, 3128, Initialize.PACKAGE);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                webView.scrollTo(100, 100);
                webView.setInitialScale(100);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_OK, new Intent().putExtra("result", 1));
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                setResult(RESULT_OK, new Intent().putExtra("result", 1));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}