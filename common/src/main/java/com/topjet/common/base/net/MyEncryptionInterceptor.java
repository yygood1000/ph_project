package com.topjet.common.base.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.serverAPI.SystemCommandAPI;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.AESEncodeUtil;
import com.topjet.common.utils.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * creator: zhulunjun
 * time:    2018/3/14
 * describe: 请求加密拦截器
 */

public class MyEncryptionInterceptor implements Interceptor {

    /**
     * 是否加密
     */
    private boolean isEncrypt = true;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // 加密 请求体
        Request encryptRequest = encryptRequest(request);


        Response response = chain.proceed(encryptRequest);

        // 解密 响应体
        Response decryptResponse = decryptResponse(response);


        return decryptResponse;
    }

    /**
     * 加密请求体
     *
     * @param request 请求体
     * @return 加密后的请求体
     * @throws IOException 1
     */
    private Request encryptRequest(Request request) throws IOException {
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return request;
        }
        String requestString = requestBodyToString(requestBody);
        Logger.d("数据加密 00000請求躰 " + requestString);
        // 符合要求才加密
        if (isEncryption(requestString)) {
            String encryptionString = AESEncodeUtil.encrypt(requestString);
            Logger.d("数据加密 1111111請求躰 " + encryptionString);
            if (!TextUtils.isEmpty(encryptionString)) {
                // 重新封装请求体
                return stringToRequest(request, encryptionString);
            } else {
                // 如果key为空，也就不需要加密了
                isEncrypt = false;
                return stringToRequest(request, requestString);

            }
        }
        return stringToRequest(request, requestString);

    }

    /**
     * 解密响应体
     *
     * @param response 响应体
     * @return 解密后的响应体
     * @throws IOException 2
     */
    private Response decryptResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (!isEncrypt || responseBody == null) {
            return response;
        }
        String responseStr = responseBodyToString(responseBody);
        Logger.d("数据加密 00000响应体 " + responseStr);
        if (!isResponseError(responseStr)) { // 报错不加密，也不解密
            String decryptStr = AESEncodeUtil.decrypt(responseStr);
            Logger.d("数据加密 1111111响应体 " + decryptStr);
            if (!TextUtils.isEmpty(decryptStr)) {
                return stringToResponse(response, decryptStr);
            } else {
                return stringToResponse(response, responseStr);
            }
        }
        return stringToResponse(response, responseStr);

    }


    /**
     * String 转 request
     *
     * @param request    request
     * @param requestStr str
     * @return request
     */
    private Request stringToRequest(Request request, String requestStr) {
        RequestBody newRequestBody = RequestBody.create(request.body().contentType(), requestStr);
        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.method(request.method(), newRequestBody);
        return requestBuilder.build();
    }

    /**
     * string 转 response
     *
     * @param response    response
     * @param responseStr str
     * @return response
     */
    private Response stringToResponse(Response response, String responseStr) {
        ResponseBody newResponseBody = ResponseBody.create(response.body().contentType(), responseStr);
        Response.Builder responseBuilder = response.newBuilder();
        responseBuilder.body(newResponseBody);
        return responseBuilder.build();
    }

    /**
     * requestBody 转 string
     *
     * @param requestBody requestBody
     * @return string
     * @throws IOException 1
     */
    private String requestBodyToString(RequestBody requestBody) throws IOException {

        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readUtf8();
    }

    /**
     * responseBody 转 String
     *
     * @param responseBody responseBody
     * @return String
     * @throws IOException 2
     */
    private String responseBodyToString(ResponseBody responseBody) throws IOException {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        return buffer.readUtf8();
    }

    /**
     * 获取session和获取key接口不需要加密 版本更新也不需要加密
     *
     * @return 是否加密
     */
    private boolean isEncryption(String requestString) {
        if (requestString.contains(SystemCommandAPI.GET_KEY) ||
                requestString.contains(SystemCommandAPI.GET_SESSION) ||
                requestString.contains(SystemCommandAPI.APP_UPGRADE) ||
                TextUtils.isEmpty(requestString)) {
            isEncrypt = false;
        } else {
            isEncrypt = true;
        }
        return isEncrypt;
    }

    /**
     * 如果返回的是json体则不需要解密
     *
     * @param responseStr 请求数据
     * @return 是否解密
     */
    private boolean isResponseError(String responseStr) {
        // 如果报错了。不解密
        try {
            BaseResponse response = new Gson().fromJson(responseStr, BaseResponse.class);
//            Logger.d("数据加密  " + ((response != null) ? "不解密" : "解密"));
            return response != null || TextUtils.isEmpty(responseStr);
        } catch (Exception e) {
            // 没有报错 正常解析
            return false;
        }


    }
}
