package sds.auto.plate.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sds.auto.plate.base.AnpImage;

public interface PlateImageInterface {
    @GET("/mobile/api_ru_photo.php")
    Call<AnpImage> getData(@Query("nomer") String nomer, @Query("key") String key );

}

