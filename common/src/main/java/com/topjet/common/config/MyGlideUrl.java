package com.topjet.common.config;

import com.bumptech.glide.load.model.GlideUrl;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.SystemUtils;

import java.util.Random;

/**
 * Created by yy on 2017/7/28.
 * <p>
 * Glide 用过url缓存
 * 重写该类，自定义key，生成文件名
 */

public class MyGlideUrl extends GlideUrl {
    private String key;

    public MyGlideUrl(String url, String key) {
        super(url);
        this.key = key;
    }

    @Override
    public String getCacheKey() {
        return key;
    }
}
