package cn.iocoder.yudao.module.wenxun.service.typochecklist;

import cn.hutool.system.SystemUtil;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.system.service.typochecklist.TypoChecklistServiceImpl;
import org.springframework.context.annotation.Import;

/**
 * {@link TypoChecklistServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(TypoChecklistServiceImpl.class)
public class TypoChecklistServiceImplTest extends BaseDbUnitTest {

    public static void main(String[] args) {
        System.out.println(  SystemUtil.getJavaInfo().getVersionInt());
    }
}