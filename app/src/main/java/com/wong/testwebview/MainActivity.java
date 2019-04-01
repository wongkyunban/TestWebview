package com.wong.testwebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public static final String URL_PREDIX = "file:///android_asset/";

    private WebView mWebview;
    private Button loadUrlBtn;
    private Button evaluateJavascriptBtn;
    private Button jsInvokerAndroidBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebview = (WebView)findViewById(R.id.webview);
        initConfigureWebview();
        onLoad("www/android_invoker_js.html");

        loadUrlBtn = (Button)findViewById(R.id.load_url);
        loadUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //无参
                mWebview.loadUrl("javascript:javaCallJs()");
                //带参
                mWebview.loadUrl("javascript:callJsWithParams(" + "'android正在调用带参的JS方法'" + ")");
            }
        });

        evaluateJavascriptBtn = (Button)findViewById(R.id.evaluate_javascript);
        evaluateJavascriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mWebview.evaluateJavascript("javascript:callJs()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Toast.makeText(MainActivity.this,value,Toast.LENGTH_SHORT).show();
                    }
                });


                mWebview.evaluateJavascript("javascript:callJsWithParams('Bye Bye')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Toast.makeText(MainActivity.this,value,Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        jsInvokerAndroidBtn = (Button)findViewById(R.id.js_invoker_android);
        jsInvokerAndroidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoad("www/js_invoker_android.html");

            }
        });



    }
    private void initConfigureWebview(){
        //加载assets目录下的html
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        //得到webview设置
        WebSettings webSettings = mWebview.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        //设置字符编码
        webSettings.setDefaultTextEncodingName("UTF-8");
        //支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //将WebAppInterface与javascript绑定
        //webview.addJavascriptInterface(new PaymentJavaScriptInterface(),"Android");
        //android assets目录下html文件路径url为 file:///android_asset/profile.html


        mWebview.addJavascriptInterface(new JSInterface(),"AndroidNativeMethod");

        //允许alert弹出
        mWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

        });


    }
    private void onLoad(String url){
        String path = URL_PREDIX + url;
        mWebview.loadUrl(path);
    }


    private class JSInterface{
        @JavascriptInterface
        public void sendDataToAndroid(String data){
            Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
        }
    }
}
