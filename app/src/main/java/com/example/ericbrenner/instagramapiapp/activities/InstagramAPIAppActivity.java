package com.example.ericbrenner.instagramapiapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ericbrenner.instagramapiapp.Constants;
import com.example.ericbrenner.instagramapiapp.InstagramAPIEndpointInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ericbrenner on 2/29/16.
 */
public abstract class InstagramAPIAppActivity extends AppCompatActivity {

    String mToken;
    WebView mWebView;
    InstagramAPIEndpointInterface mApiService;

    private void createEndpointInterface() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = retrofit.create(InstagramAPIEndpointInterface.class);
    }

    protected abstract void setWebView();

    private void setWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(Constants.REDIRECT_URL)) {
                    mToken = url.substring(Constants.REDIRECT_URL.length());
                    mWebView.setVisibility(View.GONE);
                    createEndpointInterface();
                    requestData();
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                mWebView.setVisibility(View.GONE);
                handleWebViewError();
            }
        });
    }

    protected abstract void requestData();

    protected abstract void handleWebViewError();

    protected abstract void setInstagramAPIAppActiviityContentView();

    protected abstract void setUpUI();

    private void login() {
        mWebView.setVisibility(View.VISIBLE);
        mWebView.loadUrl(Constants.LOGIN_URL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInstagramAPIAppActiviityContentView();
        setUpUI();
        setWebView();
        setWebViewClient();
        login();
    }
}
