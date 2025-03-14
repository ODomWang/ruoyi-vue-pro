package cn.iocoder.yudao.module.system.mapper;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.system.model.spider.WenxunSpiderSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface WenXunSpiderConfigMapper extends BaseMapperX<WenxunSpiderSourceConfigDO> {
    default PageResult<WenxunSpiderSourceConfigDO> selectByDebtCode( PageParam pageReqVO) {
        return selectPage(pageReqVO, new QueryWrapperX<WenxunSpiderSourceConfigDO>().orderByAsc("create_time"));
    }

    default List<WenxunSpiderSourceConfigDO> selectByDataStatus() {
        return selectList(new QueryWrapperX<WenxunSpiderSourceConfigDO>()
                .in("status", 1).orderByAsc("create_time"));
    }

}

