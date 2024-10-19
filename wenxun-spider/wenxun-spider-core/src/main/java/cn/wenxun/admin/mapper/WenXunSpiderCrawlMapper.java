package cn.wenxun.admin.mapper;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.wenxun.admin.model.spider.WenxunSpiderCrawlDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface WenXunSpiderCrawlMapper extends BaseMapperX<WenxunSpiderCrawlDetail> {
    default PageResult<WenxunSpiderCrawlDetail> selectByDebtCode(Set<Long> deptCode, PageParam pageReqVO) {
        return selectPage(pageReqVO, new QueryWrapperX<WenxunSpiderCrawlDetail>()
                .in("dept_id", deptCode).orderByAsc("create_time"));
    }

    default List<WenxunSpiderCrawlDetail> selectByDataStatus() {
        return selectList(new QueryWrapperX<WenxunSpiderCrawlDetail>()
                .in("status", 1).orderByAsc("create_time"));
    }

}

