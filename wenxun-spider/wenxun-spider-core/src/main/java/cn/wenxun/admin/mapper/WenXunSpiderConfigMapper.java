package cn.wenxun.admin.mapper;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface WenXunSpiderConfigMapper extends BaseMapperX<WenxunSpiderSourceConfigDO> {
     default PageResult<WenxunSpiderSourceConfigDO> selectByDebtCode(Set<Long> deptCode, PageParam pageReqVO) {
        return selectPage(pageReqVO,new QueryWrapperX<WenxunSpiderSourceConfigDO>()
                .in("dept_id", deptCode).orderByAsc("create_time"));
    }

}

