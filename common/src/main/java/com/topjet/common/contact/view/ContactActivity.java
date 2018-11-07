package com.topjet.common.contact.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.contact.ContactExtra;
import com.topjet.common.contact.ContactPresenter;
import com.topjet.common.contact.model.ContactBean;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.PermissionsUtils;
import com.topjet.common.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * creator: zhulunjun
 * time:    2017/10/16
 * describe: 本机联系人列表
 * 1. 添加熟车时，选择本地联系人
 * 2. 手机短信分享时选择本地联系人
 */

public class ContactActivity extends MvpActivity<ContactPresenter> implements ContactView {

    @BindView(R2.id.rv_contact)
    RecyclerView rvContact;
    @BindView(R2.id.sb_view)
    MySideBar sbView;
    @BindView(R2.id.tvLetter)
    TextView tvLetter;
    @BindView(R2.id.et_mobile)
    EditText etMobile;
    @BindView(R2.id.tv_btn_invitation)
    TextView tvBtnInvitation;
    @BindView(R2.id.ll_contact_empty)
    LinearLayout llContactEmpty;
    @BindView(R2.id.tv_btn_empty)
    TextView tvBtnEmpty;
    @BindView(R2.id.tv_familiar_msg)
    TextView tvFamiliarMsg;

    private ContactExtra contactExtra;
    private ContactAdapter adapter;

    @Override
    protected void initTitle() {
        super.initTitle();
        contactExtra = getIntentExtra(ContactExtra.getExtraName());
        String title = "添加熟车";
        if (contactExtra.getType() == ContactExtra.FAMILIAR_CAR) {
            title = "添加熟车";
        } else if (contactExtra.getType() == ContactExtra.SHARE) {
            title = "手机短信分享";
        }
        getMyTitleBar().setTitleText(title);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ContactPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initView() {
        rvContact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ContactAdapter(this);
        rvContact.setAdapter(adapter);
        adapter.setContactExtra(contactExtra);
        adapter.setBtnStyle(tvBtnInvitation, contactExtra);
        adapter.setContactBtnClick(new ContactAdapter.ContactBtnClick() {
            @Override
            public void btnClick(ContactBean item) {
                if (StringUtils.isNotBlank(item.getMobile()) && ApplyUtils.validateMobile(item.getMobile())) {
                    if (contactExtra.getType() == ContactExtra.FAMILIAR_CAR) {
                        mPresenter.invitation(item.getMobile());
                    } else {
                        mPresenter.shareSms(item.getMobile());
                    }
                } else {
                    showToast("手机号不存在");
                }
            }

            @Override
            public void contentClick(ContactBean item) {
                if (StringUtils.isNotBlank(item.getMobile())) {
                    etMobile.setText(item.getMobile());
                }
            }
        });

        // 字母触摸监听
        sbView.setOnTouchingLetterChangedListener(new MySideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    new RecyclerViewUtils(rvContact).smoothMoveToPosition(position);
                }
            }
        });
        // 选择字母的显示
        sbView.setTextView(tvLetter);

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 筛选, 替换数据
                adapter.replaceData(mPresenter.filterData(s.toString()));

                displayBtn(s.toString());
                displayList(s.toString());
                setEditLength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (contactExtra.getType() == ContactExtra.FAMILIAR_CAR) {
            tvFamiliarMsg.setVisibility(View.VISIBLE);
        } else {
            tvFamiliarMsg.setVisibility(View.GONE);
        }

    }

    /**
     * 设置可输入的长度
     * 如果输入的是数字，则11位
     * 如果输入的非纯数字，则5位
     */
    private void setEditLength(String txt) {
        if(ApplyUtils.checkNumber(txt)){
           // 输入的是数字
            setEditMaxLength(11);
        } else {
            // 输入的非纯数字
            setEditMaxLength(5);
        }
    }

    /**
     * 设置可输入的最大长度
     */
    private void setEditMaxLength(int length){
        etMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }


    /**
     * 输入正确的手机号格式（11位数字）后，【邀请】按钮高亮
     * 设置按钮的显示状态
     *
     * @param str
     */
    private void displayBtn(String str) {
        if (StringUtils.isNotBlank(str) && ApplyUtils.validateMobile(str)) {
            tvBtnInvitation.setEnabled(true);
            tvBtnInvitation.setTextColor(getResources().getColor(R.color.v3_common_green));
        } else {
            tvBtnInvitation.setEnabled(false);
            tvBtnInvitation.setTextColor(getResources().getColor(R.color.color_dddddd));
        }
    }

    /**
     * 设置列表的显示状态
     * 如果搜索，则不显示侧边和字母
     *
     * @param str
     */
    private void displayList(String str) {
        if (StringUtils.isNotBlank(str)) {
            sbView.setVisibility(View.INVISIBLE);
            adapter.setShowLetter(false);
        } else {
            sbView.setVisibility(View.VISIBLE);
            adapter.setShowLetter(true);
        }
    }

    @OnClick(R2.id.tv_btn_invitation)
    public void invitation() {
        mPresenter.invitation(etMobile.getText().toString().trim());
        etMobile.setText("");
    }


    @Override
    public void setContactData(List<ContactBean> data) {
        if (data != null && data.size() > 0) {
            if (llContactEmpty.isShown()) {
                llContactEmpty.setVisibility(View.GONE);
            }
            adapter.addData(data);
            etMobile.setHint(R.string.hint_contact_search);
        } else {
            emptyData();
        }

    }

    @Override
    public void emptyData() {
        etMobile.setHint(R.string.hint_contact_num);
        llContactEmpty.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getContactPermissions();
    }

    @OnClick(R2.id.tv_btn_empty)
    public void emptyClick() {
        PermissionsUtils.goToSyetemSetting(this);
    }
}
