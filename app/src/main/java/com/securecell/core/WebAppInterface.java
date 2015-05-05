package com.securecell.core;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class WebAppInterface
{
    Context mContext;

    WebAppInterface(Context c)
    {
        mContext = c;
    }

    @JavascriptInterface
    public void Connect(String mail, String password)
    {
        Activity parent = (Activity) mContext;

        String accountType = parent.getIntent().getStringExtra("auth.token");
        if (accountType == null)
        {
            accountType = "com.github.securecell";   //AccountAuthenticator.ACCOUNT_TYPE;
        }

        AccountManager accMgr = AccountManager.get(mContext);

        final Account account = new Account(mail, accountType);
        accMgr.addAccountExplicitly(account, password, null);

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mail);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, accountType);
        //this.setAccountAuthenticatorResult(intent.getExtras());
        parent.setResult(parent.RESULT_OK, intent);
        parent.finish();
    }
}