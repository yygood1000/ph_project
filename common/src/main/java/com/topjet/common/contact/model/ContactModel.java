package com.topjet.common.contact.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.topjet.common.contact.model.pinyin.CharacterParser;
import com.topjet.common.contact.model.pinyin.PinyinComparator;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/17
 * describe: 联系人数据处理
 */

public class ContactModel {

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    private ArrayList<ContactBean> mContactBeans = new ArrayList<>();

    public ContactModel() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
    }

    /**
     * 获取本机联系人信息
     *
     * @param mContext
     * @return
     */
    @NonNull
    public ArrayList<ContactBean> getPhoneContacts(Context mContext) {
        ArrayList<ContactBean> result = new ArrayList<>(0);
        ContentResolver resolver = mContext.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(0).replace(" ", "");
                String contactName = phoneCursor.getString(1);
                result.add(new ContactBean(contactName, phoneNumber));
            }
            phoneCursor.close();
        }

        filledData(result);
        mContactBeans = result;
        return result;
    }


    /**
     * 获取名字首字母-大写
     * 加工数据
     *
     * @param data
     * @return
     */
    public List<ContactBean> filledData(ArrayList<ContactBean> data) {
        for (ContactBean contact : data) {
            String pinyin = characterParser.getSelling(contact.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                contact.setSortLetters(sortString.toUpperCase());
            } else {
                contact.setSortLetters("#");
            }
        }

        //根据首字母排序
        Collections.sort(data, pinyinComparator);
        return data;

    }

    /**
     * 根据输入框中的值来过滤数据并更新数据
     *
     * @param filterStr
     */
    public List<ContactBean> filterData(String filterStr) {
        List<ContactBean> filterDateList = new ArrayList<>();

        if (StringUtils.isEmpty(filterStr)) {
            filterDateList = mContactBeans;
        } else {
            filterDateList.clear();
            for (ContactBean sortModel : mContactBeans) {
                String name = sortModel.getName();
                // 拼音匹配，汉字匹配
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())) {
                    filterDateList.add(sortModel);
                }
                String mobile = sortModel.getMobile();
                // 电话匹配
                if(StringUtils.isNotBlank(mobile) && mobile.contains(filterStr)){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        return filterDateList;
    }


    /**
     * 从系统通讯录选择联系人
     * @return 电话
     */
    public String getContactFormSystem(Context mContext, Intent intent){
        try {
            Uri contactUri = intent.getData();
            // String[] projection = { Phone.NUMBER, };
            Cursor cursor = mContext.getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            // Retrieve the phone number from the NUMBER column
            int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor.getString(column).replace(" ", "").replace("-", "").trim();
            String displayName = cursor.getString(displayNameColumn);
            return number;// 返回手机号
        } catch (Exception e) {
            return "";
        }
    }

}

