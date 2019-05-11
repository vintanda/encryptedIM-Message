package org.linux.encrypted_im.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 好友请求方的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestVO {
    private String sendUserId;

    private String sendUsername;

    private String sendNickname;

}
