package cn.iocoder.yudao.module.infra.dal.mysql.document;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentQueryVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文档 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DocumentMapper extends BaseMapperX<DocumentDO> {

    default PageResult<DocumentDO> selectPage(PageParam pageParam, Long parentId, String title, Integer status) {
        return selectPage(pageParam, new LambdaQueryWrapperX<DocumentDO>()
                .eqIfPresent(DocumentDO::getParentId, parentId)
                .likeIfPresent(DocumentDO::getTitle, title)
                .eqIfPresent(DocumentDO::getStatus, status)
                .orderByDesc(DocumentDO::getId));
    }

    default List<DocumentDO> selectList(Long parentId) {
        return selectList(new LambdaQueryWrapperX<DocumentDO>()
                .eq(DocumentDO::getParentId, parentId)
                .orderByDesc(DocumentDO::getId));
    }

    default List<DocumentDO> selectList(DocumentQueryVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DocumentDO>()
                .eqIfPresent(DocumentDO::getParentId, reqVO.getParentId())
                .likeIfPresent(DocumentDO::getTitle, reqVO.getTitle())
                .eqIfPresent(DocumentDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DocumentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DocumentDO::getId));
    }

} 