package cn.iocoder.yudao.module.system.service.wenxunDict;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictTypeDO;
import cn.iocoder.yudao.module.system.dal.mysql.wenxunDict.WenXunDictDataMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 字典数据 Service 实现类
 *
 * @author ruoyi
 */
@Service
@Slf4j
public class WenXunDictDataServiceImpl implements WenXunDictDataService {

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<WenXunDictDataDO> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(WenXunDictDataDO::getDictType)
            .thenComparingInt(WenXunDictDataDO::getSort);

    @Resource
    private WenXunDictTypeService wenXunDictTypeService;

    @Resource
    private WenXunDictDataMapper wenXunDictDataMapper;

    @Override
    public List<WenXunDictDataDO> getDictDataList(Integer status, String dictType) {
        List<WenXunDictDataDO> list = wenXunDictDataMapper.selectListByStatusAndDictType(status, dictType);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public PageResult<WenXunDictDataDO> getDictDataPage(WenXunDictDataPageReqVO pageReqVO) {
        return wenXunDictDataMapper.selectPage(pageReqVO);
    }

    @Override
    public WenXunDictDataDO getDictData(Long id) {
        return wenXunDictDataMapper.selectById(id);
    }

    @Override
    public Long createDictData(WenXunDictDataSaveReqVO createReqVO) {
        // 校验字典类型有效
        validateDictTypeExists(createReqVO.getDictType());
        // 校验字典数据的值的唯一性
//        validateDictDataValueUnique(null, createReqVO.getDictType(), createReqVO.getValue());

        // 插入字典类型
        WenXunDictDataDO dictData = BeanUtils.toBean(createReqVO, WenXunDictDataDO.class);
        wenXunDictDataMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public void updateDictData(WenXunDictDataSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictDataExists(updateReqVO.getId());
        // 校验字典类型有效
        validateDictTypeExists(updateReqVO.getDictType());
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(updateReqVO.getId(), updateReqVO.getDictType(), updateReqVO.getValue());

        // 更新字典类型
        WenXunDictDataDO updateObj = BeanUtils.toBean(updateReqVO, WenXunDictDataDO.class);
        wenXunDictDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        validateDictDataExists(id);

        // 删除字典数据
        wenXunDictDataMapper.deleteById(id);
    }

    @Override
    public long getDictDataCountByDictType(String dictType) {
        return wenXunDictDataMapper.selectCountByDictType(dictType);
    }

    @VisibleForTesting
    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        WenXunDictDataDO dictData = wenXunDictDataMapper.selectByDictTypeAndLabel(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
//        if (id == null) {
//            throw exception(DICT_DATA_VALUE_DUPLICATE);
//        }
//        if (!dictData.getId().equals(id)) {
//            throw exception(DICT_DATA_VALUE_DUPLICATE);
//        }
    }

    @VisibleForTesting
    public void validateDictDataExists(Long id) {
        if (id == null) {
            return;
        }
        WenXunDictDataDO dictData = wenXunDictDataMapper.selectById(id);
        if (dictData == null) {
            throw exception(DICT_DATA_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    public void validateDictTypeExists(String type) {
        WenXunDictTypeDO dictType = wenXunDictTypeService.getDictType(type);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw exception(DICT_TYPE_NOT_ENABLE);
        }
    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        Map<String, WenXunDictDataDO> dictDataMap = CollectionUtils.convertMap(
                wenXunDictDataMapper.selectByDictTypeAndValues(dictType, values), WenXunDictDataDO::getValue);
        // 校验
        values.forEach(value -> {
            WenXunDictDataDO dictData = dictDataMap.get(value);
            if (dictData == null) {
                throw exception(DICT_DATA_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dictData.getStatus())) {
                throw exception(DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        });
    }

    @Override
    public WenXunDictDataDO getDictData(String dictType, String value) {
        return wenXunDictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public WenXunDictDataDO parseDictData(String dictType, String label) {
        return wenXunDictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }

    @Override
    public List<WenXunDictDataDO> getDictDataListByDictType(String dictType) {
        List<WenXunDictDataDO> list = wenXunDictDataMapper.selectList(WenXunDictDataDO::getDictType, dictType);
        list.sort(Comparator.comparing(WenXunDictDataDO::getSort));
        return list;
    }

    /**
     * @param datas
     * @return
     */
    @Override
    public List<WenXunDictDataDO> getDictDataListByDatas(Set<String> datas) {
        if (!org.springframework.util.CollectionUtils.isEmpty(datas)) {
            return   wenXunDictDataMapper.selectListDatas(datas);
        }
        return null;
    }

}
