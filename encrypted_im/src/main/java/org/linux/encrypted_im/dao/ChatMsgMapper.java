package org.linux.encrypted_im.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linux.encrypted_im.entity.ChatMsg;
import org.linux.encrypted_im.entity.ChatMsgExample;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMsgMapper {
    int countByExample(ChatMsgExample example);

    int deleteByExample(ChatMsgExample example);

    int deleteByPrimaryKey(String id);

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    List<ChatMsg> selectByExample(ChatMsgExample example);

    ChatMsg selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ChatMsg record, @Param("example") ChatMsgExample example);

    int updateByExample(@Param("record") ChatMsg record, @Param("example") ChatMsgExample example);

    int updateByPrimaryKeySelective(ChatMsg record);

    int updateByPrimaryKey(ChatMsg record);
}