package com.wangxy.tienchin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author tienchin
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TienchinApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(TienchinApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  TeinChin健身启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
