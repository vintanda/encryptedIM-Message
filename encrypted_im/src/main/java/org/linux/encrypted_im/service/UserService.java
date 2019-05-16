package org.linux.encrypted_im.service;

import org.linux.encrypted_im.entity.Users;
import org.linux.encrypted_im.entity.vo.FriendRequestVO;
import org.linux.encrypted_im.entity.vo.MyFriendsVO;
import org.linux.encrypted_im.netty.ChatMsg;

import java.util.List;

public interface UserService {

    // 查询用户名是否存在
    public boolean queryUsernameIsExist(String username);

    // 查询用户是否存在
    public Users queryUserForLogin(String username, String pwd);

    // 注册
    public Users saveUser(Users user);

    // 更新用户信息
    public Users updateUserInfo(Users user);

    // 搜索朋友的前置条件
    public Integer preconditionSearchFriends(String myUserId, String friendUsername);

    // 根据用户名查询用户对象
    public Users queryUserInfoByUsername(String username);

    // 添加好友
    public void sendFriendRequest(String myUserId, String friendUsername);

    // 查询好友请求列表 -- 站在收到申请的角度查询
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    // 删除好友请求记录
    public void deleteFriendRequest(String sendUserId, String acceptUserId);

    // 通过好友请求
    //   1.保存好友
    //   2.逆向保存好友
    //   3.删除好友请求记录
    public void passFriendRequest(String sendUserId, String acceptUserId);

    // 查询好友列表
    public List<MyFriendsVO> queryMyFriends(String userId);

    // 存储聊天记录
    public String saveMsg(ChatMsg chatMsg);

    // 批量签收消息
    public void updateMsgSigned(List<String> msgIdList);

}
