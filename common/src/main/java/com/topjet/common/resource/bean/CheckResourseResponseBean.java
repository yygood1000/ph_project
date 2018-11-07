package com.topjet.common.resource.bean;

/**
 * Created by yy on 2017/12/7.
 * <p>
 * 校验资源更新 返回集合实体类
 */

public class CheckResourseResponseBean {
    private String is_update;//String	是否有更新 0:无需更新 1:需要更新
    private String inner_version;//	String	资源文件内部版本号
    private String resource_type;//String	资源文件类型

    public String getIs_update() {
        return is_update;
    }

    public String getInner_version() {
        return inner_version;
    }

    public String getResource_type() {
        return resource_type;
    }

    @Override
    public String toString() {
        return "CheckResourseParamsBean{" +
                "is_update='" + is_update + '\'' +
                ", inner_version='" + inner_version + '\'' +
                ", resource_type='" + resource_type + '\'' +
                '}';
    }
}
