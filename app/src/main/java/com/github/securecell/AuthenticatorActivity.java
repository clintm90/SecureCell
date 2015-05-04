package com.github.securecell;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AuthenticatorActivity extends ActionBarActivity
{
    public static final String PARAM_AUTHTOKEN_TYPE = "auth.token";
    public static final String PARAM_CREATE = "create";
    public static final int REQ_CODE_CREATE = 1;
    public static final int REQ_CODE_UPDATE = 2;
    public static final String EXTRA_REQUEST_CODE = "req.code";
    public static final int RESP_CODE_SUCCESS = 0;
    public static final int RESP_CODE_ERROR = 1;
    public static final int RESP_CODE_CANCEL = 2;

    @Override
    protected void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        setContentView(R.layout.activity_authenticator);

        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);
        WebView webView = (WebView) findViewById(R.id.webViewLogin);
        
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.loadUrl("http://securecellhost.sourceforge.net/Authorization.php");
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                mProgress.setProgress(progress);
                if (progress == 100)
                {
                    mProgress.setVisibility(View.GONE);
                }
            }
        });
        //Initialize.setProxyToWebView(webView, Initialize.VPS_DOMAIN, 3128, Initialize.PACKAGE);
        //webView.loadData("<h1>salut</h1>", "text/html", "UTF-8");
        //webView.loadDataWithBaseURL("http://perdu.com", "<h1>salut</h1>", "text/html", "UTF-8", "http://monip.org");

        onSaveClick(null);
    }

    public void onCancelClick(View v)
    {
        finish();
    }

    public void onSaveClick(View v)
    {
        TextView tvUsername;
        TextView tvPassword;
        TextView tvApiKey;
        String username;
        String password;
        String apiKey;
        boolean hasErrors = false;

        /*tvUsername = (TextView) findViewById(R.id.uc_txt_username);
        tvPassword = (TextView) findViewById(R.id.uc_txt_password);
        tvApiKey = (TextView) findViewById(R.id.uc_txt_api_key);

        tvUsername.setBackgroundColor(Color.WHITE);
        tvPassword.setBackgroundColor(Color.WHITE);
        tvApiKey.setBackgroundColor(Color.WHITE);

        username = tvUsername.getText().toString();
        password = tvPassword.getText().toString();
        apiKey = tvApiKey.getText().toString();*/

        /*if (username.length() < 3)
        {
            hasErrors = true;
            tvUsername.setBackgroundColor(Color.MAGENTA);
        }
        if (password.length() < 3)
        {
            hasErrors = true;
            tvPassword.setBackgroundColor(Color.MAGENTA);
        }
        if (apiKey.length() < 3)
        {
            hasErrors = true;
            tvApiKey.setBackgroundColor(Color.MAGENTA);
        }*/

        // Now that we have done some simple "client side" validation it
        // is time to check with the server

        // ... perform some network activity here

        // finished

        /*String accountType = this.getIntent().getStringExtra(PARAM_AUTHTOKEN_TYPE);
        if (accountType == null)
        {
            accountType = "com.github.securecell";   //AccountAuthenticator.ACCOUNT_TYPE;
        }

        AccountManager accMgr = AccountManager.get(this);

        // This is the magic that addes the account to the Android Account Manager
        final Account account = new Account("clintm", accountType);
        accMgr.addAccountExplicitly(account, "rafale", null);

        // Now we tell our caller, could be the Andreoid Account Manager or even our own application
        // that the process was successful

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, "clintm");
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, accountType);
        //this.setAccountAuthenticatorResult(intent.getExtras());
        this.setResult(RESULT_OK, intent);
        //this.finish();
        finish();*/
    }
}