package cn.iocoder.yudao.module.wenxun.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WxUserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxUserMapper extends BaseMapperX<WxUserDO> {

    default WxUserDO selectByOpenid(String openid) {
        return selectOne(WxUserDO::getOpenid, openid);
    }

} 