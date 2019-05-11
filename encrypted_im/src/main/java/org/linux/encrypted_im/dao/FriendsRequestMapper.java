package org.linux.encrypted_im.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linux.encrypted_im.entity.FriendsRequest;
import org.linux.encrypted_im.entity.FriendsRequestExample;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRequestMapper {
    int countByExample(FriendsRequestExample example);

    int deleteByExample(FriendsRequestExample example);

    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    int insertSelective(FriendsRequest record);

    List<FriendsRequest> selectByExample(FriendsRequestExample example);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") FriendsRequest record, @Param("example") FriendsRequestExample example);

    int updateByExample(@Param("record") FriendsRequest record, @Param("example") FriendsRequestExample example);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);
}