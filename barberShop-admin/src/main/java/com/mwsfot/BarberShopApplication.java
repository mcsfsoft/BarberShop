package com.mwsfot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class BarberShopApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(BarberShopApplication.class, args);
        System.out.println(
            "(♥◠‿◠)ﾉﾞ  " + BarberShopApplication.class.getName() + "启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
