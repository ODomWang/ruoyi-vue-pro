package cn.iocoder.yudao.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 *
 * @author
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${yudao.info.base-package}
@SpringBootApplication(scanBasePackages = {
        "${yudao.info.base-package}.server",
        "${yudao.info.base-package}.module",
        "cn.wenxun.admin",
        "cn.iocoder.yudao"},
        exclude = {org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration.class,
                com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure.class,
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class}
)


public class YudaoServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(YudaoServerApplication.class, args);

    }

}
