package cn.iocoder.yudao.module.wenxun.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WxContactDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WxContactMapper extends BaseMapperX<WxContactDO> {

    default List<WxContactDO> selectListByWxId(String wxId) {
        return selectList(WxContactDO::getWxId, wxId);
    }

    default List<WxContactDO> selectListByWxIdAndAppId(String wxId, String appId) {
        return selectList(new LambdaQueryWrapperX<WxContactDO>()
                .eq(WxContactDO::getWxId, wxId)
                .eq(WxContactDO::getAppId, appId));
    }

    default WxContactDO selectByWxIdAndFriendWxId(String wxId, String friendWxId) {
        return selectOne(new LambdaQueryWrapperX<WxContactDO>()
                .eq(WxContactDO::getWxId, wxId)
                .eq(WxContactDO::getFriendWxId, friendWxId));
    }

    default WxContactDO selectByWxIdAndFriendWxIdAndAppId(String wxId, String friendWxId, String appId) {
        return selectOne(new LambdaQueryWrapperX<WxContactDO>()
                .eq(WxContactDO::getWxId, wxId)
                .eq(WxContactDO::getFriendWxId, friendWxId)
                .eq(WxContactDO::getAppId, appId));
    }
} 