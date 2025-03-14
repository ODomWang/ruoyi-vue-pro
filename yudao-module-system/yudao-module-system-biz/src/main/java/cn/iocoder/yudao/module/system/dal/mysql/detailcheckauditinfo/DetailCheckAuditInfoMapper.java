package cn.iocoder.yudao.module.system.dal.mysql.detailcheckauditinfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.ArrayUtils;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoPageReqVO;
 import cn.iocoder.yudao.module.system.dal.dataobject.customerauditlog.CustomerAuditLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.detailcheckauditinfo.DetailCheckAuditInfoDO;
 import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 详情检测信息表-用户审核 Mapper
 *
 * @author 文巡智检
 */
@Mapper
public interface DetailCheckAuditInfoMapper extends BaseMapperX<DetailCheckAuditInfoDO> {

    default PageResult<DetailCheckAuditInfoDO> selectPage(DetailCheckAuditInfoPageReqVO reqVO) {

        MPJLambdaWrapper<DetailCheckAuditInfoDO> wrapper = new MPJLambdaWrapper<DetailCheckAuditInfoDO>()
                .select(DetailCheckAuditInfoDO::getId,
                        DetailCheckAuditInfoDO::getCheckDetail,
                        DetailCheckAuditInfoDO::getTargetDetail,
                        DetailCheckAuditInfoDO::getCheckSource,
                        DetailCheckAuditInfoDO::getSourceUrl,
                        DetailCheckAuditInfoDO::getSpiderConfigId,
                        DetailCheckAuditInfoDO::getWebIcon,
                        DetailCheckAuditInfoDO::getCreateTime,
                        DetailCheckAuditInfoDO::getUpdateTime,
                        DetailCheckAuditInfoDO::getTitleDesc
                )
                .select(CustomerAuditLogDO::getStatus)
                .innerJoin(CustomerAuditLogDO.class, CustomerAuditLogDO::getSpiderId, DetailCheckAuditInfoDO::getId)
                .eqIfExists(DetailCheckAuditInfoDO::getCheckSource, reqVO.getCheckSource())
                .eqIfExists(DetailCheckAuditInfoDO::getCheckDetail, reqVO.getCheckDetail())
                .eqIfExists(DetailCheckAuditInfoDO::getTargetDetail, reqVO.getTargetDetail())
                .eqIfExists(CustomerAuditLogDO::getStatus, reqVO.getStatus()) // 审核状态
                .eqIfExists(DetailCheckAuditInfoDO::getSpiderConfigId, reqVO.getSpiderConfigId())
                .orderByDesc(DetailCheckAuditInfoDO::getId);
        // 如果提供了时间范围，则添加时间条件

        // 如果提供了时间范围，则添加时间条件
        if (StringUtils.isNotEmpty(reqVO.getCreateTime())) {
            if (reqVO.getCreateTime().equals("custom")) {
                Object val1 = ArrayUtils.get(reqVO.getTimeRange(), 0);
                LocalDateTime val2 = ArrayUtils.get(reqVO.getTimeRange(), 1);
                if (val1 != null) {
                    wrapper.ge(CustomerAuditLogDO::getCreateTime, val1);
                }
                if (val2 != null) {
                    wrapper.le(CustomerAuditLogDO::getCreateTime, val2);
                }
            }
            if (reqVO.getCreateTime().equals("3d")) {
                wrapper.ge(CustomerAuditLogDO::getCreateTime, LocalDateTime.now().minusDays(3));
            } else if (reqVO.getCreateTime().equals("7d")) {
                wrapper.ge(CustomerAuditLogDO::getCreateTime, LocalDateTime.now().minusDays(7));
            } else if (reqVO.getCreateTime().equals("15d")) {
                wrapper.ge(CustomerAuditLogDO::getCreateTime, LocalDateTime.now().minusDays(15));
            }
        }

        return selectJoinPage(reqVO, DetailCheckAuditInfoDO.class, wrapper);

    }

    /**
     * 查询详情检测信息列表
     *
     * @return
     */
    default List<DetailCheckAuditInfoDO> selectJoinList() {
        return selectJoinList(DetailCheckAuditInfoDO.class,
                new MPJLambdaWrapper<DetailCheckAuditInfoDO>()
                        .selectAll(DetailCheckAuditInfoDO.class)
                        .leftJoin(CustomerAuditLogDO.class, CustomerAuditLogDO::getSpiderId, DetailCheckAuditInfoDO::getId)
                        .isNull(CustomerAuditLogDO::getId)
                        .ne(CustomerAuditLogDO::getStatus, CustomerAuditLogDO::getStatus)
        );
    }

}