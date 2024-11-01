package cn.iocoder.yudao.module.system.dal.mysql.wenxunDict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface WenXunDictDataMapper extends BaseMapperX<WenXunDictDataDO> {

    default WenXunDictDataDO selectByDictTypeAndValue(String dictType, String value) {
        return selectOne(WenXunDictDataDO::getDictType, dictType, WenXunDictDataDO::getValue, value);
    }

    default WenXunDictDataDO selectByDictTypeAndLabel(String dictType, String label) {
        return selectOne(WenXunDictDataDO::getDictType, dictType, WenXunDictDataDO::getLabel, label);
    }

    default List<WenXunDictDataDO> selectByDictTypeAndValues(String dictType, Collection<String> values) {
        return selectList(new LambdaQueryWrapper<WenXunDictDataDO>().eq(WenXunDictDataDO::getDictType, dictType)
                .in(WenXunDictDataDO::getValue, values));
    }

    default long selectCountByDictType(String dictType) {
        return selectCount(WenXunDictDataDO::getDictType, dictType);
    }

    default PageResult<WenXunDictDataDO> selectPage(WenXunDictDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WenXunDictDataDO>()
                .likeIfPresent(WenXunDictDataDO::getLabel, reqVO.getLabel())
                .eqIfPresent(WenXunDictDataDO::getDictType, reqVO.getDictType())
                .eqIfPresent(WenXunDictDataDO::getStatus, reqVO.getStatus())
                .orderByDesc(Arrays.asList(WenXunDictDataDO::getDictType, WenXunDictDataDO::getSort)));
    }

    default List<WenXunDictDataDO> selectListByStatusAndDictType(Integer status, String dictType) {
        return selectList(new LambdaQueryWrapperX<WenXunDictDataDO>()
                .eqIfPresent(WenXunDictDataDO::getStatus, status)
                .eqIfPresent(WenXunDictDataDO::getDictType, dictType));
    }

    // 落马官员
    default List<WenXunDictDataDO> selectListDatas(Set<String> datas) {
        return selectList(new LambdaQueryWrapperX<WenXunDictDataDO>()
                .in(WenXunDictDataDO::getValue, datas)
                        .eqIfPresent(WenXunDictDataDO::getStatus, 0));
    }

}
