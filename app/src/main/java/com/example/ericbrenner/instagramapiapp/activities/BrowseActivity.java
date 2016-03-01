package com.example.ericbrenner.instagramapiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ericbrenner.instagramapiapp.Constants;
import com.example.ericbrenner.instagramapiapp.R;
import com.example.ericbrenner.instagramapiapp.adapters.ImageAdapter;
import com.example.ericbrenner.instagramapiapp.pojos.SearchResult;
import com.example.ericbrenner.instagramapiapp.pojos.SearchResultsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ericbrenner on 2/29/16.
 */
public class BrowseActivity extends InstagramAPIAppActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String[] SEARCH_TYPES = {"Location", "Tag"};

    TextView mSearchByTextView;
    TextView mErrorText;
    Spinner mSpinner;
    EditText mEditText;
    View mButton;

    GridView mGridView;
    ImageAdapter mAdapter;

    @Override
    protected void setWebView() {
        mWebView = (WebView)findViewById(R.id.web_view);
    }

    @Override
    protected void requestData() {
        requestPhotos();
    }

    @Override
    protected void handleWebViewError() {
        mButton.setEnabled(false);
        mErrorText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setInstagramAPIAppActiviityContentView() {
        setContentView(R.layout.activity_browse);
    }

    @Override
    protected void setUpUI() {
        setUpToolbar();
        mErrorText = (TextView)findViewById(R.id.error_message);
        setUpSearchUI();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpSearchUI() {
        mSearchByTextView = (TextView)findViewById(R.id.search_by_text_view);
        mSpinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, SEARCH_TYPES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mEditText = (EditText)findViewById(R.id.param_edit_text);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void requestPhotos() {
        Call<SearchResultsResponse> call;
        if (mSpinner.getSelectedItemPosition() == 0) {
            call = mApiService.getPhotosByLocation(mToken);
        } else {
            call = mApiService.getPhotosByTag(mEditText.getText().toString(), mToken);
        }
        call.enqueue(new Callback<SearchResultsResponse>() {
            @Override
            public void onResponse(Call<SearchResultsResponse> call, Response<SearchResultsResponse> response) {
                ArrayList<SearchResult> results = response.body().data;
                mAdapter = new ImageAdapter(BrowseActivity.this, results);
                if (mGridView == null) {
                    mGridView = (GridView) findViewById(R.id.grid_view);
                    mGridView.setOnItemClickListener(BrowseActivity.this);
                }
                mGridView.setVisibility(View.VISIBLE);
                mErrorText.setVisibility(View.GONE);
                mGridView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<SearchResultsResponse> call, Throwable t) {
                mGridView.setVisibility(View.GONE);
                mErrorText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        requestPhotos();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageAdapter adapter = (ImageAdapter)parent.getAdapter();
        SearchResult result = adapter.getItem(position);
        Intent intent = new Intent(this, ViewImageActivity.class);
        intent.putExtra(Constants.KEY_SEARCH_RESULT, result);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (position == 0) {
            mEditText.setEnabled(false);
            mEditText.setText(getString(R.string.hard_coded_location_name));
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        } else {
            mEditText.setEnabled(true);
            mEditText.setText("");

            imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
