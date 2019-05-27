package org.linux.encrypted_im.service.impl;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.linux.encrypted_im.dao.*;
import org.linux.encrypted_im.encryptedUtils.AESUtil;
import org.linux.encrypted_im.entity.*;
import org.linux.encrypted_im.entity.vo.FriendRequestVO;
import org.linux.encrypted_im.entity.vo.MyFriendsVO;
import org.linux.encrypted_im.enums.MsgActionEnum;
import org.linux.encrypted_im.enums.MsgSignFlagEnum;
import org.linux.encrypted_im.enums.OperatorFriendRequestTypeEnum;
import org.linux.encrypted_im.enums.SearchFriendsStatusEnum;
import org.linux.encrypted_im.idworker.Sid;
import org.linux.encrypted_im.netty.ChatMsg;
import org.linux.encrypted_im.netty.DataContent;
import org.linux.encrypted_im.netty.UserChannelRel;
import org.linux.encrypted_im.service.UserService;
import org.linux.encrypted_im.utils.JSONResult;
import org.linux.encrypted_im.utils.JSONUtils;
import org.linux.encrypted_im.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersMapperCustom usersMapperCustom;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    /**
     * 查询用户名是否存在：
     *   存在 --- true
     *   不存在 --- false
     * @param username
     * @return
     */
    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(username);

        List<Users> usersList =  usersMapper.selectByExample(example);

        return usersList != null && usersList.size() != 0;
    }

    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String pwd) {

        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(pwd);

        List<Users> usersList = usersMapper.selectByExample(example);

        if (usersList != null && usersList.size() != 0) {
            return usersList.get(0);
        }

        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users user) {

        String userId = sid.nextShort();

        user.setNickname(user.getUsername());
        user.setFaceImageBig("");
        user.setFaceImage("");

        // 为每个用户生成一个唯一的二维码
        String qrCodePath = "C://user" + userId + "qrcode.png";
        // message_qrcode:[username]
//        qrCodeUtils.createQRCode(qrCodePath, "message_qrcode:" + user.getUsername());
        user.setQrcode("");
        user.setId(userId);

        usersMapper.insert(user);

        return user;
    }

    @Override
    public Users updateUserInfo(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Users queryUserById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {

        Users friend = queryUserInfoByUsername(friendUsername);

        // 1.搜索的用户不存在，返回[查无此人]
        if (friend == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }

        // 2.搜索的账户是你自己，返回[这是你自己啊]
        if (friend.getId().equals(myUserId)) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }

        // 3.搜索的用户已是你的好友，返回[该用户已经是你的好友啦]
        MyFriendsExample example = new MyFriendsExample();
        MyFriendsExample.Criteria criteria = example.createCriteria();

        criteria.andIdEqualTo(myUserId);
        criteria.andMyFriendUserIdEqualTo(friend.getId());

        List<MyFriends> myFriends = myFriendsMapper.selectByExample(example);

        if (myFriends != null && myFriends.size() != 0) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfoByUsername(String username) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(username);

        List<Users> usersList = usersMapper.selectByExample(example);

        if (usersList != null && usersList.size() != 0) {
            return usersList.get(0);
        }

        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {

        // 根据用户名把朋友信息查询出来
        Users friend = queryUserInfoByUsername(friendUsername);

        // 1. 查询好友请求记录表
        FriendsRequestExample fre = new FriendsRequestExample();
        FriendsRequestExample.Criteria frec = fre.createCriteria();
        frec.andAcceptUserIdEqualTo(friend.getId());
        frec.andSendUserIdEqualTo(myUserId);
        List<FriendsRequest> friendsRequests = friendsRequestMapper.selectByExample(fre);
        if (friendsRequests == null || friendsRequests.size() == 0) {
            // 2. 如果不是你的好友 并且没有好友请求记录 创建一条请求
            String requestId = sid.nextShort();

            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDataTime(new Date());

            friendsRequestMapper.insert(request);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return usersMapperCustom.queryFriendRequestList(acceptUserId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {
        FriendsRequestExample fre = new FriendsRequestExample();
        FriendsRequestExample.Criteria frec = fre.createCriteria();
        frec.andSendUserIdEqualTo(sendUserId);
        frec.andAcceptUserIdEqualTo(acceptUserId);
        friendsRequestMapper.deleteByExample(fre);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        // 保存好友
        saveFriends(sendUserId, acceptUserId);

        // 逆向保存好友
        saveFriends(acceptUserId, sendUserId);

        // 删除好友请求记录
        deleteFriendRequest(sendUserId, acceptUserId);

        // 获取发送好友请求用户在线的情况下更新其好友列表
        Channel sendChannel = UserChannelRel.get(sendUserId);
        if (sendChannel != null) {
            // 使用websocket主动推送消息到请求发起者，更新其通讯录列表
            DataContent dataContent = new DataContent();
            dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);

            sendChannel.writeAndFlush(
                    new TextWebSocketFrame(JSONUtils.objectToJson(dataContent)));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void saveFriends(String sendUserId, String acceptUserId) {
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyFriendUserId(acceptUserId);
        myFriends.setMyUserId(sendUserId);
        myFriendsMapper.insert(myFriends);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {

        List<MyFriendsVO> myFriends = usersMapperCustom.queryMyFriends(userId);

        return myFriends;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsg chatMsg) {

        org.linux.encrypted_im.entity.ChatMsg msgDB = new org.linux.encrypted_im.entity.ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);

        String key = msgId + chatMsg.getSenderId();
        System.out.println("AES加密存储KEY：" + key);

        // 聊天记录加密方式 msgId+senderId拼接后的结果作为加密秘钥
        try {
            String encryptedMsg = AESUtil.encrypt(chatMsg.getMsg(), key);
            msgDB.setMsg(encryptedMsg);
            System.out.println("加密存储的聊天消息为：" + encryptedMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        usersMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<org.linux.encrypted_im.entity.ChatMsg> getUnReadMsgList(String acceptUserId) {

        System.out.println("===================== getUnReadMsgList =====================");
        ChatMsgExample example = new ChatMsgExample();
        ChatMsgExample.Criteria criteria = example.createCriteria();
        criteria.andAcceptUserIdEqualTo(acceptUserId);
        criteria.andSignFlagEqualTo(0);

        List<org.linux.encrypted_im.entity.ChatMsg> chatMsgs = chatMsgMapper.selectByExample(example);

        // 解密
        for (int i = 0;i < chatMsgs.size();i++) {
            try {
                org.linux.encrypted_im.entity.ChatMsg chatMsg = chatMsgs.get(i);
                System.out.println("数据库中读出的加密消息内容：" + chatMsg.getMsg());
                String key = chatMsg.getId() + chatMsg.getSendUserId();
                System.out.println("AES解密KEY：" + key);
                String decodeMsg = AESUtil.decrypt(chatMsg.getMsg(),
                                        chatMsg.getId() + chatMsg.getSendUserId());
                System.out.println("decodeMsg：" + decodeMsg);
                chatMsg.setMsg(decodeMsg);
                chatMsgs.set(i, chatMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return chatMsgs;
    }
}
