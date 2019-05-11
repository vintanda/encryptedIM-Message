package org.linux.encrypted_im;

import org.linux.encrypted_im.dao.UsersMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
// 扫描mybatis mapper包路径
@MapperScan("org.linux.encrypted_im.dao")
// 扫描所有需要的包
@ComponentScan(basePackages = {"org.linux"})
public class EncryptedImApplication {

    @Autowired
    private UsersMapper usersMapper;

    @GetMapping("/hello")
    public Object hello(){
        return usersMapper.selectByPrimaryKey("1");
    }
    public static void main(String[] args) {
        SpringApplication.run(EncryptedImApplication.class, args);
    }

}
