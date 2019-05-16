package org.linux.encrypted_im.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataContent implements Serializable {

    private static final long serialVersionUID = 3621994768191754022L;

    // 动作类型
    private Integer action;

    // 聊天内容
    private ChatMsg chatMsg;

    // 扩展字段
    private String extand;
}
