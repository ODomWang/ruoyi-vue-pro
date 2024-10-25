package cn.iocoder.yudao.module.wenxun.service.typochecklist;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.typochecklist.vo.TypoChecklistPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.typochecklist.vo.TypoChecklistSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.typochecklist.TypoChecklistDO;

import javax.validation.Valid;

/**
 * 错词检查 Service 接口
 *
 * @author 芋道源码
 */
public interface TypoChecklistService {

    /**
     * 创建错词检查
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTypoChecklist(@Valid TypoChecklistSaveReqVO createReqVO);

    /**
     * 更新错词检查
     *
     * @param updateReqVO 更新信息
     */
    void updateTypoChecklist(@Valid TypoChecklistSaveReqVO updateReqVO);

    /**
     * 删除错词检查
     *
     * @param id 编号
     */
    void deleteTypoChecklist(Long id);

    /**
     * 获得错词检查
     *
     * @param id 编号
     * @return 错词检查
     */
    TypoChecklistDO getTypoChecklist(Long id);

    /**
     * 获得错词检查分页
     *
     * @param pageReqVO 分页查询
     * @return 错词检查分页
     */
    PageResult<TypoChecklistDO> getTypoChecklistPage(TypoChecklistPageReqVO pageReqVO);

}