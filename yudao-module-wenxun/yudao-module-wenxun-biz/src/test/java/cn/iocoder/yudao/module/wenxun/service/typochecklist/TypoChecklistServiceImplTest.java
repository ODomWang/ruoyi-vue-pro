package cn.iocoder.yudao.module.wenxun.service.typochecklist;

import cn.hutool.system.SystemUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.wenxun.controller.admin.typochecklist.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.typochecklist.TypoChecklistDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.typochecklist.TypoChecklistMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.module.wenxun.enums.GlobalErrorCodeConstants.TYPO_CHECKLIST_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

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