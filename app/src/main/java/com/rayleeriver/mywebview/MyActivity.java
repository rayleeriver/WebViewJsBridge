package com.rayleeriver.mywebview;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

public class MyActivity extends Activity implements OnClickListener {

    private static final String URL = "file:///android_asset/index.html";
    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String user = ((EditText) findViewById(R.id.edit_text)).getText().toString();
                if (user.isEmpty()) {
                    user = "World";
                }
                String javascript = "javascript: document.getElementById('msg').innerHTML='Hello " + user + "!';injectedObject.toast();";
                view.loadUrl(javascript);
            }
        });

        class JsObject {
            @JavascriptInterface
            public String toString() { return "injectedObject"; }

            @JavascriptInterface
            public void toast() {
                Toast.makeText(MyActivity.this, "aa", Toast.LENGTH_SHORT);
            }
        }
        mWebView.addJavascriptInterface(new JsObject(), "injectedObject");
        mWebView.loadData("", "text/html", null);
        mWebView.loadUrl("javascript:alert(injectedObject.toast())");

        refreshWebView();
        findViewById(R.id.button).setOnClickListener(this);
    }

    private void refreshWebView() {
        mWebView.loadUrl(URL);
    }

    @Override
    public void onClick(View v) {
        refreshWebView();
    }

}