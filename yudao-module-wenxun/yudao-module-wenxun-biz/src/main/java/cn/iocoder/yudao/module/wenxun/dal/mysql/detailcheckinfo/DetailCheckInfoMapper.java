package cn.iocoder.yudao.module.wenxun.dal.mysql.detailcheckinfo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.*;

/**
 * 详情检测信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DetailCheckInfoMapper extends BaseMapperX<DetailCheckInfoDO> {

    default PageResult<DetailCheckInfoDO> selectPage(DetailCheckInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DetailCheckInfoDO>()
                .eqIfPresent(DetailCheckInfoDO::getCheckSource, reqVO.getCheckSource())
                .eqIfPresent(DetailCheckInfoDO::getCheckDetail, reqVO.getCheckDetail())
                .eqIfPresent(DetailCheckInfoDO::getTargetDetail, reqVO.getTargetDetail())
                .eqIfPresent(DetailCheckInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DetailCheckInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DetailCheckInfoDO::getId));
    }

}