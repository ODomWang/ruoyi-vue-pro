package cn.iocoder.yudao.module.infra.dal.mysql.document;

import org.apache.ibatis.annotations.Mapper;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentShareDO;

/**
 * 文档分享 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DocumentShareMapper extends BaseMapperX<DocumentShareDO> {

    default DocumentShareDO selectByShareId(String shareId) {
        return selectOne(new LambdaQueryWrapperX<DocumentShareDO>()
                .eq(DocumentShareDO::getShareId, shareId));
    }

    default PageResult<DocumentShareDO> selectPage(PageParam pageParam, Long userId) {
        return selectPage(pageParam, new LambdaQueryWrapperX<DocumentShareDO>()
                .eqIfPresent(DocumentShareDO::getUserId, userId)
                .orderByDesc(DocumentShareDO::getId));
    }
} 