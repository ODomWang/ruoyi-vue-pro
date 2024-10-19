package cn.wenxun.admin.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import cn.wenxun.admin.mapper.WenXunSpiderConfigMapper;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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
    public Long createDataSourceConfig(WenxunSpiderSourceConfigDO createReqVO) {
        return null;
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

        Set<Long> deptIds = adminUserService.getDeptCondition(SecurityFrameworkUtils.getLoginUserDeptId());
        TenantContextHolder.setIgnore(true);
        PageResult<WenxunSpiderSourceConfigDO> result = wenXunSpiderConfigMapper.selectByDebtCode(deptIds, pageReqVO);

        return result;
    }

    @Override
    public List<WenxunSpiderSourceConfigDO> getAllUrlConfigInfo() {
        TenantContextHolder.setIgnore(true);
        return wenXunSpiderConfigMapper.selectByDataStatus();
    }
}
