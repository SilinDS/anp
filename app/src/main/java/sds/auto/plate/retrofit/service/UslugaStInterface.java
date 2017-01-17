package sds.auto.plate.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import sds.auto.plate.base.Webdk;

public interface UslugaStInterface {
    @Headers({
       //     "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0",
            "DNT: 1",
            "Host: avto-yslyga.ru"
    })
    @GET("/proverit-tekhosmotr/")
    Call<ResponseBody> getData();

}

