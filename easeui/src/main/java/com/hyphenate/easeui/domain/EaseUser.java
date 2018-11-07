/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.domain;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContact;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.utils.Logger;

public class EaseUser extends EMContact {

	/**
	 * initial letter for nickname
	 */
	protected String initialLetter;
	/**
	 * avatar of the user
	 */
	protected String avatar;
	protected String userId;//用户id
	protected String userPhone;//用户电话
	protected String isAnonymous = "0";//是否匿名 0显示头像，1不显示头像
	protected String avatarKey;//头像的key
	protected String sex = "2";//性别 1 女，2 男

	public static final String IS_ANONYMOUS = "1";//匿名
	public static final String NOT_ANONYMOUS = "0";//不匿名

	public static final String MAN = "2";   //女
	public static final String WOMAN = "1"; //男

	public EaseUser(String username){
		this.username = checkHead(username);
	}
	public EaseUser(String hx_userid,String hx_name,String avatar,String userid,String userPhone, String avatarKey, String isAnonymous, String sex){
		this.username = checkHead(hx_userid);
		this.nick = hx_name;
		this.avatar = avatar;
		this.userId = userid;
		this.userPhone = userPhone;
		this.avatarKey = avatarKey;
		this.isAnonymous = isAnonymous;
		this.sex = sex;
		if (isAnonymous.equals(IS_ANONYMOUS)){
			this.nick = "匿名用户";
		}
	}

	// 默认不匿名,男
	public EaseUser(String hx_userid,String hx_name,String avatar,String userid,String userPhone, String avatarKey){
		this.username = checkHead(hx_userid);
		this.nick = hx_name;
		this.avatar = avatar;
		this.userId = userid;
		this.userPhone = userPhone;
		this.avatarKey = avatarKey;
		this.isAnonymous = NOT_ANONYMOUS;
		this.sex = MAN;
		if (isAnonymous.equals(IS_ANONYMOUS)){
			this.nick = "匿名用户";
		}
	}

	/**
	 * 检查头部前缀是否已添加
	 */
	public String checkHead(String username){
		String head;
		if(username.equals(EMClient.getInstance().getCurrentUser())){
			return username;
		}
		if(username.equals(CPersisData.getUserID())) { // 添加自己
			head = CMemoryData.isDriver() ? CConstants.IM_HEAR_DRIVER : CConstants.IM_HEAR_SHIPPER;
		} else { // 添加别人
			head = CMemoryData.isDriver() ? CConstants.IM_HEAR_SHIPPER : CConstants.IM_HEAR_DRIVER;
		}
		// 用户id长度为1，长度为2 的情况直接加前缀
		if(username.length() == 1 || username.length() == 2){
			username = head + username;
		} else if(username.length() > 2 && !username.substring(0, 2).equals(head)){ //没添加前缀
			username = head + username;
		}
		Logger.d("用户信息 "+username);
		return username;
	}

	/**
	 * 获取数据库存储对象
	 */
	public IMUserBean getUserBean(EaseUser user){
		IMUserBean userBean = new IMUserBean();
		userBean.setAvatar(user.getAvatar());
		userBean.setAvatarKey(user.getAvatarKey());
		userBean.setIsAnonymous(user.getIsAnonymous());
		userBean.setNick(user.getNick());
		userBean.setSex(user.getSex());
		userBean.setUserId(user.getUserId());
		userBean.setUsername(user.getUsername());
		userBean.setUserPhone(user.getUserPhone());
		return userBean;
	}

	/**
	 * 获取环信用户对象
	 */
	public EaseUser getEaseUser(IMUserBean userBean){
		EaseUser user = new EaseUser(userBean.getUsername());
		user.setAvatar(userBean.getAvatar());
		user.setAvatarKey(userBean.getAvatarKey());
		user.setIsAnonymous(userBean.getIsAnonymous());
		user.setNick(userBean.getNick());
		user.setSex(userBean.getSex());
		user.setUserId(userBean.getUserId());
		user.setUserPhone(userBean.getUserPhone());
		return user;
	}

	public void setUserName(String userName){
		this.username = checkHead(userName);
	}
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getInitialLetter() {
		if(initialLetter == null){
			EaseCommonUtils.setUserInitialLetter(this);
		}
		return initialLetter;
	}

	public String getAvatarKey() {
		return avatarKey;
	}

	public void setAvatarKey(String avatarKey) {
		this.avatarKey = avatarKey;
	}

	public String getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(String isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public void setInitialLetter(String initialLetter) {
		this.initialLetter = initialLetter;
	}


	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EaseUser)) {
			return false;
		}
		return getUsername().equals(((EaseUser) o).getUsername());
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
}
