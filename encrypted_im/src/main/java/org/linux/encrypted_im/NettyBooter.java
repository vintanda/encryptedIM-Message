package org.linux.encrypted_im;

import org.linux.encrypted_im.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * ContextRefreshedEvent为初始化完毕事件
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 当一个ApplicationContext被初始化或刷新触发
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // root application context 没有parent，他就是老大
        if (event.getApplicationContext().getParent() == null) {
            try {
                // 当spring容器初始化完成后就会执行
                WSServer.getInstance().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
