package org.linux.encrypted_im.controller;

import org.apache.commons.lang3.StringUtils;
import org.linux.encrypted_im.entity.ChatMsg;
import org.linux.encrypted_im.entity.bo.UserBO;
import org.linux.encrypted_im.entity.Users;
import org.linux.encrypted_im.entity.vo.MyFriendsVO;
import org.linux.encrypted_im.enums.OperatorFriendRequestTypeEnum;
import org.linux.encrypted_im.enums.SearchFriendsStatusEnum;
import org.linux.encrypted_im.service.UserService;
import org.linux.encrypted_im.utils.JSONResult;
import org.linux.encrypted_im.utils.MD5Utils;
import org.linux.encrypted_im.entity.vo.UsersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("u")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @description 登录注册
     */
    @RequestMapping("/registOrLogin")
    public JSONResult registOrLogin(@RequestBody Users user) throws NoSuchAlgorithmException {

        System.out.println("registOrLogin");
        // 0.判断用户名/密码不能为空
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名或密码不能为空...");
        }

        // 1.判断用户名是否存在，存在 --> 登录， 不存在 --> 注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        Users userResult = null;
        if (usernameIsExist) {
            // 1.1 login
            System.out.println("login...");

            userResult = userService.queryUserForLogin(user.getUsername(),
                    MD5Utils.getMD5Str(user.getPassword()));

            if (userResult == null) {
                return JSONResult.errorMsg("用户名或密码不正确...");
            }
        } else {
            // 1.2 regist
            System.out.println("regist...");

            // 更改user的密码为MD5格式
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));

            // 其余字段在service中实现
            userResult = userService.saveUser(user);

        }

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);

        return JSONResult.ok(usersVO);
    }

    /**
     * @description 修改昵称
     */
    @RequestMapping("/setNickname")
    public JSONResult setNickname(@RequestBody UserBO userBO) throws Exception {
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        Users result = userService.updateUserInfo(user);

        return JSONResult.ok(result);
    }

    /**
     * @description 搜索好友：根据账号做匹配查询而不是做模糊查询
     */
    @RequestMapping("/search")
    public JSONResult searchUser(String myUserId, String friendUsername) throws Exception {
        // 0.判断 myUserId 和 friendUsername 都不为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)) {
            return JSONResult.errorMsg("");
        }

        /*
        前置条件：
            1.搜索的用户不存在，返回[查无此人]
            2.搜索的账户是你自己，返回[这是你自己啊]
            3.搜索的用户已是你的好友，返回[该用户已经是你的好友啦]
         */
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);

        // 成功
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            Users friend = userService.queryUserInfoByUsername(friendUsername);
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(friend, usersVO);

            return JSONResult.ok(usersVO);
        } else {
            // 不成功
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return JSONResult.errorMsg(errorMsg);
        }

    }

    /**
     * @description: 添加好友
     */
    @RequestMapping("/addFriendRequest")
    public JSONResult addFriendRequest(String myUserId, String friendUsername) {

        // 0.判断 myUserId 和 friendUsername 都不为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)) {
            return JSONResult.errorMsg("");
        }

        /*
        前置条件：
            1.搜索的用户不存在，返回[查无此人]
            2.搜索的账户是你自己，返回[这是你自己啊]
            3.搜索的用户已是你的好友，返回[该用户已经是你的好友啦]
         */
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);

        // 成功
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            userService.sendFriendRequest(myUserId, friendUsername);
        } else {
            // 不成功
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return JSONResult.errorMsg(errorMsg);
        }

        return JSONResult.ok();
    }

    /**
     * @description: 查询收到的好友申请
     */
    @RequestMapping("/queryFriendRequest")
    public JSONResult queryFriendRequest(String userId) {

        System.out.println("查询好友请求列表.....");

        // 0.判断 userId 不能为空
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }

        // 1.查询用户收到的好友申请
        return JSONResult.ok(userService.queryFriendRequestList(userId));

    }

    /**
     * @description: 对收到的好友请求进行处理：忽略/通过
     */
    @RequestMapping("/operFriendRequest")
    public JSONResult operFriendRequest(String acceptUserId, String sendUserId, Integer operType) {

        // 0. 判断 acceptUserId sendUserId 不能为空
        if (StringUtils.isBlank(acceptUserId) || StringUtils.isBlank(sendUserId) || operType == null) {
            return JSONResult.errorMsg("");
        }

        // 1. 如果 operType 没有对应的枚举值 则直接抛出空错误信息
        if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
            return JSONResult.errorMsg("");
        }

        // 2. 判断是忽略/通过
        if (operType == OperatorFriendRequestTypeEnum.IGNORE.type) {
            // 2.1 是忽略 --- 删除好友请求的数据库表记录
            userService.deleteFriendRequest(sendUserId, acceptUserId);
        } else if (operType == OperatorFriendRequestTypeEnum.PASS.type) {
            // 2.2 是通过 --- 增加好友记录到数据库friend表 并 删除好友请求记录的表
            userService.passFriendRequest(sendUserId, acceptUserId);
        }

        // 4.数据库查询好友列表
        List<MyFriendsVO> myFriends = userService.queryMyFriends(acceptUserId);

        return JSONResult.ok(myFriends);
    }

    /**
     * @description 查询我的好友列表
     */
    @RequestMapping("/myFriends")
    public JSONResult myFriends(String userId) throws Exception {

        System.out.println("查询好友列表");
        // 0. 判断userId不能为空
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }

        // 1.数据库查询好友列表
        List<MyFriendsVO> myFriends = userService.queryMyFriends(userId);

        return JSONResult.ok(myFriends);
    }

    /**
     * @description: 用户获取未读消息列表
     */
    @RequestMapping("/getUnReadMsgList")
    public JSONResult getUnReadMsgList(String acceptUserId) {

        // 0.判断 userId 不能为空
        if (StringUtils.isBlank(acceptUserId)) {
            return JSONResult.errorMsg("");
        }

        // 查询列表
        List<ChatMsg> unReadMsgList = userService.getUnReadMsgList(acceptUserId);

        return JSONResult.ok(unReadMsgList);

    }
}
