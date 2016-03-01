package com.example.ericbrenner.instagramapiapp.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericbrenner.instagramapiapp.Constants;
import com.example.ericbrenner.instagramapiapp.R;
import com.example.ericbrenner.instagramapiapp.pojos.LikesResponse;
import com.example.ericbrenner.instagramapiapp.pojos.SearchResult;
import com.example.ericbrenner.instagramapiapp.pojos.SelfResponse;
import com.example.ericbrenner.instagramapiapp.pojos.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ericbrenner on 2/29/16.
 */
public class ViewImageActivity extends InstagramAPIAppActivity implements View.OnClickListener {

    Button mButton;
    TextView mErrorText;
    SearchResult mSearchResult;
    SelfResponse mSelfResponse;

    @Override
    protected void setWebView() {
        mWebView = (WebView)findViewById(R.id.web_view);
    }

    @Override
    protected void requestData() {
        Call<SelfResponse> selfCall = mApiService.getSelf(mToken);
        selfCall.enqueue(new Callback<SelfResponse>() {
            @Override
            public void onResponse(Call<SelfResponse> call, Response<SelfResponse> response) {
                mSelfResponse = response.body();
                Call<LikesResponse> likesCall = mApiService.getLikes(mSearchResult.id, mToken);
                likesCall.enqueue(new Callback<LikesResponse>() {
                    @Override
                    public void onResponse(Call<LikesResponse> call, Response<LikesResponse> response) {
                        LikesResponse likesResponse = response.body();
                        ArrayList<User> users = likesResponse.data;
                        if (userListContainsUser(users, mSelfResponse.data)) {
                            mButton.setText(getString(R.string.unlike));
                        } else {
                            mButton.setText(getString(R.string.like));
                        }
                        mButton.setOnClickListener(ViewImageActivity.this);
                    }

                    @Override
                    public void onFailure(Call<LikesResponse> call, Throwable t) {
                        disableLikeButton();
                    }
                });
            }

            @Override
            public void onFailure(Call<SelfResponse> call, Throwable t) {
                disableLikeButton();
            }
        });
    }

    @Override
    protected void handleWebViewError() {
        disableLikeButton();
    }

    @Override
    protected void setInstagramAPIAppActiviityContentView() {
        setContentView(R.layout.activity_view_image);
    }

    @Override
    protected void setUpUI() {
        setUpToolbar();
        mButton = (Button)findViewById(R.id.like_button);
        mErrorText = (TextView)findViewById(R.id.error_message);
        ImageView imageView = (ImageView)findViewById(R.id.full_size_image_view);
        Bundle bundle = getIntent().getExtras();
        mSearchResult = bundle.getParcelable(Constants.KEY_SEARCH_RESULT);
        Picasso.with(this).load(mSearchResult.images.standard_resolution.url).into(imageView);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public static boolean userListContainsUser(ArrayList<User> userList, User user) {
        for (User u : userList) {
            if (u.id.equals(user.id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Call<ResponseBody> call;
        final String buttonText = mButton.getText().toString();
        if (buttonText.equals(getString(R.string.like))) {
            call = mApiService.postLike(mSearchResult.id, mToken);
        } else {
            call = mApiService.deleteLike(mSearchResult.id, mToken);
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (buttonText.equals(getString(R.string.like))) {
                        mButton.setText(getString(R.string.unlike));
                    } else {
                        mButton.setText(getString(R.string.like));
                    }
                } else {
                    mErrorText.setText(getString(R.string.cannot_complete_action));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mErrorText.setText(getString(R.string.cannot_complete_action));
            }
        });
    }

    private void disableLikeButton() {
        mButton.setEnabled(false);
        mErrorText.setText(getString(R.string.like_functionality_disabled));
    }
}
