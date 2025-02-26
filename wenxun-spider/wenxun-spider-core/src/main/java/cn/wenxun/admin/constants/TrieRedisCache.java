//package cn.wenxun.admin.constants;
//
//import cn.iocoder.yudao.framework.common.pojo.PageResult;
//import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataPageReqVO;
//import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
//import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
//import cn.wenxun.admin.job.utils.AhoCorasickRedisUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import jakarta.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class TrieRedisCache implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Resource
//    private WenXunDictDataService wenXunDictDataService;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        initializeSensitiveWords();
//    }
//
//    /**
//     * 初始化敏感词
//     */
//    private void initializeSensitiveWords() {
//        AhoCorasickRedisUtil acRedis = new AhoCorasickRedisUtil(redisTemplate);
//        WenXunDictDataPageReqVO vo = new WenXunDictDataPageReqVO();
//        vo.setStatus(0);
//        vo.setPageNo(1);
//        vo.setPageSize(500);
//        PageResult<WenXunDictDataDO> pageResult = wenXunDictDataService.getDictDataPage(vo);
//
//        if (pageResult.getTotal() > vo.getPageSize()) {
//            int totalPage = (int) (pageResult.getTotal() / vo.getPageSize())+1;
//            for (int i = 1; i <= totalPage; i++) {
//                vo.setPageNo(i);
//                pageResult = wenXunDictDataService.getDictDataPage(vo);
//                for (WenXunDictDataDO wenXunDictDataDO : pageResult.getList()) {
//                    List<String> groupPatterns = new ArrayList<>();
//
//                    if (StringUtils.isNotEmpty(wenXunDictDataDO.getValue())) {
//                        acRedis.addPattern(wenXunDictDataDO.getValue());
//                        groupPatterns.add(wenXunDictDataDO.getValue());
//                    }
//                    if (StringUtils.isNotEmpty(wenXunDictDataDO.getLabel())) {
//                        acRedis.addPattern(wenXunDictDataDO.getLabel());
//                        groupPatterns.add(wenXunDictDataDO.getLabel());
//                    }
//                    if (StringUtils.isNotEmpty(wenXunDictDataDO.getRemark())) {
//                        String[] remark = StringUtils.split(wenXunDictDataDO.getRemark(), "|");
//                        for (String s : remark) {
//                            acRedis.addPattern(s);
//                            groupPatterns.add(s);
//                        }
//                    }
//
//                    // 添加敏感词组
//                    acRedis.addPatternGroup(wenXunDictDataDO.getId() + "", groupPatterns);
//                }
//            }
//        }
//
//        // 构建失败指针
//        acRedis.buildFailureLinks();
//    }
//}
