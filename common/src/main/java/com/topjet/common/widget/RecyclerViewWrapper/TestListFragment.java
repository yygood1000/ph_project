package com.topjet.common.widget.RecyclerViewWrapper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.utils.Toaster;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * creator: zhulunjun
 * time:    2017/9/29
 * describe:
 */

public class TestListFragment extends BaseRecyclerViewFragment {
    @BindView(R2.id.recyclerViewWrapper)
    public RecyclerViewWrapper testRecyclerView;

    @Override
    public BaseQuickAdapter getAdapter() {
        return new TestAdapter();
    }



    @Override
    public void loadData() {
        delay(2);
    }



    int i = 0;

    private void delay(int time) {
        i++;
        Observable.timer(time, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (i > 3) {
                            loadSuccess(TestBean.getData(0));
                        } else {
                            loadSuccess(TestBean.getData(10));
                        }

                    }
                });
    }


    @Override
    public void errorClick() {
        super.errorClick();
        Toaster.showShortToast("errorClick");
    }

    @Override
    public void emptyClick() {
        super.emptyClick();
        Toaster.showShortToast("emptyClick");
    }

    @Override
    public RecyclerViewWrapper getRecyclerView() {
        return testRecyclerView;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_recyclerview;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void initData() {

    }

}
