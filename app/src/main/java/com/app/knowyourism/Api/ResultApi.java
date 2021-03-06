package com.app.knowyourism.Api;

import com.app.knowyourism.Model.ClubsResponse;
import com.app.knowyourism.Model.Feed.Feed;
import com.app.knowyourism.Model.Feed.FeedResponse;
import com.app.knowyourism.Model.LnF.LnFResponse;
import com.app.knowyourism.Model.LnF.LostFound;
import com.app.knowyourism.Model.Location.LocationResult;
import com.app.knowyourism.Model.LoginBody;
import com.app.knowyourism.Model.LoginResponse;
import com.app.knowyourism.Model.OtpInitiateBody;
import com.app.knowyourism.Model.OtpInitiateResponse;
import com.app.knowyourism.Model.Restaurant.Restaurant;
import com.app.knowyourism.Model.Result;
import com.app.knowyourism.Model.Student;
import com.app.knowyourism.Model.User;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ResultApi {
    public static final String BASE_URL = "https://kyi.herokuapp.com/api/";
    public static PostService postService = null;

    public static PostService getService() {
        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService = retrofit.create(PostService.class);
        }
        return postService;

    }

    public interface PostService {
        @GET("locations/college")
        Call< LocationResult > getLocation();

        @GET("locations/restaurant")
        Call< Restaurant > getRestaurant();

        @GET("v2/users?")
        Call< Result > getStudents(
                @Query("limit") String limit,
                @Query("skip") String skip,
                @Query("name") String name,
                @Query("house") String house,
                @Query("sex") String sex,
                @Query("admno") String admno,
                @Query("filter") String filter
        );

        @PATCH("students/{ID}")
        Call< Student > updateStudents(
                @Path("ID") String id,
                @Body Student post
        );

        @GET("v2/users/{ID}")
        Call< User > getUser(@Path("ID") String id);

        //Login

        @POST("v2/login/system/initialise")
        Call< OtpInitiateResponse > putOtpRequest(@Body OtpInitiateBody otpInitiateBody);


        @POST("v2/login/system/access_token")
        Call< LoginResponse > getLogin(@Body LoginBody loginBody);

        @GET("v2/grievances")
        Call< FeedResponse > getFeeds();

        @GET("v2/laf")
        Call< LnFResponse > getLnF();

        @GET("v2/clubs")
        Call< ClubsResponse > getClubs();

        @Multipart
        @POST("v2/laf")
        Call< LostFound > postLnF(
                @Part("title") String title,
                @Part("description") String details,
                @Part("found") Boolean found,
                @Part("userId") String user,
                @Part MultipartBody.Part photo);
    }
}
