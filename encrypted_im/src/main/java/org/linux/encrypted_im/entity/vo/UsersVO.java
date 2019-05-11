package org.linux.encrypted_im.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersVO {
    private String id;

    private String username;

    private String faceImage;

    private String faceImageBig;

    private String nickname;

    private String qrcode;
}
