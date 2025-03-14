package cn.iocoder.yudao.module.system.service.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import cn.iocoder.yudao.module.system.mapper.WenXunSpiderConfigMapper;
import cn.iocoder.yudao.module.system.model.spider.SpiderXpathConfigDO;
import cn.iocoder.yudao.module.system.model.spider.WenxunSpiderSourceConfigDO;
import cn.iocoder.yudao.module.system.service.WenXunSpiderConfigService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Service
@Validated
public class WenXunSpiderConfigServiceImpl implements WenXunSpiderConfigService {

    @Resource
    private WenXunSpiderConfigMapper wenXunSpiderConfigMapper;
    @Resource
    private AdminUserService adminUserService;


    @Override
    public Long createDataSourceConfig(SpiderXpathConfigDO createReqVO) {
        if (StringUtils.isNotEmpty(createReqVO.getSpiderUrl())) {
            WenxunSpiderSourceConfigDO sourceConfigDO = wenXunSpiderConfigMapper.selectOne(WenxunSpiderSourceConfigDO::getSpiderUrl, createReqVO.getSpiderUrl());
            if (sourceConfigDO != null) {
                sourceConfigDO.setUpdater(SecurityFrameworkUtils.getLoginUserNickname());
                sourceConfigDO.setBodyXpath(createReqVO.getBodyXpath());
                sourceConfigDO.setNextPageXpath(createReqVO.getNextPageXpath());
                sourceConfigDO.setListXpath(createReqVO.getListXpath());
                sourceConfigDO.setTitleXpath(createReqVO.getTitleXpath());
                sourceConfigDO.setDateXpath(createReqVO.getDateXpath());
                sourceConfigDO.setDescXpath(createReqVO.getDescXpath());
                sourceConfigDO.setItemXpath(createReqVO.getItemXpath());
                sourceConfigDO.setSpiderPageNum(createReqVO.getSpiderPageNum());
                sourceConfigDO.setSpiderModel(createReqVO.getSpiderModel());
                sourceConfigDO.setSpiderName(createReqVO.getSpiderName());
                sourceConfigDO.setRemark(createReqVO.getRemark());
                boolean i = wenXunSpiderConfigMapper.insertOrUpdate(sourceConfigDO);
                if (i) {
                    return 1L;
                }
            } else {
                sourceConfigDO = WenxunSpiderSourceConfigDO.builder()
                        .spiderUrl(createReqVO.getSpiderUrl())
                        .spiderName(createReqVO.getSpiderName())
                        .spiderModel(createReqVO.getSpiderModel())
                        .remark(createReqVO.getRemark())
                        .spiderPageNum(createReqVO.getSpiderPageNum())
                        .bodyXpath(createReqVO.getBodyXpath())
                        .nextPageXpath(createReqVO.getNextPageXpath())
                        .listXpath(createReqVO.getListXpath())
                        .titleXpath(createReqVO.getTitleXpath())
                        .dateXpath(createReqVO.getDateXpath())
                        .descXpath(createReqVO.getDescXpath())
                        .pingStatus(1L)

                        .status(1L)
                        .itemXpath(createReqVO.getItemXpath())
                        .deptId(createReqVO.getDeptId())
                        .creator(SecurityFrameworkUtils.getLoginUserNickname())
                        .updater(SecurityFrameworkUtils.getLoginUserNickname())
                        .build();
                boolean i = wenXunSpiderConfigMapper.insertOrUpdate(sourceConfigDO);
                if (i) {
                    return 1L;
                }
            }

        }

        return 0L;
    }

    @Override
    public void updateDataSourceConfig(WenxunSpiderSourceConfigDO updateReqVO) {
        TenantContextHolder.setIgnore(true);
        wenXunSpiderConfigMapper.updateById(updateReqVO);
    }

    @Override
    public void deleteDataSourceConfig(String id) {

    }

    @Override
    public WenxunSpiderSourceConfigDO getDataSourceConfig(Long id) {
        TenantContextHolder.setIgnore(true);
        return wenXunSpiderConfigMapper.selectById(id);
    }

    /**
     * 获取爬虫配置列表
     *
     * @param
     * @return
     */
    @Override
    public PageResult<WenxunSpiderSourceConfigDO> getDataSourceConfigList(PageParam pageReqVO) {

        //获取所有用户下级


        PageResult<WenxunSpiderSourceConfigDO> result = wenXunSpiderConfigMapper.selectByDebtCode(pageReqVO);

        return result;
    }

    @Override
    public List<WenxunSpiderSourceConfigDO> getAllUrlConfigInfo() {
        TenantContextHolder.setIgnore(true);
        return wenXunSpiderConfigMapper.selectByDataStatus();
    }
}
