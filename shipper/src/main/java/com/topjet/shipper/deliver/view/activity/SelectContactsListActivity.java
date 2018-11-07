package com.topjet.shipper.deliver.view.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.adapter.SelectContactsListAdapter;
import com.topjet.shipper.user.model.extra.AddContactExtra;
import com.topjet.shipper.user.model.serviceAPI.ContactCommand;
import com.topjet.shipper.user.model.serviceAPI.ContactCommandAPI;
import com.topjet.shipper.user.presenter.SelectContactsListPresenter;
import com.topjet.shipper.user.view.activity.ContactsInputActivity;


/**
 * 选择常用联系人
 * Created by tsj004 on 2017/8/25.
 */

public class SelectContactsListActivity extends BaseRecyclerViewActivity<SelectContactsListPresenter, UsualContactListItem> implements
        IListView<UsualContactListItem> {

    SelectContactsListAdapter mAdapter;

    @Override
    public void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT).setTitleText(R.string.contacts_select).setRightText
                (R.string.contacts_add);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SelectContactsListPresenter(this, this, new ContactCommand(ContactCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contacts_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setEmptyText(getString(R.string.contact_empty));
        setEmptyBtnText(getString(R.string.add_contact));
        mAdapter.setSelectContactClick(new SelectContactsListAdapter.SelectContactClick() {
            @Override
            public void selectClick(UsualContactListItem item) {
                refreClickStatus(item);
                BusManager.getBus().post(item);//把选择结果发送出去
                finishPage();//关闭选择联系人列表页
            }
        });
    }

    /**
     * 刷新列表选中状态
     */
    private void refreClickStatus(UsualContactListItem item)
    {
        if (mData != null) {
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setCheck(false);
                if (item.getLinkman_id().equals(mData.get(i).getLinkman_id())) {
                    mData.get(i).setCheck(true);
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加联系人
     */
    @Override
    public void onClickRightText() {
        // 实名认证
        CheckUserStatusUtils.isRealNameAuthentication(this,
                new CheckUserStatusUtils.OnJudgeResultListener() {
                    @Override
                    public void onSucceed() {
                        turnToActivity(ContactsInputActivity.class, new AddContactExtra(true, AddContactExtra.SELECT));
                    }
                });
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        mAdapter = new SelectContactsListAdapter(this,mData);
        return mAdapter;
    }

    @Override
    public void loadData() {
        mPresenter.getLinkmanList(page);
    }

    @Override
    public void emptyClick() {
        super.emptyClick();
        onClickRightText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
