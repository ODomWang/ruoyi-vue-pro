package cn.iocoder.yudao.module.wenxun.service.draftdata;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.DraftDataPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.DraftDataSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.draftdata.DraftDataDO;

import javax.validation.Valid;

/**
 * 文巡-在线检测草稿 Service 接口
 *
 * @author 芋道源码
 */
public interface DraftDataService {

    /**
     * 创建文巡-在线检测草稿
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDraftData(@Valid DraftDataSaveReqVO createReqVO);

    /**
     * 更新文巡-在线检测草稿
     *
     * @param updateReqVO 更新信息
     */
    void updateDraftData(@Valid DraftDataSaveReqVO updateReqVO);

    /**
     * 删除文巡-在线检测草稿
     *
     * @param id 编号
     */
    void deleteDraftData(Long id);

    /**
     * 获得文巡-在线检测草稿
     *
     * @param id 编号
     * @return 文巡-在线检测草稿
     */
    DraftDataDO getDraftData(Long id);

    /**
     * 获得文巡-在线检测草稿分页
     *
     * @param pageReqVO 分页查询
     * @return 文巡-在线检测草稿分页
     */
    PageResult<DraftDataDO> getDraftDataPage(DraftDataPageReqVO pageReqVO);

}