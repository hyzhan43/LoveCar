package zqx.rj.com.lovecar.utils;

import android.content.Context;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zqx.rj.com.lovecar.entity.OkhttpResponse;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.utils
 * 文件名：  OkHttp
 * 创建者：  ZQX
 * 创建时间：2018/5/10 18:38
 * 描述：    封装 OkHttp
 */

public class OkHttp {

//    public OkhttpResponse get(String url) {
//
//        final Request request = new Request.Builder()
//                .url(url)
//                .build();
//        return enqueue(request);
//    }

    public OkhttpResponse get(Context context, String url) {

        String token = "Bearer " + ShareUtils.getString(context, "token", "");

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .build();
        return enqueue(request);
    }

//    public OkhttpResponse post(String url, RequestBody body) {
//
//        final Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        return enqueue(request);
//    }

    public OkhttpResponse post(Context context, String url, RequestBody body) {

        String token = "Bearer " + ShareUtils.getString(context, "token", "");

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", token)
                .build();

        return enqueue(request);
    }




    private OkhttpResponse enqueue(Request request) {

        OkhttpResponse okHttpData = new OkhttpResponse();
        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                okHttpData.setCode(OkhttpResponse.STATE_OK);
                okHttpData.setData(response.body().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
            okHttpData.setCode(OkhttpResponse.STATE_UNKNOWN_ERROR);
            okHttpData.setData(e.getMessage());
        }
        return okHttpData;
    }
}
