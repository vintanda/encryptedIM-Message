package org.linux.encrypted_im;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linux.encrypted_im.entity.Users;
import org.linux.encrypted_im.service.UserService;
import org.linux.encrypted_im.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncryptedImApplicationTests {

    @Autowired
    UserService service;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testForUsersRegistOrLogin() throws NoSuchAlgorithmException {
        // 查询用户是否存在
        Users user = new Users();
        user.setUsername("1");

        boolean isExist = service.queryUsernameIsExist(user.getUsername());

        System.out.println("用户" + user.getUsername() + "是否存在： " + isExist);

        // 查询用户信息
        user.setUsername("stay");
        user.setPassword(MD5Utils.getMD5Str("19971219y"));
        Users users = service.queryUserForLogin(user.getUsername(), user.getPassword());

        System.out.println(user.toString());
    }

}
