package co.za.jaspasystems.nonameapp.retrofit;

import java.util.List;

import co.za.jaspasystems.nonameapp.models.Hamper;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("load_hamper.php")
    Call<Hamper> uploadHamper(
            @Field("name") String name,
            @Field("contents") String contents,
            @Field("image") String image,
            @Field("price") double price
    );

    @GET("list_hampers.php")
    Call<List<Hamper>> listHampers();

    @FormUrlEncoded
    @POST("update_hamper.php")
    Call<Hamper> updateHamper(
            @Field("id") int id,
            @Field("contents") String contents,
            @Field("image") String image,
            @Field("price") double price
    );

    @FormUrlEncoded
    @POST("delete_hamper.php")
    Call<Hamper> deleteHamper( @Field("id") int id );
}
