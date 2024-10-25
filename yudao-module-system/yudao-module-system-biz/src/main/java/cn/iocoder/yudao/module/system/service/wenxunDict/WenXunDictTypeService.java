package cn.iocoder.yudao.module.system.service.wenxunDict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypeSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictTypeDO;

import java.util.List;

/**
 * 字典类型 Service 接口
 *
 * @author 芋道源码
 */
public interface  WenXunDictTypeService {

    /**
     * 创建字典类型
     *
     * @param createReqVO 字典类型信息
     * @return 字典类型编号
     */
    Long createDictType( WenXunDictTypeSaveReqVO createReqVO);

    /**
     * 更新字典类型
     *
     * @param updateReqVO 字典类型信息
     */
    void updateDictType( WenXunDictTypeSaveReqVO updateReqVO);

    /**
     * 删除字典类型
     *
     * @param id 字典类型编号
     */
    void deleteDictType(Long id);

    /**
     * 获得字典类型分页列表
     *
     * @param pageReqVO 分页请求
     * @return 字典类型分页列表
     */
    PageResult<WenXunDictTypeDO> getDictTypePage(WenXunDictTypePageReqVO pageReqVO);

    /**
     * 获得字典类型详情
     *
     * @param id 字典类型编号
     * @return 字典类型
     */
    WenXunDictTypeDO getDictType(Long id);

    /**
     * 获得字典类型详情
     *
     * @param type 字典类型
     * @return 字典类型详情
     */
    WenXunDictTypeDO getDictType(String type);

    /**
     * 获得全部字典类型列表
     *
     * @return 字典类型列表
     */
    List<WenXunDictTypeDO> getDictTypeList();

}
