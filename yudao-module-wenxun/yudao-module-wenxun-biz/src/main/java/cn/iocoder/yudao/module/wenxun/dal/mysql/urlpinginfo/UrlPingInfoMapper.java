package cn.iocoder.yudao.module.wenxun.dal.mysql.urlpinginfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.wenxun.controller.admin.urlpinginfo.vo.UrlPingInfoPageReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlpinginfo.UrlPingInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网页连通记录 Mapper
 *
 * @author 文巡智检
 */
@Mapper
public interface UrlPingInfoMapper extends BaseMapperX<UrlPingInfoDO> {

    default PageResult<UrlPingInfoDO> selectPage(UrlPingInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UrlPingInfoDO>()
                .likeIfPresent(UrlPingInfoDO::getUrlName, reqVO.getUrlName())
                .eqIfPresent(UrlPingInfoDO::getAllCount, reqVO.getAllCount())
                .eqIfPresent(UrlPingInfoDO::getSuccessCount, reqVO.getSuccessCount())
                .eqIfPresent(UrlPingInfoDO::getFailCount, reqVO.getFailCount())
                .betweenIfPresent(UrlPingInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UrlPingInfoDO::getId));
    }

}