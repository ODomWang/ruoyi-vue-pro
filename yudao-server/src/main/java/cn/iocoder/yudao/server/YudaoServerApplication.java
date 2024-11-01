package cn.iocoder.yudao.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 项目的启动类
 *
 * @author
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${yudao.info.base-package}
@SpringBootApplication(scanBasePackages = {"${yudao.info.base-package}.server",
        "${yudao.info.base-package}.module","cn.wenxun.admin"})
@MapperScan("cn.wenxun.admin.mapper")
@ComponentScan(basePackages = {"cn.wenxun.admin.job.utils.trie"})
 public class YudaoServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(YudaoServerApplication.class, args);

    }

}
