package cn.iocoder.yudao.module.system.service.dict;

import cn.hutool.core.io.FileUtil;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.dal.mysql.wenxunDict.WenXunDictDataMapper;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(WenXunDictDataServiceImpl.class)
public class DictDataServiceImplTest extends BaseDbUnitTest {


    @Resource
    private WenXunDictDataMapper wenXunDictDataMapper;

    @Resource
    private WenXunDictDataServiceImpl wenXunDictDataService;

    @Test
    public void testCreateDictData_success() {
        // 准备参数
        List<String> list = FileUtil.readUtf8Lines("C:\\Users\\wangjp\\Downloads\\错别字1020.csv");
        int i = 0;
        for (String s : list) {
            if (i == 0) {
                i++;
                continue;
            }
            String[] t = s.split(",,,");
            WenXunDictDataSaveReqVO wenXunDictDataDO = new WenXunDictDataSaveReqVO();
            wenXunDictDataDO.setSort(i);
            wenXunDictDataDO.setLabel(t[0]);
            wenXunDictDataDO.setValue(t[1]);
            wenXunDictDataDO.setDictType("wrong_word_configuration");
            wenXunDictDataDO.setStatus(0);
            wenXunDictDataDO.setColorType("warning");
            try {
                wenXunDictDataDO.setRemark(t[2]);
            } catch (Exception e) {
                System.out.println(s);
            }
            i++;
            long dictDataId = wenXunDictDataService.createDictData(wenXunDictDataDO);
            // 断言
            assertNotNull(dictDataId);
            // 校验记录的属性是否正确
            WenXunDictDataDO dictData = wenXunDictDataMapper.selectById(dictDataId);
            assertPojoEquals(wenXunDictDataDO, dictData, "id");
        }


    }


}
