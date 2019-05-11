package org.linux.encrypted_im.dao;

import org.linux.encrypted_im.entity.vo.FriendRequestVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersMapperCustom {

    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
}