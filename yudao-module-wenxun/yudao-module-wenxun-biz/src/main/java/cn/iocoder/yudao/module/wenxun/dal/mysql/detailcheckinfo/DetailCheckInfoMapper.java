package cn.iocoder.yudao.module.wenxun.dal.mysql.detailcheckinfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.ArrayUtils;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoPageReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.auditlog.AuditLogDO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 详情检测信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DetailCheckInfoMapper extends BaseMapperX<DetailCheckInfoDO> {

    default PageResult<DetailCheckInfoDO> selectPage(DetailCheckInfoPageReqVO reqVO) {
        MPJLambdaWrapper<DetailCheckInfoDO> wrapper = new MPJLambdaWrapper<DetailCheckInfoDO>()
                .select(DetailCheckInfoDO::getId,
                        DetailCheckInfoDO::getCheckDetail,
                        DetailCheckInfoDO::getTargetDetail,
                        DetailCheckInfoDO::getCheckSource,
                        DetailCheckInfoDO::getSourceUrl,
                        DetailCheckInfoDO::getSpiderConfigId,
                        DetailCheckInfoDO::getWebIcon,
                        DetailCheckInfoDO::getCreateTime,
                        DetailCheckInfoDO::getUpdateTime,
                        DetailCheckInfoDO::getTitleDesc
                )
                .select(AuditLogDO::getStatus)
                .innerJoin(AuditLogDO.class, AuditLogDO::getSpiderId, DetailCheckInfoDO::getId)
                .eqIfExists(DetailCheckInfoDO::getCheckSource, reqVO.getCheckSource())
                .eqIfExists(DetailCheckInfoDO::getCheckDetail, reqVO.getCheckDetail())
                .eqIfExists(DetailCheckInfoDO::getTargetDetail, reqVO.getTargetDetail())
                .eqIfExists(AuditLogDO::getStatus, reqVO.getStatus()) // 审核状态
                .orderByDesc(DetailCheckInfoDO::getId);
        // 如果提供了时间范围，则添加时间条件
        Object val1 = ArrayUtils.get(reqVO.getCreateTime(), 0);
        LocalDateTime val2 = ArrayUtils.get(reqVO.getCreateTime(), 1);
        if (val1 != null) {
            wrapper.ge(AuditLogDO::getCreateTime, val1);
        }
        if (val2 != null) {
            wrapper.le(AuditLogDO::getCreateTime, val2);
        }
        if (0 != reqVO.getSpiderConfigId()) {
            wrapper.eqIfExists(DetailCheckInfoDO::getSpiderConfigId, reqVO.getSpiderConfigId());
        }

        return selectJoinPage(reqVO, DetailCheckInfoDO.class, wrapper);

    }

    /**
     * 查询详情检测信息列表
     *
     * @return
     */
    default List<DetailCheckInfoDO> selectJoinList() {
        return selectJoinList(DetailCheckInfoDO.class,
                new MPJLambdaWrapper<DetailCheckInfoDO>()
                        .selectAll(DetailCheckInfoDO.class)
                        .leftJoin(AuditLogDO.class, AuditLogDO::getSpiderId, DetailCheckInfoDO::getId)
                        .isNull(AuditLogDO::getId));
    }

}