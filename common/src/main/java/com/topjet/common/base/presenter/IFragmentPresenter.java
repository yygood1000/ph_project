package com.topjet.common.base.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yy on 2017/3/13.
 * <p>
 * Fragment 的 Presenter 接口，继承自IPresenter
 * 该接口补全Fragment所特有的生命周期方法
 */

public interface IFragmentPresenter extends IPresenter {
    void onActivityCreated(@Nullable Bundle savedInstanceState);

    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState);

    void onViewCreated(View view, @Nullable Bundle savedInstanceState);
}
