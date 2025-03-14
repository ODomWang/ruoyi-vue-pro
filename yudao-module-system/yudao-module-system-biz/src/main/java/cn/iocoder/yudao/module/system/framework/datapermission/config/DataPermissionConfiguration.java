package cn.iocoder.yudao.module.system.framework.datapermission.config;

import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import cn.iocoder.yudao.module.system.dal.dataobject.customerauditlog.CustomerAuditLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.detailcheckauditinfo.DetailCheckAuditInfoDO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangeinfo.UrlChangeInfoDO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlpinginfo.UrlPingInfoDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.model.spider.WenxunSpiderSourceConfigDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的数据权限 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class DataPermissionConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer sysDeptDataPermissionRuleCustomizer() {
        return rule -> {
            // dept
            rule.addDeptColumn(AdminUserDO.class);
            rule.addDeptColumn(DeptDO.class, "id");
            // user
            rule.addUserColumn(AdminUserDO.class, "id");
            // 数据权限控制
            rule.addDeptColumn(DetailCheckAuditInfoDO.class, "dept_id");
            rule.addDeptColumn(WenxunSpiderSourceConfigDO.class, "dept_id");
            rule.addDeptColumn(CustomerAuditLogDO.class, "dept_id");
            rule.addDeptColumn(UrlPingInfoDO.class, "dept_id");
            rule.addDeptColumn(UrlChangeInfoDO.class, "dept_id");


        };
    }

}
