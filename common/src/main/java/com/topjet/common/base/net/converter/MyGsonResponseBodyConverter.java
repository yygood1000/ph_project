/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.topjet.common.base.net.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.topjet.common.base.net.exception.ApiException;
import com.topjet.common.base.net.exception.KeyInvalidException;
import com.topjet.common.base.net.exception.RetryException;
import com.topjet.common.base.net.exception.TokenInvalidException;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.base.net.response.Error;
import com.topjet.common.base.net.response.Result;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.ErrorConstant;
import com.topjet.common.utils.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Converter;

final class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private String systemCode;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    public Object convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        Object obj = null;
        try {
             obj = adapter.read(jsonReader);
        } catch (MalformedJsonException exception){
            Logger.e("数据加密 报错 "+CPersisData.getKey()+" "+CPersisData.getUserSession()+" "+exception.getMessage()+" "+value.string());
            if(TextUtils.isEmpty(CPersisData.getKey())){
                throw new KeyInvalidException(); // 加密key失效
            } else if(TextUtils.isEmpty(CPersisData.getUserSession())) {
                throw new TokenInvalidException();// session失效
            }
            // 重新加载，出现未知错误
            throw new RetryException();
        }

        if (obj instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse) obj;
            String systemCode = baseResponse.getSystemCode();
            String systemMsg = baseResponse.getSystemMsg();
            if (!TextUtils.isEmpty(systemCode) && !systemCode.equals(ErrorConstant.E_200)) {
                if (systemCode.equals(ErrorConstant.E_100) ||
                        systemCode.equals(ErrorConstant.E_103) ||
                        systemCode.equals(ErrorConstant.E_104)) {
                    throw new TokenInvalidException();// session失效
                } else if (systemCode.equals(ErrorConstant.E_112)) {
                    CPersisData.setKey("");
                    throw new KeyInvalidException(); // 加密key失效
                } else if (systemCode.equals(ErrorConstant.E_300)) {
                    // 业务异常
                    Result result = baseResponse.getResult();
                    if (result != null) {
                        Error error = result.getError();
                        if (error != null) {
                            String businessCode = error.getBusiness_code();
                            String businessMsg = error.getBusiness_msg();
                            if (businessCode != null) {
                                throw new ApiException(businessCode, businessMsg);
                            }
                        }
                    }
                } else {
                    throw new ApiException(systemCode, systemMsg);
                }
            }
        }
        value.close();
        return obj;

    }

}
