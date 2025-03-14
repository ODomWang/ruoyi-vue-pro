package cn.iocoder.yudao.module.system.dal.mysql.urlchangelog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.controller.admin.urlchangelog.vo.UrlChangeLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangelog.UrlChangeLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网站更新检查-日志 Mapper
 *
 * @author 文巡一哥
 */
@Mapper
public interface UrlChangeLogMapper extends BaseMapperX<UrlChangeLogDO> {

    default PageResult<UrlChangeLogDO> selectPage(UrlChangeLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UrlChangeLogDO>()
                .likeIfPresent(UrlChangeLogDO::getUrlName, reqVO.getUrlName())
                .eqIfPresent(UrlChangeLogDO::getSpiderId, reqVO.getSpiderId())
                .eqIfPresent(UrlChangeLogDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UrlChangeLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(UrlChangeLogDO::getTitle, reqVO.getTitle())
                .eqIfPresent(UrlChangeLogDO::getUrl, reqVO.getUrl())
                .orderByDesc(UrlChangeLogDO::getId));
    }

}