package com.example.woorimanager_payment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("application/add_test.php")
    Call<ResponseBody> sendToken(@Field("reg_id") String token);

    @FormUrlEncoded
    @POST("application/add_test.php")
    Call<ResponseBody> sendTokenAndPhone(@Field("reg_id") String token, @Field("phone") String phone);

    // @Body 어노테이션을 사용하여 POST 요청 시에 전송할 데이터를 지정할 수 있습니다.
    // 이 경우에는 JSON 형태로 변환된 데이터가 요청 본문에 담겨 전송됩니다.
    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("application/add_test.php")
    Call<ResponseBody> sendRegisterData(@Body RegistrationDto dto);

    @FormUrlEncoded
    @POST("application/add_send_conv.php")
    Call<ResponseBody> sendMessage(@Field("text") String message, @Field("phone") String phone);
}