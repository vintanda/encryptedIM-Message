package org.linux.encrypted_im.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linux.encrypted_im.entity.Users;
import org.linux.encrypted_im.entity.UsersExample;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper {
    int countByExample(UsersExample example);

    int deleteByExample(UsersExample example);

    int deleteByPrimaryKey(String id);

    int insert(Users record);

    int insertSelective(Users record);

    List<Users> selectByExample(UsersExample example);

    Users selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Users record, @Param("example") UsersExample example);

    int updateByExample(@Param("record") Users record, @Param("example") UsersExample example);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
}