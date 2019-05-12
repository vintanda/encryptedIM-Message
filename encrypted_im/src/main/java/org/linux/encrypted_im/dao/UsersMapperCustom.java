package org.linux.encrypted_im.dao;

import org.linux.encrypted_im.entity.vo.FriendRequestVO;
import org.linux.encrypted_im.entity.vo.MyFriendsVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersMapperCustom {

    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    public List<MyFriendsVO> queryMyFriends(String userId);

}