package org.linux.encrypted_im.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyFriendsVO {
    private String friendUserId;
    private String friendUsername;
    private String friendFaceImage;
    private String friendNickname;
}
