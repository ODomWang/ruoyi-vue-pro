package cn.iocoder.yudao.module.wenxun.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WxAccountDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxAccountMapper extends BaseMapperX<WxAccountDO> {

    default WxAccountDO selectByAppId(String appId) {
        return selectOne(WxAccountDO::getAppId, appId);
    }

    default WxAccountDO selectByWxId(String wxId) {
        return selectOne(WxAccountDO::getWxId, wxId);
    }

} 