package cn.iocoder.yudao.module.wenxun.convert.auth;

import cn.iocoder.yudao.module.wenxun.controller.admin.vo.WenxunUserRespVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WenxunUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 文巡用户 Convert
 */
@Mapper
public interface WenxunUserConvert {

    WenxunUserConvert INSTANCE = Mappers.getMapper(WenxunUserConvert.class);

    WenxunUserRespVO convert(WenxunUserDO bean);

} 