package com.topjet.common.resource.bean;

/**
 * Created by yy on 2017/12/7.
 * <p>
 * 校验资源更新 返回集合实体类
 */

public class CheckResourseParamsBean {
    private String inner_version;//	是	string	资源文件版本号
    private String resource_type;//	是	string	资源文件类型 1.首页底部配置 2 首页中间配置 3.城市文件 4.车型 5.车长 6.包装方式 7.装卸方式 8.货物类型 9 货物单位

    public CheckResourseParamsBean(String inner_version, String resource_type) {
        this.inner_version = inner_version;
        this.resource_type = resource_type;
    }

    @Override
    public String toString() {
        return "CheckResourseParamsBean{" +
                "inner_version='" + inner_version + '\'' +
                ", resource_type='" + resource_type + '\'' +
                '}';
    }
}
