package org.linux.encrypted_im.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linux.encrypted_im.entity.MyFriends;
import org.linux.encrypted_im.entity.MyFriendsExample;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFriendsMapper {
    int countByExample(MyFriendsExample example);

    int deleteByExample(MyFriendsExample example);

    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    List<MyFriends> selectByExample(MyFriendsExample example);

    MyFriends selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MyFriends record, @Param("example") MyFriendsExample example);

    int updateByExample(@Param("record") MyFriends record, @Param("example") MyFriendsExample example);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);
}