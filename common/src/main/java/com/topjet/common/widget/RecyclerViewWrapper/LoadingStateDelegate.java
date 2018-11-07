package com.topjet.common.widget.RecyclerViewWrapper;

import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewStub;

import com.topjet.common.widget.SwipeRefreshLayout.SwipeRefreshLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sunfusheng on 2017/1/16.
 */
public class LoadingStateDelegate {

    @IntDef({STATE.LOADING, STATE.SUCCEED, STATE.ERROR, STATE.EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATE {
        int LOADING = 0;
        int SUCCEED = 1;
        int ERROR = 2;
        int EMPTY = 3;
    }

    private View viewHolder[] = new View[4];

    public LoadingStateDelegate(View normalView, View loadingView, View errorStub, View emptyStub) {
        viewHolder[0] = loadingView;
        viewHolder[1] = normalView;
        viewHolder[2] = errorStub;
        viewHolder[3] = emptyStub;

        for (View v : viewHolder) {
            if (v == null) {
                continue;
            }
            if(v instanceof SwipeRefreshLayout)
                v.setVisibility(View.VISIBLE);
            else
                v.setVisibility(View.GONE);
        }
    }

    public View setViewState(@STATE int viewState) {
        if (viewState < 0 || viewState >= viewHolder.length) {
            return null;
        }

        for (View v : viewHolder) {
            if (v == null) {
                continue;
            }
            v.setVisibility(View.GONE);
        }

        if (viewHolder[viewState] == null) {
//            if (viewStubHolder[viewState] != null && viewStubHolder[viewState].getParent() != null) {
//                viewHolder[viewState] = viewStubHolder[viewState].inflate();
//            }
        }

        if (viewHolder[viewState] != null) {
            viewHolder[viewState].setVisibility(View.VISIBLE);
        }

        return viewHolder[viewState];
    }

    public void setEmptyView(View view){
        viewHolder[3] = view;
    }
}
