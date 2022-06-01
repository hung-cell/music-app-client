package vn.assignment.musicapp.retrofit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.assignment.musicapp.model.Artist;
import vn.assignment.musicapp.model.ProfileImage;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.model.Type;
import vn.assignment.musicapp.model.User;


public interface MusicAppService {

    @GET("songs")
    Call<List<Song>> findAllSong();
    @GET("top")
    Call<List<Song>> findTop();
    @GET("artists")
    Call<List<Artist>> findAllArtist();
    @GET("types")
    Call<List<Type>> findAllTypes();
    @GET("songs/artist/{id}")
    Call<List<Song>> findSongByArtistId(@Path("id") int id);
    @GET("songs/type/{id}")
    Call<List<Song>> findSongByType(@Path("id") int id);
    @POST("register")
    Call<User> createUser(@Body User user);
    @GET("songs/search/{key}")
    Call<List<Song>> seachSong(@Path("key") String newText);

    @PUT("songs/view/{id}")
    Call<Void> increseView(@Path("id") int id);

    @PUT("songs/favorites/{uid}/{idSong}/{isFavorites}")
    Call<Void> favorite(@Path("uid") String uid, @Path("idSong") int id, @Path("isFavorites") boolean isFavories );

    @GET("songs/favorites/check/{uid}/{id}")
    Call<Boolean> check(@Path("uid") String uid, @Path("id") int id);
//    @FormUrlEncoded
//    @POST("api/auth/login")
//    Call<UserResponse> postLogin(@Field("email") String email,
//                                 @Field("password") String password);
//
//    @GET("api/auth/me")
//    Call<User> getUser(@Header("Authorization") String token);
//
//    @POST("api/auth/refresh")
//    Call<UserResponse> refreshToken(@Header("Authorization") String token);

}
