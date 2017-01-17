package sds.auto.plate.retrofit.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sds.auto.plate.base.Device;

public interface DeviceInterface {
    @FormUrlEncoded
    @POST("android/device.php")
    Call<Device> getData (@Field("upid") String user_upid );
}
