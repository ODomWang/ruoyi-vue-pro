package cn.iocoder.yudao.module.system.dal.mysql.typochecklist;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo.TypoChecklistPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.typochecklist.TypoChecklistDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 错词检查 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface TypoChecklistMapper extends BaseMapperX<TypoChecklistDO> {

    default PageResult<TypoChecklistDO> selectPage(TypoChecklistPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TypoChecklistDO>()
                .eqIfPresent(TypoChecklistDO::getTypo, reqVO.getTypo())
                .eqIfPresent(TypoChecklistDO::getCorrection, reqVO.getCorrection())
                .eqIfPresent(TypoChecklistDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(TypoChecklistDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(TypoChecklistDO::getSpiderUrl, reqVO.getSpiderUrl())
                .eqIfPresent(TypoChecklistDO::getColorType, reqVO.getColorType())
                .orderByDesc(TypoChecklistDO::getId));
    }

}