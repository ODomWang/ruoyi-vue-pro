package cn.wenxun.admin.service;

import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlpinginfo.UrlPingInfoDO;
import cn.iocoder.yudao.module.wenxun.enums.commondao.WrongWordInfo;

import java.util.List;

/**
 * 敏感词检测
 */
public interface CheckSensitiveWordsService {
    /**
     * 检测文本是否包含敏感词
     * @param content 文本内容
     * @return 检测结果
     */
    List<WrongWordInfo> checkSensitiveWords(String content);

    /**
     * 连通性检查
     */
    Boolean ping(Integer id);

    /**
     * 网站更新检查
     */
    Boolean change(Integer id);


}
