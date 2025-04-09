package cn.iocoder.yudao.module.infra.service.document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentShareCreateReqVO;
import cn.iocoder.yudao.module.infra.convert.document.DocumentShareConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentShareDO;
import cn.iocoder.yudao.module.infra.dal.mysql.document.DocumentMapper;
import cn.iocoder.yudao.module.infra.dal.mysql.document.DocumentShareMapper;
import cn.iocoder.yudao.module.infra.enums.ErrorCodeConstantsDocument;
import cn.iocoder.yudao.module.infra.enums.document.DocumentShareStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 文档分享 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DocumentShareServiceImpl implements DocumentShareService {

    @Resource
    private DocumentShareMapper documentShareMapper;

    @Resource
    private DocumentMapper documentMapper;

    @Resource
    private DocumentService documentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createDocumentShare(DocumentShareCreateReqVO reqVO, Long userId) {
        // 校验文档是否存在
        DocumentDO document = documentService.getDocument(reqVO.getDocumentId());
        if (document == null) {
            log.warn("[createDocumentShare][文档不存在，无法创建分享链接：{}]", reqVO.getDocumentId());
            return null;
        }

        // 创建分享记录 - 不使用MapStruct转换，直接手动创建
        DocumentShareDO documentShare = new DocumentShareDO();
        documentShare.setDocumentId(reqVO.getDocumentId());
        documentShare.setPassword(reqVO.getPassword());
        documentShare.setRemark(reqVO.getRemark());
        documentShare.setUserId(userId);
        documentShare.setShareId(generateShareId());
        documentShare.setStatus(DocumentShareStatusEnum.NORMAL.getStatus());
        documentShare.setPasswordProtected(StringUtils.hasText(reqVO.getPassword()));

        // 处理过期时间
        if (StringUtils.hasText(reqVO.getExpireTime())) {
            try {
                LocalDateTime expireTime = OffsetDateTime.parse(reqVO.getExpireTime())
                        .toLocalDateTime();
                documentShare.setExpireTime(expireTime);
            } catch (Exception e) {
                log.warn("[createDocumentShare][过期时间格式错误：{}]", reqVO.getExpireTime(), e);
            }
        }
        
        // 插入数据库
        documentShareMapper.insert(documentShare);
        
        return documentShare.getShareId();
    }

    @Override
    public DocumentDO verifyDocumentShare(String shareId, String password) {
        // 获取分享记录
        DocumentShareDO share = documentShareMapper.selectByShareId(shareId);
        if (share == null) {
            throw new ServiceException(ErrorCodeConstantsDocument.DOCUMENT_SHARE_NOT_EXISTS);
        }

        // 校验状态
        if (!Objects.equals(share.getStatus(), DocumentShareStatusEnum.NORMAL.getStatus())) {
            if (Objects.equals(share.getStatus(), DocumentShareStatusEnum.EXPIRED.getStatus())) {
                throw new ServiceException(ErrorCodeConstantsDocument.DOCUMENT_SHARE_EXPIRED);
            } else if (Objects.equals(share.getStatus(), DocumentShareStatusEnum.DISABLED.getStatus())) {
                throw new ServiceException(ErrorCodeConstantsDocument.DOCUMENT_SHARE_DISABLED);
            }
            throw new ServiceException(ErrorCodeConstantsDocument.DOCUMENT_SHARE_DISABLED);
        }

        // 校验过期时间
        if (share.getExpireTime() != null && share.getExpireTime().isBefore(LocalDateTime.now())) {
            // 更新状态为已过期
            updateDocumentShareStatus(share.getId(), DocumentShareStatusEnum.EXPIRED.getStatus());
            throw new ServiceException(ErrorCodeConstantsDocument.DOCUMENT_SHARE_EXPIRED);
        }

        // 校验密码
        if (share.getPasswordProtected() && !Objects.equals(share.getPassword(), password)) {
            throw new ServiceException(ErrorCodeConstantsDocument.DOCUMENT_SHARE_PASSWORD_ERROR);
        }

        // 获取文档内容
        return documentService.getDocument(share.getDocumentId());
    }

    @Override
    public DocumentShareDO getDocumentShare(Long id) {
        return documentShareMapper.selectById(id);
    }

    @Override
    public DocumentShareDO getDocumentShareByShareId(String shareId) {
        return documentShareMapper.selectByShareId(shareId);
    }

    @Override
    public PageResult<DocumentShareDO> getDocumentSharePage(PageParam pageParam, Long userId) {
        return documentShareMapper.selectPage(pageParam, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDocumentShareStatus(Long id, Integer status) {
        // 校验存在
        DocumentShareDO share = documentShareMapper.selectById(id);
        if (share == null) {
            log.warn("[updateDocumentShareStatus][分享链接不存在，无法更新状态：{}]", id);
            return false;
        }

        // 更新状态
        DocumentShareDO updateShare = new DocumentShareDO();
        updateShare.setId(id);
        updateShare.setStatus(status);
        documentShareMapper.updateById(updateShare);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDocumentShare(Long id) {
        // 校验存在
        DocumentShareDO share = documentShareMapper.selectById(id);
        if (share == null) {
            log.warn("[deleteDocumentShare][分享链接不存在，无法删除：{}]", id);
            return false;
        }

        // 删除分享记录
        documentShareMapper.deleteById(id);
        return true;
    }

    /**
     * 生成分享ID
     *
     * @return 分享ID
     */
    private String generateShareId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
} 