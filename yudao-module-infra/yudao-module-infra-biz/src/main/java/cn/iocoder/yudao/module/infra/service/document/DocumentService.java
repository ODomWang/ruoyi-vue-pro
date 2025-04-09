package cn.iocoder.yudao.module.infra.service.document;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentQueryVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentTreeRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;

import java.util.List;

/**
 * 文档 Service 接口
 *
 * @author 芋道源码
 */
public interface DocumentService {

    /**
     * 获得文档树形结构
     *
     * @return 文档树形结构
     */
    List<DocumentTreeRespVO> getDocumentTree();

    /**
     * 获得文档列表
     *
     * @param queryVO 查询条件
     * @return 文档列表
     */
    List<DocumentDO> getDocumentList(DocumentQueryVO queryVO);

    /**
     * 创建文档
     *
     * @param parentId 父文档编号
     * @param title 标题
     * @param content 内容
     * @param userId 用户编号
     * @return 文档编号
     */
    Long createDocument(Long parentId, String title, String content, Long userId);

    /**
     * 创建文件夹
     *
     * @param parentId 父文档编号
     * @param title 标题
     * @param userId 用户编号
     * @return 文档编号
     */
    Long createFolder(Long parentId, String title, Long userId);

    /**
     * 更新文档
     *
     * @param id 文档编号
     * @param title 标题
     * @param content 内容
     * @param version 版本号
     * @param userId 用户编号
     * @return 是否成功
     */
    boolean updateDocument(Long id, String title, String content, Integer version, Long userId);

    /**
     * 更新文档状态
     *
     * @param id 文档编号
     * @param status 状态
     * @param userId 用户编号
     * @return 是否成功
     */
    boolean updateDocumentStatus(Long id, Integer status, Long userId);

    /**
     * 获得文档
     *
     * @param id 文档编号
     * @return 文档
     */
    DocumentDO getDocument(Long id);

    /**
     * 获得文档分页
     *
     * @param parentId 父级文档编号
     * @param title 标题
     * @param status 状态
     * @return 文档分页
     */
    PageResult<DocumentDO> getDocumentPage(Long parentId, String title, Integer status);

    /**
     * 获取指定文档的子文档列表
     *
     * @param parentId 父级文档编号
     * @return 子文档列表
     */
    List<DocumentDO> getChildDocuments(Long parentId);

    /**
     * 删除文档
     *
     * @param id 文档编号
     * @return 是否成功
     */
    boolean deleteDocument(Long id);

    /**
     * 自动保存文档
     *
     * @param id 文档编号
     * @param content 内容
     * @param userId 用户编号
     * @return 是否成功
     */
    boolean autoSaveDocument(Long id, String content, Long userId);

    /**
     * 移动文档
     *
     * @param id 文档编号
     * @param parentId 父文档编号
     * @param userId 用户编号
     * @return 是否成功
     */
    boolean moveDocument(Long id, Long parentId, Long userId);

    /**
     * 重命名文档
     *
     * @param id 文档编号
     * @param title 新标题
     * @param userId 用户编号
     * @return 是否成功
     */
    boolean renameDocument(Long id, String title, Long userId);

} 