package com.snalopainen.data.api;

import com.google.gson.JsonObject;
import com.snalopainen.data.models.Comment;
import com.snalopainen.data.models.Shot;
import com.snalopainen.data.models.ShotWrapper;
import com.snalopainen.data.models.User;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author snalopainen.
 */
public class Api {


    public Api() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer 4e3e676ce2881d166900f7f0ba4f1c0c599f3126ff426c78e61fd3fc233b2a32")
                        .header("Accept", "application/vnd.dribbble.v1.param+json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.dribbble.com/v1/").client(client).addConverterFactory(GsonConverterFactory.create()).build();
        dribbbleService = retrofit.create(DribbbleService.class);
    }

    private DribbbleService dribbbleService;

    public DribbbleService getDribbbleService() {
        return dribbbleService;
    }

    public interface DribbbleService {
        @GET("/shots")
        Call<ArrayList<Shot>> shots(@Query("list") String param, @Query("page") int page);

        @GET("/shots/{id}")
        Call<Shot> shot(@Path("id") int shotId);

        @GET("/shots/{id}/rebounds")
        Call<ShotsResponse> rebounds(@Path("id") int shotId);

        @GET("/shots/{id}/comments")
        Call<ArrayList<Comment>> comments(@Path("id") int shotId, @Query("page") int page);

        @GET("/players/{id}/shots")
        Call<ArrayList<Shot>> userShots(@Path("id") int playerId, @Query("page") int page);

        @GET("/users/{id}/shots/following")
        Call<ArrayList<ShotWrapper>> followingShots(@Path("id") int userId, @Query("page") int page);

        @GET("/users/{id}/shots/likes")
        Call<ArrayList<ShotWrapper>> likesShots(@Path("id") int userId, @Query("page") int page);

        @GET("/users/{id}")
        Call<User> userProfile(@Path("id") int userId);

        @GET("/users/{id}/followers/")
        Call<UsersResponse> userFollowers(@Path("id") int userId, @Query("page") int page
        );

        @GET("/users/{id}/following")
        Call<UsersResponse> userFollowing(@Path("id") int userId, @Query("page") int page
        );

        @GET("/users/{id}/draftees")
        Call<UsersResponse> userDraftees(@Path("id") int userId, @Query("page") int page
        );

        @GET("/users/{id}/shots")
        Call<ArrayList<Shot>> userShots(@Path("id") String userName, @Query("page") int page
        );

        @GET("/users/{id}/following")
        Call<ArrayList<ShotWrapper>> followingShots(@Path("id") String userName, @Query("page") int page
        );

        @GET("/users/{id}/likes")
        Call<ArrayList<ShotWrapper>> likesShots(@Path("id") String userName, @Query("page") int page
        );

        @GET("/users/{id}")
        Call<User> userProfile(@Path("id") String userName);

        @GET("/users/{id}/followers")
        Call<UsersResponse> userFollowers(@Path("id") String userName, @Query("page") int page
        );

        @GET("/users/{id}/following")
        Call<UsersResponse> userFollowing(@Path("id") String userName, @Query("page") int page
        );

        @GET("/users/{id}/draftees")
        Call<UsersResponse> userDraftees(@Path("id") String userName, @Query("page") int page
        );

        @GET("/search")
        JsonObject search(@Query("q") String query, @Query("page") int page);
    }
}
