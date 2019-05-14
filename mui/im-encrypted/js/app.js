window.app = {
	
	/**
	 * 后端netty服务发布的url地址
	 */
	nettyServerUrl: 'ws://192.168.43.106:8088/ws',
	
	/**
	 * 后端服务发布的url地址
	 */
	serverUrl: 'http://192.168.43.106:8888',
	
	/**
	 * 判断字符串是否为空
	 * @param {Object} str
	 * true：不为空
	 * false：为空
	 */
	isNotNull: function(str) {
		if (str != null && str != "" && str != undefined) {
			return true;
		}
		return false;
	},
	
	/**
	 * 封装消息提示框，默认mui的不支持居中和自定义icon，所以使用h5+
	 * @param {Object} msg
	 * @param {Object} type
	 */
	showToast: function(msg, type) {
		plus.nativeUI.toast(msg, 
			{icon: "image/" + type + ".png", verticalAlign: "center"})
	},
	
	/**
	 * 保存用户的全局对象
	 */
	setUserGlobalInfo: function(user) {
		var userInfoStr = JSON.stringify(user);
		plus.storage.setItem("userInfo", userInfoStr);
	},
	
	/**
	 * 获取用户的全局对象
	 */
	getUserGlobalInfo: function() {
		var userInfoStr = plus.storage.getItem("userInfo");
		return JSON.parse(userInfoStr);
	},
	
	/**
	 * 退出登录后，移除用户全局对象
	 */
	userLogout: function() {
		plus.storage.removeItem("userInfo");
	},
	
	/**
	 * 保存用户的联系人列表到本地（缓存）
	 */
	setContactList: function(contactList) {
		var contactListStr = JSON.stringify(contactList);
		plus.storage.setItem("contactList", contactListStr);
	},
	
	/**
	 * 获取本地缓存中的联系人列表
	 */
	getContactList: function() {
		var contactListStr = plus.storage.getItem("contactList");
		
		if (!this.isNotNull(contactListStr)) {
			return [];
		}
		
		return JSON.parse(contactListStr);
	},
	
	/**
	 * 根据用户id，从本地的缓存（联系人列表）中获取朋友的信息
	 */
	getFriendFromContactList: function(friendId) {
		var contactListStr = plus.storage.getItem("contactList");
		
		// 判断contactListStr是否为空
		if (this.isNotNull(contactListStr)) {
			// 不为空，则把用户信息返回
			var contactList = JSON.parse(contactListStr);
			for (var i = 0 ; i < contactList.length ; i ++) {
				var friend = contactList[i];
				if (friend.friendUserId == friendId) {
					return friend;
					break;
				}
			}
		} else {
			// 如果为空，直接返回null
			return null;
		}
	}
	
}
