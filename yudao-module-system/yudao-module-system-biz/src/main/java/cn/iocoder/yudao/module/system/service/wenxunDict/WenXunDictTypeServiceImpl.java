package cn.iocoder.yudao.module.system.service.wenxunDict;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypeSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictTypeDO;
import cn.iocoder.yudao.module.system.dal.mysql.wenxunDict.WenXunDictTypeMapper;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 字典类型 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class  WenXunDictTypeServiceImpl implements  WenXunDictTypeService {

    @Resource
    private WenXunDictDataService wenXundictDataService;

    @Resource
    private WenXunDictTypeMapper wenXundictTypeMapper;

    @Override
    public PageResult<WenXunDictTypeDO> getDictTypePage(WenXunDictTypePageReqVO pageReqVO) {
        return wenXundictTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public WenXunDictTypeDO getDictType(Long id) {
        return wenXundictTypeMapper.selectById(id);
    }

    @Override
    public WenXunDictTypeDO getDictType(String type) {
        return wenXundictTypeMapper.selectByType(type);
    }

    @Override
    public Long createDictType(WenXunDictTypeSaveReqVO createReqVO) {
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(null, createReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(null, createReqVO.getType());

        // 插入字典类型
        WenXunDictTypeDO dictType = BeanUtils.toBean(createReqVO, WenXunDictTypeDO.class);
        dictType.setDeletedTime(LocalDateTimeUtils.EMPTY); // 唯一索引，避免 null 值
        wenXundictTypeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void updateDictType(WenXunDictTypeSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictTypeExists(updateReqVO.getId());
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(updateReqVO.getId(), updateReqVO.getType());

        // 更新字典类型
        WenXunDictTypeDO updateObj = BeanUtils.toBean(updateReqVO, WenXunDictTypeDO.class);
        wenXundictTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictType(Long id) {
        // 校验是否存在
        WenXunDictTypeDO dictType = validateDictTypeExists(id);
        // 校验是否有字典数据
        if (wenXundictDataService.getDictDataCountByDictType(dictType.getType()) > 0) {
            throw exception(DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        wenXundictTypeMapper.updateToDelete(id, LocalDateTime.now());
    }

    @Override
    public List<WenXunDictTypeDO> getDictTypeList() {
        return wenXundictTypeMapper.selectList();
    }

    @VisibleForTesting
    void validateDictTypeNameUnique(Long id, String name) {
        WenXunDictTypeDO dictType = wenXundictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
    }

    @VisibleForTesting
    void validateDictTypeUnique(Long id, String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        WenXunDictTypeDO dictType = wenXundictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    @VisibleForTesting
    WenXunDictTypeDO validateDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        WenXunDictTypeDO dictType = wenXundictTypeMapper.selectById(id);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

}
