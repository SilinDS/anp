package sds.auto.plate.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PlateInterface {
    @GET("/mobile/api_ru_inf.php")
    Call<ResponseBody> getData( @Query("nomer") String nomer );

}

