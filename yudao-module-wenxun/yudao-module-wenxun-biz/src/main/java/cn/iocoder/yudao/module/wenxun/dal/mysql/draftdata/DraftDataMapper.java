package cn.iocoder.yudao.module.wenxun.dal.mysql.draftdata;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.draftdata.DraftDataDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.*;

/**
 * 文巡-在线检测草稿 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DraftDataMapper extends BaseMapperX<DraftDataDO> {

    default PageResult<DraftDataDO> selectPage(DraftDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DraftDataDO>()
                .eqIfPresent(DraftDataDO::getContent, reqVO.getContent())
                .eqIfPresent(DraftDataDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(DraftDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DraftDataDO::getId));
    }

}