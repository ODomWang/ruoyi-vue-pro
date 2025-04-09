package cn.iocoder.yudao.module.infra.service.document;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentShareCreateReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentShareDO;

/**
 * 文档分享 Service 接口
 *
 * @author 芋道源码
 */
public interface DocumentShareService {

    /**
     * 创建文档分享
     *
     * @param reqVO 创建信息
     * @param userId 用户编号
     * @return 分享链接的标识
     */
    String createDocumentShare(DocumentShareCreateReqVO reqVO, Long userId);

    /**
     * 验证分享链接
     *
     * @param shareId 分享链接的标识
     * @param password 密码，允许为空
     * @return 文档对象
     */
    DocumentDO verifyDocumentShare(String shareId, String password);

    /**
     * 获得文档分享
     *
     * @param id 分享编号
     * @return 文档分享
     */
    DocumentShareDO getDocumentShare(Long id);
    
    /**
     * 根据分享标识获得文档分享
     *
     * @param shareId 分享标识
     * @return 文档分享
     */
    DocumentShareDO getDocumentShareByShareId(String shareId);

    /**
     * 获得文档分享分页
     *
     * @param pageParam 分页参数
     * @param userId 用户编号
     * @return 文档分享分页
     */
    PageResult<DocumentShareDO> getDocumentSharePage(PageParam pageParam, Long userId);

    /**
     * 更新文档分享状态
     *
     * @param id 分享编号
     * @param status 状态
     * @return 是否成功
     */
    boolean updateDocumentShareStatus(Long id, Integer status);

    /**
     * 删除文档分享
     *
     * @param id 分享编号
     * @return 是否成功
     */
    boolean deleteDocumentShare(Long id);

} 