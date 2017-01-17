package sds.auto.plate.retrofit.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import sds.auto.plate.base.MyPlate;

public interface MyPlateInterface {
    @FormUrlEncoded
    @POST("android/plate.php")
    Call<MyPlate> getData(@Field("md5") String md5, @Field("plate") String plate,
                          @Field("vin") String vin, @Field("caption") String caption,
                          @Field("goal") String goal);

}

