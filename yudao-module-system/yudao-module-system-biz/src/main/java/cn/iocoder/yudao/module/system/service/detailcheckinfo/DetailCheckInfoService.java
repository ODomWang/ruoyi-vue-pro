package cn.iocoder.yudao.module.system.service.detailcheckinfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoWithDictDataRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 详情检测信息 Service 接口
 *
 * @author 芋道源码
 */
public interface DetailCheckInfoService {

    /**
     * 创建详情检测信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDetailCheckInfo(@Valid DetailCheckInfoSaveReqVO createReqVO);

    /**
     * 更新详情检测信息
     *
     * @param updateReqVO 更新信息
     */
    void updateDetailCheckInfo(@Valid DetailCheckInfoSaveReqVO updateReqVO);

    /**
     * 删除详情检测信息
     *
     * @param id 编号
     */
    void deleteDetailCheckInfo(Long id);

    /**
     * 获得详情检测信息
     *
     * @param id 编号
     * @return 详情检测信息
     */
    DetailCheckInfoWithDictDataRespVO getDetailCheckInfo(Long id);

    /**
     * 获得详情检测信息分页
     *
     * @param pageReqVO 分页查询
     * @return 详情检测信息分页
     */
    PageResult<DetailCheckInfoDO> getDetailCheckInfoPage(DetailCheckInfoPageReqVO pageReqVO);


    /**
     * 获得详情检测信息列表
     *
     * @return
     */
    List<DetailCheckInfoDO> selectJoinList();

}