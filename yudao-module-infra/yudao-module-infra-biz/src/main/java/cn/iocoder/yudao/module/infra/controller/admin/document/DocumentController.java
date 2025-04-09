package cn.iocoder.yudao.module.infra.controller.admin.document;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentQueryVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentTreeRespVO;
import cn.iocoder.yudao.module.infra.convert.document.DocumentConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;
import cn.iocoder.yudao.module.infra.service.document.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@Tag(name = "管理后台 - 文档")
@RestController
@RequestMapping("/infra/document")
@Validated
public class DocumentController {

    @Resource
    private DocumentService documentService;

    @GetMapping("/tree")
    @Operation(summary = "获得文档树形结构")
    public CommonResult<List<DocumentTreeRespVO>> getDocumentTree() {
        List<DocumentTreeRespVO> tree = documentService.getDocumentTree();
        return success(tree);
    }

    @GetMapping("/list")
    @Operation(summary = "获得文档列表")
    public CommonResult<List<DocumentRespVO>> getDocumentList(@Valid DocumentQueryVO queryVO) {
        List<DocumentDO> list = documentService.getDocumentList(queryVO);
        return success(DocumentConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/children")
    @Operation(summary = "获得子文档列表")
    @Parameter(name = "parentId", description = "父文档编号", required = true, example = "1024")
    public CommonResult<List<DocumentRespVO>> getChildDocumentList(@RequestParam("parentId") Long parentId) {
        List<DocumentDO> list = documentService.getChildDocuments(parentId);
        return success(DocumentConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/folder")
    @Operation(summary = "创建文件夹")
    @Parameter(name = "parentId", description = "父文档编号", required = true, example = "1024")
    @Parameter(name = "title", description = "文件夹名称", required = true, example = "项目文档")
    public CommonResult<Long> createFolder(@RequestParam("parentId") Long parentId,
                                         @RequestParam("title") String title) {
        Long folderId = documentService.createFolder(parentId, title, SecurityFrameworkUtils.getLoginUserId());
        return success(folderId);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文档内容")
    @Parameter(name = "id", description = "文档编号", required = true, example = "1024")
    public CommonResult<DocumentRespVO> getDocument(@RequestParam("id") Long id) {
        DocumentDO document = documentService.getDocument(id);
        return success(DocumentConvert.INSTANCE.convert(document));
    }

} 