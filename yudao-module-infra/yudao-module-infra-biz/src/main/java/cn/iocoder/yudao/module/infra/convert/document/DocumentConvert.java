package cn.iocoder.yudao.module.infra.convert.document;

import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentTreeRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DocumentConvert {

    DocumentConvert INSTANCE = Mappers.getMapper(DocumentConvert.class);

    DocumentRespVO convert(DocumentDO document);

    List<DocumentRespVO> convertList(List<DocumentDO> list);

    List<DocumentTreeRespVO> convertTreeList(List<DocumentDO> list);

} 