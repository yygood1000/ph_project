package com.topjet.common.widget.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/26
 * describe:
 */

public class TestBean {
    String name;

    public TestBean(String name) {
        this.name = name;
    }


    public static List<TestBean> getData(int num){
        List<TestBean> datas = new ArrayList<>();
        for (int i=0; i<num; i++){
            TestBean testBean = new TestBean(i+ "条数据");
            datas.add(testBean);
        }
        return datas;
    }

}
