package cn.iocoder.yudao.module.wenxun.dal.mysql.urlchangeinfo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlchangeinfo.UrlChangeInfoDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wenxun.controller.admin.urlchangeinfo.vo.*;

/**
 * 网站更新检查 Mapper
 *
 * @author 文巡一哥
 */
@Mapper
public interface UrlChangeInfoMapper extends BaseMapperX<UrlChangeInfoDO> {

    default PageResult<UrlChangeInfoDO> selectPage(UrlChangeInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UrlChangeInfoDO>()
                .likeIfPresent(UrlChangeInfoDO::getUrlName, reqVO.getUrlName())
                .eqIfPresent(UrlChangeInfoDO::getAllCount, reqVO.getAllCount())
                .eqIfPresent(UrlChangeInfoDO::getSuccessCount, reqVO.getSuccessCount())
                .eqIfPresent(UrlChangeInfoDO::getFailCount, reqVO.getFailCount())
                .betweenIfPresent(UrlChangeInfoDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(UrlChangeInfoDO::getUrl, reqVO.getUrl())
                .eqIfPresent(UrlChangeInfoDO::getLastTitle, reqVO.getLastTitle())
                .orderByDesc(UrlChangeInfoDO::getId));
    }

}