package sds.auto.plate.retrofit.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import sds.auto.plate.base.Libsite;
import sds.auto.plate.base.Webdk;

public interface WebDkInterface {
    @Headers({
            "Host: web-dk.ru",
            "Accept: */*",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: http://web-dk.ru/",
            "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0",
            "Connection: keep-alive",
            "DNT: 1",
            "X-Requested-With: XMLHttpRequest"

    })
    @FormUrlEncoded
    @POST("/services/poiskEAISTO.php")
    Call<Webdk> getData(@Header("Cookie") String cookie,
                        @Field("regNumber") String number,
                        @Field("token") String token);

}

