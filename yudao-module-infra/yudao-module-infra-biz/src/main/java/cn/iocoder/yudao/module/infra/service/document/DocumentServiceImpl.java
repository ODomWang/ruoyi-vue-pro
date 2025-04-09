package cn.iocoder.yudao.module.infra.service.document;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentQueryVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentTreeRespVO;
import cn.iocoder.yudao.module.infra.convert.document.DocumentConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;
import cn.iocoder.yudao.module.infra.dal.mysql.document.DocumentMapper;
import cn.iocoder.yudao.module.infra.enums.document.DocumentStatusEnum;
import cn.iocoder.yudao.module.infra.enums.document.DocumentTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文档 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Resource
    private DocumentMapper documentMapper;

    @Override
    public List<DocumentTreeRespVO> getDocumentTree() {
        // 获取所有文档
        List<DocumentDO> allDocuments = documentMapper.selectList();
        // 转换为树形结构
        return buildTree(allDocuments);
    }

    /**
     * 构建树形结构
     *
     * @param documents 所有文档列表
     * @return 树形结构的文档列表
     */
    private List<DocumentTreeRespVO> buildTree(List<DocumentDO> documents) {
        // 转换为VO
        List<DocumentTreeRespVO> documentVOs = DocumentConvert.INSTANCE.convertTreeList(documents);
        // 按父ID分组
        Map<Long, List<DocumentTreeRespVO>> parentIdMap = documentVOs.stream()
                .collect(Collectors.groupingBy(DocumentTreeRespVO::getParentId));
        // 设置子文档
        documentVOs.forEach(vo -> vo.setChildren(parentIdMap.getOrDefault(vo.getId(), new ArrayList<>())));
        // 返回顶级文档
        return parentIdMap.getOrDefault(0L, new ArrayList<>());
    }

    @Override
    public List<DocumentDO> getDocumentList(DocumentQueryVO queryVO) {
        return documentMapper.selectList(queryVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDocument(Long parentId, String title, String content, Long userId) {
        if (null == parentId) {
            parentId = 0L;
        }
        // 校验父文档存在
        if (!Objects.equals(parentId, DocumentDO.PARENT_ID_ROOT)) {
            DocumentDO parentDocument = documentMapper.selectById(parentId);
            if (parentDocument == null) {
                log.warn("[createDocument][父文档不存在，无法创建文档：{}]", parentId);
                return null;
            }
        }

        DocumentDO document = new DocumentDO();
        document.setParentId(parentId);
        document.setTitle(title);
        document.setContent(content);
        document.setType(DocumentTypeEnum.FILE.getType()); // 默认为文件类型
        document.setStatus(DocumentStatusEnum.DRAFT.getStatus());
        document.setLastUpdatedBy(userId);
        document.setVersion(1);
        documentMapper.insert(document);
        return document.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFolder(Long parentId, String title, Long userId) {
        if (null == parentId) {
            parentId = 0L;
        }
        // 校验父文档存在
        if (!Objects.equals(parentId, DocumentDO.PARENT_ID_ROOT)) {
            DocumentDO parentDocument = documentMapper.selectById(parentId);
            if (parentDocument == null) {
                log.warn("[createFolder][父文档不存在，无法创建文件夹：{}]", parentId);
                return null;
            }
        }

        DocumentDO document = new DocumentDO();
        document.setParentId(parentId);
        document.setTitle(title);
        document.setType(DocumentTypeEnum.FOLDER.getType()); // 设置为文件夹类型
        document.setStatus(DocumentStatusEnum.DRAFT.getStatus());
        document.setLastUpdatedBy(userId);
        document.setVersion(1);
        documentMapper.insert(document);
        return document.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDocument(Long id, String title, String content, Integer version, Long userId) {
        // 校验存在
        DocumentDO document = documentMapper.selectById(id);
        if (document == null) {
            log.warn("[updateDocument][文档不存在，无法更新：{}]", id);
            return false;
        }

        // 校验版本号，避免并发冲突
//        if (!Objects.equals(document.getVersion(), version)) {
//            log.warn("[updateDocument][文档版本号不匹配，无法更新。期望:{}, 当前:{}]", version, document.getVersion());
//            return false;
//        }

        // 更新文档
        DocumentDO updateDocument = new DocumentDO();
        updateDocument.setId(id);
        updateDocument.setTitle(title);
        updateDocument.setContent(content);
        updateDocument.setLastUpdatedBy(userId);
        updateDocument.setVersion(document.getVersion() + 1); // 版本号 + 1
        documentMapper.updateById(updateDocument);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDocumentStatus(Long id, Integer status, Long userId) {
        // 校验存在
        DocumentDO document = documentMapper.selectById(id);
        if (document == null) {
            log.warn("[updateDocumentStatus][文档不存在，无法更新状态：{}]", id);
            return false;
        }

        // 更新状态
        DocumentDO updateDocument = new DocumentDO();
        updateDocument.setId(id);
        updateDocument.setStatus(status);
        updateDocument.setLastUpdatedBy(userId);
        documentMapper.updateById(updateDocument);
        return true;
    }

    @Override
    public DocumentDO getDocument(Long id) {
        return documentMapper.selectById(id);
    }

    @Override
    public PageResult<DocumentDO> getDocumentPage(Long parentId, String title, Integer status) {
        return documentMapper.selectPage(new PageParam(), parentId, title, status);
    }

    @Override
    public List<DocumentDO> getChildDocuments(Long parentId) {
        return documentMapper.selectList(parentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDocument(Long id) {
        // 删除子文档
        List<DocumentDO> childDocuments = getChildDocuments(id);
        for (DocumentDO child : childDocuments) {
            deleteDocument(child.getId());
        }
        // 删除当前文档
        documentMapper.deleteById(id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean autoSaveDocument(Long id, String content, Long userId) {
        // 校验存在
        DocumentDO document = documentMapper.selectById(id);
        if (document == null) {
            log.warn("[autoSaveDocument][文档不存在，无法自动保存：{}]", id);
            return false;
        }

        // 更新文档内容，不更新版本号
        DocumentDO updateDocument = new DocumentDO();
        updateDocument.setId(id);
        updateDocument.setContent(content);
        updateDocument.setLastUpdatedBy(userId);
        documentMapper.updateById(updateDocument);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveDocument(Long id, Long parentId, Long userId) {
        // 校验存在
        DocumentDO document = documentMapper.selectById(id);
        if (document == null) {
            log.warn("[moveDocument][文档不存在，无法移动：{}]", id);
            return false;
        }

        // 校验父文档存在
        if (!Objects.equals(parentId, DocumentDO.PARENT_ID_ROOT)) {
            DocumentDO parentDocument = documentMapper.selectById(parentId);
            if (parentDocument == null) {
                log.warn("[moveDocument][父文档不存在，无法移动：{}]", parentId);
                return false;
            }
        }

        // 更新文档
        DocumentDO updateDocument = new DocumentDO();
        updateDocument.setId(id);
        updateDocument.setParentId(parentId);
        updateDocument.setLastUpdatedBy(userId);
        documentMapper.updateById(updateDocument);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean renameDocument(Long id, String title, Long userId) {
        // 校验存在
        DocumentDO document = documentMapper.selectById(id);
        if (document == null) {
            log.warn("[renameDocument][文档不存在，无法重命名：{}]", id);
            return false;
        }

        // 更新文档
        DocumentDO updateDocument = new DocumentDO();
        updateDocument.setId(id);
        updateDocument.setTitle(title);
        updateDocument.setLastUpdatedBy(userId);
        documentMapper.updateById(updateDocument);
        return true;
    }
} 