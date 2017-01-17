package sds.auto.plate.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface WebDkStInterface {
    @Headers({
            "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0",
    })
    @GET("/")
    Call<ResponseBody> getData();

}

