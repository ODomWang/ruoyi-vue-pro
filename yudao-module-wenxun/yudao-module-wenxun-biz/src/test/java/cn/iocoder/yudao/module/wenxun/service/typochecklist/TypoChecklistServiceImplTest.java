package cn.iocoder.yudao.module.wenxun.service.typochecklist;

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

    @Resource
    private TypoChecklistServiceImpl typoChecklistService;

    @Resource
    private TypoChecklistMapper typoChecklistMapper;

    @Test
    public void testCreateTypoChecklist_success() {
        // 准备参数
        TypoChecklistSaveReqVO createReqVO = randomPojo(TypoChecklistSaveReqVO.class).setId(null);

        // 调用
        Long typoChecklistId = typoChecklistService.createTypoChecklist(createReqVO);
        // 断言
        assertNotNull(typoChecklistId);
        // 校验记录的属性是否正确
        TypoChecklistDO typoChecklist = typoChecklistMapper.selectById(typoChecklistId);
        assertPojoEquals(createReqVO, typoChecklist, "id");
    }

    @Test
    public void testUpdateTypoChecklist_success() {
        // mock 数据
        TypoChecklistDO dbTypoChecklist = randomPojo(TypoChecklistDO.class);
        typoChecklistMapper.insert(dbTypoChecklist);// @Sql: 先插入出一条存在的数据
        // 准备参数
        TypoChecklistSaveReqVO updateReqVO = randomPojo(TypoChecklistSaveReqVO.class, o -> {
            o.setId(dbTypoChecklist.getId()); // 设置更新的 ID
        });

        // 调用
        typoChecklistService.updateTypoChecklist(updateReqVO);
        // 校验是否更新正确
        TypoChecklistDO typoChecklist = typoChecklistMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, typoChecklist);
    }

    @Test
    public void testUpdateTypoChecklist_notExists() {
        // 准备参数
        TypoChecklistSaveReqVO updateReqVO = randomPojo(TypoChecklistSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> typoChecklistService.updateTypoChecklist(updateReqVO), TYPO_CHECKLIST_NOT_EXISTS);
    }

    @Test
    public void testDeleteTypoChecklist_success() {
        // mock 数据
        TypoChecklistDO dbTypoChecklist = randomPojo(TypoChecklistDO.class);
        typoChecklistMapper.insert(dbTypoChecklist);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbTypoChecklist.getId();

        // 调用
        typoChecklistService.deleteTypoChecklist(id);
       // 校验数据不存在了
       assertNull(typoChecklistMapper.selectById(id));
    }

    @Test
    public void testDeleteTypoChecklist_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> typoChecklistService.deleteTypoChecklist(id), TYPO_CHECKLIST_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetTypoChecklistPage() {
       // mock 数据
       TypoChecklistDO dbTypoChecklist = randomPojo(TypoChecklistDO.class, o -> { // 等会查询到
           o.setTypo(null);
           o.setCorrection(null);
           o.setStatus(null);
           o.setCreateTime(null);
           o.setSpiderUrl(null);
           o.setColorType(null);
       });
       typoChecklistMapper.insert(dbTypoChecklist);
       // 测试 typo 不匹配
       typoChecklistMapper.insert(cloneIgnoreId(dbTypoChecklist, o -> o.setTypo(null)));
       // 测试 correction 不匹配
       typoChecklistMapper.insert(cloneIgnoreId(dbTypoChecklist, o -> o.setCorrection(null)));
       // 测试 status 不匹配
       typoChecklistMapper.insert(cloneIgnoreId(dbTypoChecklist, o -> o.setStatus(null)));
       // 测试 createTime 不匹配
       typoChecklistMapper.insert(cloneIgnoreId(dbTypoChecklist, o -> o.setCreateTime(null)));
       // 测试 spiderUrl 不匹配
       typoChecklistMapper.insert(cloneIgnoreId(dbTypoChecklist, o -> o.setSpiderUrl(null)));
       // 测试 colorType 不匹配
       typoChecklistMapper.insert(cloneIgnoreId(dbTypoChecklist, o -> o.setColorType(null)));
       // 准备参数
       TypoChecklistPageReqVO reqVO = new TypoChecklistPageReqVO();
       reqVO.setTypo(null);
       reqVO.setCorrection(null);
       reqVO.setStatus(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setSpiderUrl(null);
       reqVO.setColorType(null);

       // 调用
       PageResult<TypoChecklistDO> pageResult = typoChecklistService.getTypoChecklistPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbTypoChecklist, pageResult.getList().get(0));
    }

}