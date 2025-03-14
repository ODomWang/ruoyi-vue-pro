package cn.iocoder.yudao.module.system.dal.mysql.urlpinglog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.urlpinglog.vo.UrlPingLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlpinglog.UrlPingLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网页连通记录-日志 Mapper
 *
 * @author 文巡智检
 */
@Mapper
public interface UrlPingLogMapper extends BaseMapperX<UrlPingLogDO> {

    default PageResult<UrlPingLogDO> selectPage(UrlPingLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UrlPingLogDO>()
                .likeIfPresent(UrlPingLogDO::getUrlName, reqVO.getUrlName())
                .eqIfPresent(UrlPingLogDO::getPingId, reqVO.getPingId())
                .eqIfPresent(UrlPingLogDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UrlPingLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UrlPingLogDO::getId));
    }

}