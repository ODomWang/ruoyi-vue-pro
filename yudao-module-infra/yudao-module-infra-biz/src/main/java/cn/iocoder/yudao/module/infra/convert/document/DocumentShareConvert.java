package cn.iocoder.yudao.module.infra.convert.document;

import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentShareCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.document.vo.DocumentShareRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.document.DocumentShareDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface DocumentShareConvert {

    DocumentShareConvert INSTANCE = Mappers.getMapper(DocumentShareConvert.class);

    @Mapping(source = "expireTime", target = "expireTime", qualifiedByName = "formatExpireTime")
    DocumentShareRespVO convert(DocumentShareDO bean);

    List<DocumentShareRespVO> convertList(List<DocumentShareDO> list);

    DocumentShareDO convert(DocumentShareCreateReqVO bean);
    
    @Named("formatExpireTime")
    default String formatExpireTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
} 