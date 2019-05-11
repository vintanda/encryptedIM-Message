package org.linux.encrypted_im.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by LZD on 2019/05/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBO {
    private String userId;
    private String nickname;
}
