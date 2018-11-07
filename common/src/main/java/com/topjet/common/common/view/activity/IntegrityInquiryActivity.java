package com.topjet.common.common.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.common.presenter.IntegrityInquiryPresenter;
import com.topjet.common.common.view.adapter.SearchContactAdapter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.contact.model.ContactBean;
import com.topjet.common.db.DBManager;
import com.topjet.common.db.bean.PhoneNumberBean;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.EditTextGangedUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.wallet.utils.CheckUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * creator: zhulunjun
 * time:    2017/11/8
 * describe: 诚信查询
 */

public class IntegrityInquiryActivity extends MvpActivity<IntegrityInquiryPresenter> implements IntegrityInquiryView {

    @BindView(R2.id.iv_clear)
    ImageView ivClear;
    @BindView(R2.id.rv_contact)
    RecyclerView rvContact;
    @BindView(R2.id.ll_contact)
    LinearLayout llContact;
    @BindView(R2.id.tv_btn_search)
    TextView tvBtnSearch;
    @BindView(R2.id.et_mobile)
    EditText etMobile;

    public static final int REQ_CODE_PICK_CONTACTS = 111;
    private SearchContactAdapter mSearchContactAdapter;

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText("诚信查询");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IntegrityInquiryPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_integrity_inquiry;
    }

    @Override
    protected void initView() {
        EditTextGangedUtils.setEtLengthWithDelete(this, etMobile, ivClear);
        rvContact.setLayoutManager(new LinearLayoutManager(this));
        mSearchContactAdapter = new SearchContactAdapter();
        rvContact.setAdapter(mSearchContactAdapter);
        if (CMemoryData.isDriver()) {
            tvBtnSearch.setBackgroundResource(R.drawable.selector_btn_corner_blue);
        } else {
            tvBtnSearch.setBackgroundResource(R.drawable.selector_btn_corner_green);
        }

        // 筛选输入的
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 筛选, 替换数据
                mobile = s.toString().trim();
                if (mobile.length() != 11 && StringUtils.isNotBlank(mobile)) {
                    if(!rvContact.isShown()) {
                        rvContact.setVisibility(View.VISIBLE);
                    }
                    mSearchContactAdapter.replaceData(mPresenter.filterData(s.toString()));
                } else {
                    if(ApplyUtils.validateMobile(mobile)) {
                        // 存历史记录
                        mPresenter.saveContact(mobile);
                    }
                    rvContact.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchContactAdapter.setContactListener(new SearchContactAdapter.ContactListener() {
            @Override
            public void addressClick(ContactBean item) {
                etMobile.setText(item.getMobile());
            }
        });
    }

    @OnClick(R2.id.iv_clear)
    public void clear() {
        etMobile.setText("");
    }


    @OnClick(R2.id.ll_contact)
    public void toContactList() {
        // 去系统通讯录
        try {
            Uri uri;
            if (ContactsContract.Contacts.CONTENT_URI == null) {
                uri = Uri.parse("content://contacts/people");
            } else {
                uri = ContactsContract.Contacts.CONTENT_URI;
            }
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, REQ_CODE_PICK_CONTACTS);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R2.id.tv_btn_search)
    public void search() {
        mobile = etMobile.getText().toString().trim();
        if (StringUtils.isBlank(mobile)) {
            Toaster.showShortToast("请输入手机号");
        } else if (!ApplyUtils.validateMobile(mobile)) {
            Toaster.showShortToast("请输入有效手机号");
            clear();
        } else if(mobile.equals(CMemoryData.getUserMobile())){
            showOneselfDialog();
            clear();
        } else {
            // 这里要调接口
            mPresenter.query(mobile);
        }
    }


    /**
     * 如果输入自己的手机号就跳转 个人中心，弹窗提示
     */
    private void showOneselfDialog(){
        AutoDialogHelper.showContent(this,
                "您可以在个人中心查看到自己的资料",
                "我知道了",
                "去看看",
                new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        if(CMemoryData.isDriver()) {
                            CommonProvider
                                    .getInstance()
                                    .getJumpDriverProvider()
                                    .jumpMain(IntegrityInquiryActivity.this, TabIndex.PERSON_CENTER_PAGE);
                        } else {
                            CommonProvider
                                    .getInstance()
                                    .getJumpShipperProvider()
                                    .jumpMain(IntegrityInquiryActivity.this, TabIndex.PERSON_CENTER_PAGE);
                        }
                    }
                }).show();
    }

    private String mobile;

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);
        if (reqCode == REQ_CODE_PICK_CONTACTS && resultCode == RESULT_OK) { // 从联系人界面成功选择返回
            mobile = mPresenter.getContactFormSystem(intent);
            etMobile.setText(mobile);

            // 存历史记录
            mPresenter.saveContact(mobile);
        }

    }



}
