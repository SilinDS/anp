package sds.auto.plate.retrofit.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;
import sds.auto.plate.base.GibddAnswer;
import sds.auto.plate.base.Libsite;

public interface GibddInterface {
    @Headers({
            "Accept: application/json, text/javascript, */*; q=0.01",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            //    "Cookie", "JSESSIONID=" + JSESSIONID,
            "DNT: 1",
            "Pragma: no-cache",
            "Host: www.gibdd.ru",
            "Referer: http://www.gibdd.ru/check/auto/",
            "X-Requested-With: XMLHttpRequest"

    })
    @FormUrlEncoded
    @POST
    Call<GibddAnswer> getData(@Header("Cookie") String authorization,
                              @Url String url,
                              @Field("captchaWord") String captchaWord,
                              @Field("checkType") String checkType,
                              @Field("vin") String vin);
}

