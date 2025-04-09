package cn.iocoder.yudao.module.infra.controller.admin.document;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentShareAccessReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentShareCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentShareRespVO;
import cn.iocoder.yudao.module.infra.convert.document.DocumentConvert;
import cn.iocoder.yudao.module.infra.convert.document.DocumentShareConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentShareDO;
import cn.iocoder.yudao.module.infra.enums.ErrorCodeConstantsDocument;
import cn.iocoder.yudao.module.infra.service.document.DocumentShareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 文档分享")
@RestController
@RequestMapping("/infra/document-share")
@Validated
public class DocumentShareController {

    @Resource
    private DocumentShareService documentShareService;

    @PostMapping("/create")
    @Operation(summary = "创建文档分享")

    public CommonResult<String> createDocumentShare(@Valid @RequestBody DocumentShareCreateReqVO reqVO) {
        String shareId = documentShareService.createDocumentShare(reqVO, SecurityFrameworkUtils.getLoginUserId());
        return success(shareId);
    }

    @GetMapping("/access")
    @Operation(summary = "访问分享文档")
    @PermitAll
    public CommonResult<DocumentRespVO> accessDocumentShare(@Valid DocumentShareAccessReqVO reqVO) {
        try {
            DocumentDO document = documentShareService.verifyDocumentShare(reqVO.getShareId(), reqVO.getPassword());
            return success(DocumentConvert.INSTANCE.convert(document));
        } catch (ServiceException e) {
            return CommonResult.error(e.getCode(), e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "获得文档分享列表")
    public CommonResult<List<DocumentShareRespVO>> getDocumentShareList() {
        PageResult<DocumentShareDO> pageResult = documentShareService.getDocumentSharePage(new PageParam(), SecurityFrameworkUtils.getLoginUserId());
        
        // 手动转换，不使用MapStruct
        List<DocumentShareRespVO> voList = new java.util.ArrayList<>(pageResult.getList().size());
        for (DocumentShareDO share : pageResult.getList()) {
            DocumentShareRespVO vo = new DocumentShareRespVO();
            vo.setId(share.getId());
            vo.setShareId(share.getShareId());
            vo.setDocumentId(share.getDocumentId());
            vo.setUserId(share.getUserId());
            vo.setPasswordProtected(share.getPasswordProtected());
            if (share.getExpireTime() != null) {
                vo.setExpireTime(share.getExpireTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            vo.setStatus(share.getStatus());
            vo.setCreateTime(share.getCreateTime());
            vo.setRemark(share.getRemark());
            voList.add(vo);
        }
        
        return success(voList);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文档分享")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteDocumentShare(@RequestParam("id") Long id) {
        boolean success = documentShareService.deleteDocumentShare(id);
        return success(success);
    }

    @PutMapping("/disable")
    @Operation(summary = "禁用文档分享")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> disableDocumentShare(@RequestParam("id") Long id) {
        boolean success = documentShareService.updateDocumentShareStatus(id, 2);
        return success(success);
    }

    @GetMapping("/info")
    @Operation(summary = "获取分享文档的基本信息")
    @PermitAll
    @Parameter(name = "shareId", description = "分享ID", required = true, example = "a1b2c3d4")
    public CommonResult<Map<String, Object>> getDocumentShareInfo(@RequestParam("shareId") Long shareId) {
        try {
            DocumentShareDO share = documentShareService.getDocumentShareByShareId(shareId.toString());
            if (share == null) {
                throw new ServiceException(ErrorCodeConstantsDocument.DOCUMENT_SHARE_NOT_EXISTS);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("passwordProtected", share.getPasswordProtected());
            
            if (share.getExpireTime() != null) {
                result.put("expireTime", share.getExpireTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                result.put("expireTime", null);
            }
            
            result.put("status", share.getStatus());
            return success(result);
        } catch (ServiceException e) {
            return CommonResult.error(e.getCode(), e.getMessage());
        }
    }
} 