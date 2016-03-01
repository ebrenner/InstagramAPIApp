package com.example.ericbrenner.instagramapiapp;

import com.example.ericbrenner.instagramapiapp.pojos.LikesResponse;
import com.example.ericbrenner.instagramapiapp.pojos.SearchResultsResponse;
import com.example.ericbrenner.instagramapiapp.pojos.SelfResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ericbrenner on 2/24/16.
 */
public interface InstagramAPIEndpointInterface {

    @GET("media/search?lat=37.773972&lng=-122.431297&scope=basic+public_content&distance=5000")
    Call<SearchResultsResponse> getPhotosByLocation(@Query("access_token") String token);

    @GET("tags/{tag}/media/recent?scope=basic+public_content")
    Call<SearchResultsResponse> getPhotosByTag(@Path("tag") String tag, @Query("access_token") String token);

    @GET("users/self/?scope=basic")
    Call<SelfResponse> getSelf(@Query("access_token") String token);

    @GET("media/{media-id}/likes?scope=basic+public_content")
    Call<LikesResponse> getLikes(@Path("media-id") String id, @Query("access_token") String token);

    @POST("media/{media-id}/likes?scope=likes")
    Call<ResponseBody> postLike(@Path("media-id") String id, @Query("access_token") String token);

    @DELETE("media/{media-id}/likes?scope=likes")
    Call<ResponseBody> deleteLike(@Path("media-id") String id, @Query("access_token") String token);
}
