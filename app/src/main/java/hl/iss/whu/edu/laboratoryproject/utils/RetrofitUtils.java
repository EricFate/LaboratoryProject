package hl.iss.whu.edu.laboratoryproject.utils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.IService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fate on 2016/11/17.
 */

public class RetrofitUtils {
    private static Retrofit retrofit;
    private static IService service;
    static {
        OkHttpClient client = new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }
    private RetrofitUtils(){}
    public static IService getService(){
        return retrofit.create(IService.class);
    }
}
