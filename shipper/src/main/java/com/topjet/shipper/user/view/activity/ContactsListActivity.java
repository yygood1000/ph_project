package com.topjet.shipper.user.view.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.user.model.extra.AddContactExtra;
import com.topjet.shipper.user.model.serviceAPI.ContactCommand;
import com.topjet.shipper.user.model.serviceAPI.ContactCommandAPI;
import com.topjet.shipper.user.presenter.ContactsListPresenter;
import com.topjet.shipper.user.view.adapter.ContactsListAdapter;

/**
 * 常用联系人列表
 * Created by tsj004 on 2017/8/22.
 */

public class ContactsListActivity extends BaseRecyclerViewActivity<ContactsListPresenter, UsualContactListItem> implements ContactsListView<UsualContactListItem> {

    ContactsListAdapter mAdapter;

    @Override
    public void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT).setTitleText(R.string.contacts_title).setRightText(R.string.contacts_add);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ContactsListPresenter(this, mContext, new ContactCommand(ContactCommandAPI.class, this));
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
        mAdapter.setContactClick(new ContactsListAdapter.ContactClick() {
            @Override
            public void editClick(UsualContactListItem item) {
                //编辑
                AddContactExtra extra = new AddContactExtra(false,AddContactExtra.LIST,item.getLinkman_id());
                turnToActivity(ContactsInputActivity.class, extra);
            }

            @Override
            public void deleteClick(UsualContactListItem item, int position) {
                mPresenter.deleteLinkmanByid(item, position);
            }
        });
    }

    /**
     * 添加联系人
     */
    @Override
    public void onClickRightText() {
        // 1. 实名认证通过才能添加
        CheckUserStatusUtils.isRealNameAuthentication(this,
                new CheckUserStatusUtils.OnJudgeResultListener() {
                    @Override
                    public void onSucceed() {
                        turnToActivity(ContactsInputActivity.class, new AddContactExtra(true, AddContactExtra.LIST));
                    }
                });
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        mAdapter = new ContactsListAdapter(this);
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

    @Override
    public void errorClick() {
        super.errorClick();
        refresh();
    }

    @Override
    public void deleteItem(UsualContactListItem item, int position) {
        refresh();
    }
}
