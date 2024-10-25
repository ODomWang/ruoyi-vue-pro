package cn.iocoder.yudao.module.system.dal.mysql.wenxunDict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypePageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictTypeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface WenXunDictTypeMapper extends BaseMapperX<WenXunDictTypeDO> {

    default PageResult<WenXunDictTypeDO> selectPage(WenXunDictTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WenXunDictTypeDO>()
                .likeIfPresent(WenXunDictTypeDO::getName, reqVO.getName())
                .likeIfPresent(WenXunDictTypeDO::getType, reqVO.getType())
                .eqIfPresent(WenXunDictTypeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WenXunDictTypeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WenXunDictTypeDO::getId));
    }

    default WenXunDictTypeDO selectByType(String type) {
        return selectOne(WenXunDictTypeDO::getType, type);
    }

    default WenXunDictTypeDO selectByName(String name) {
        return selectOne(WenXunDictTypeDO::getName, name);
    }

    @Update("UPDATE wenxun_dict_type SET deleted = 1, deleted_time = #{deletedTime} WHERE id = #{id}")
    void updateToDelete(@Param("id") Long id, @Param("deletedTime") LocalDateTime deletedTime);

}
