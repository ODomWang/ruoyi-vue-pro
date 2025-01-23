package cn.wenxun.admin.core.service;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
import cn.iocoder.yudao.module.wenxun.controller.admin.urlpinglog.vo.UrlPingLogSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlpinginfo.UrlPingInfoDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.urlpinginfo.UrlPingInfoMapper;
import cn.iocoder.yudao.module.wenxun.enums.commondao.WrongWordInfo;
import cn.iocoder.yudao.module.wenxun.service.urlpinglog.UrlPingLogService;
import cn.wenxun.admin.job.utils.SensitiveFilter;
import cn.wenxun.admin.utils.ExcelToHtmlUtil;
import cn.wenxun.admin.utils.PdfToHtmlUtil;
import cn.wenxun.admin.utils.WordToHtmlUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wenxun.enums.GlobalErrorCodeConstants.TYPO_CHECKLIST_NOT_EXISTS;
import static cn.wenxun.admin.constants.GlobalErrorCodeConstants.FILE_ERROR;

@Service
@Validated
public class CheckSensitiveWordsServiceImpl implements CheckSensitiveWordsService {
    @Resource
    private WenXunDictDataService wenXunDictDataService;
    @Resource
    private UrlPingInfoMapper urlPingInfoMapper;
    @Resource
    private UrlPingLogService urlPingLogService;

    /**
     * @param content 文本内容
     * @return 错词内容
     */
    @Override
    public List<WrongWordInfo> checkSensitiveWords(String content) {
        Set<String> s2 = SensitiveFilter.getMatchingWords(content);
        //查询所有词库信息
        List<WenXunDictDataDO> list = wenXunDictDataService.getDictDataListByDatas(s2);
        // 对词库信息进行有效判断
        if (list != null && !list.isEmpty()) {
            List<WrongWordInfo> ids = new java.util.ArrayList<>();
            // 敏感词入库
            for (WenXunDictDataDO wenXunDictDataDO : list) {
                //敏感词
                if (wenXunDictDataDO.getDictType().equals("wrong_word_configuration")) {
                    boolean b = false;
                    //不包含冲突词
                    if (StringUtils.isNotEmpty(wenXunDictDataDO.getRemark())) {
                        String[] split = wenXunDictDataDO.getRemark().split("\\|");
                        for (String s : split) {
                            if (content.contains(s)) {
                                b = true;
                                break;
                            }
                        }
                    }
                    if (!b) {
                        ids.add(WrongWordInfo.builder()
                                .wrongWord(wenXunDictDataDO.getLabel()).
                                rightWord(wenXunDictDataDO.getValue()).remark(wenXunDictDataDO.getRemark())
                                .colorType(wenXunDictDataDO.getColorType())
                                .wrongWordType(wenXunDictDataDO.getDictType()).wrongWordDesc("敏感词")
                                .build());

                    }
                    //落马官员
                } else if (wenXunDictDataDO.getDictType().equals("allen_official")) {
                    if (s2.contains(wenXunDictDataDO.getValue()) && s2.contains(wenXunDictDataDO.getRemark())) {
                        ids.add(WrongWordInfo.builder()
                                .wrongWord(wenXunDictDataDO.getLabel()).
                                rightWord(wenXunDictDataDO.getValue()).remark(wenXunDictDataDO.getRemark())
                                .colorType(wenXunDictDataDO.getColorType())
                                .wrongWordType(wenXunDictDataDO.getDictType()).wrongWordDesc("落马官员")
                                .build());

                    }
                }
            }
            ids = filterSensitiveWords(content, ids);
            return ids;

        }
        return new ArrayList<>();
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean ping(Integer id) {
        if (null != id) {
            UrlPingInfoDO urlPingInfoDO = urlPingInfoMapper.selectById(id);
            if (urlPingInfoDO != null) {
                UrlPingLogSaveReqVO urlPingLogSaveReqVO = new UrlPingLogSaveReqVO();
                urlPingLogSaveReqVO.setPingId(Math.toIntExact(urlPingInfoDO.getId()));
                urlPingLogSaveReqVO.setUrlName(urlPingInfoDO.getUrlName());
                urlPingLogSaveReqVO.setUrl(urlPingInfoDO.getUrl());
                urlPingLogSaveReqVO.setUpdater(SecurityFrameworkUtils.getLoginUserNickname());

                try (WebClient webClient = new WebClient()) {
                    // 禁用 CSS 和 JavaScript 支持以提高性能
                    webClient.getOptions().setCssEnabled(false);
                    webClient.getOptions().setJavaScriptEnabled(false);
                    // 加载 URL
                    HtmlPage page = webClient.getPage(urlPingInfoDO.getUrl());
                    WebResponse response = page.getWebResponse();

                    // 检查响应状态码
                    int statusCode = response.getStatusCode();
                    if (statusCode >= 200 && statusCode < 300) {
                        urlPingLogSaveReqVO.setStatus(1);
                    } else {
                        urlPingLogSaveReqVO.setStatus(0);
                    }
                    urlPingLogSaveReqVO.setPingCode(statusCode + "");
                } catch (Exception e) {
                    // 捕获异常，表示 URL 不可用
                    urlPingLogSaveReqVO.setStatus(0);
                    urlPingLogSaveReqVO.setPingCode("-1");
                }
                urlPingInfoDO.setAllCount(urlPingInfoDO.getAllCount() + 1);
                if (urlPingLogSaveReqVO.getStatus() == 1) {
                    urlPingInfoDO.setSuccessCount(urlPingInfoDO.getSuccessCount() + 1);
                } else {
                    urlPingInfoDO.setFailCount(urlPingInfoDO.getFailCount() + 1);
                }
                urlPingInfoDO.setUpdateTime(LocalDateTime.now());
                urlPingInfoMapper.updateById(urlPingInfoDO);
                urlPingLogService.createUrlPingLog(urlPingLogSaveReqVO);

                if (urlPingLogSaveReqVO.getStatus() == 1) {
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Boolean change(Integer id) {
        if (null != id) {
            UrlPingInfoDO urlPingInfoDO = urlPingInfoMapper.selectById(id);

        }
        return null;
    }

    /**
     * @param file
     * @return
     */
    @Override
    public String checkFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "File upload failed: file is empty.";
        }
        String content = "";
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            String lowerCaseFileName = fileName.toLowerCase();
            if (lowerCaseFileName.endsWith(".doc") || lowerCaseFileName.endsWith(".docx")) {
                content = WordToHtmlUtil.convert(file.getInputStream());
            } else if (lowerCaseFileName.endsWith(".xls") || lowerCaseFileName.endsWith(".xlsx") || lowerCaseFileName.endsWith(".csv")) {
                content = ExcelToHtmlUtil.convert(file.getInputStream());
            } else if (lowerCaseFileName.endsWith(".pdf")) {
                content = PdfToHtmlUtil.PDFtoHTMLStream(file.getInputStream());
            } else {
                content = WordToHtmlUtil.convert(file.getInputStream());
            }
            // 返回成功信息
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            throw exception(FILE_ERROR);
        }

    }

    public static List<WrongWordInfo> filterSensitiveWords(String text, List<WrongWordInfo> wrongWordInfos) {
        // 存储敏感词及其位置
        List<SensitiveWord> wordPositions = new ArrayList<>();

        // 查找所有敏感词的位置
        for (WrongWordInfo info : wrongWordInfos) {
            String word = info.getWrongWord();
            int index = text.indexOf(word);
            while (index != -1) {
                wordPositions.add(new SensitiveWord(word, index, index + word.length(), info));
                index = text.indexOf(word, index + 1);
            }
        }

        // 根据长度从大到小排序，长度相同时按起始位置排序
        wordPositions.sort((a, b) -> {
            if (b.length() != a.length()) {
                return b.length() - a.length();
            }
            return a.start - b.start;
        });

        // 去重逻辑
        List<SensitiveWord> filteredWords = new ArrayList<>();
        for (SensitiveWord word : wordPositions) {
            boolean isSubsumed = false;
            for (SensitiveWord existing : filteredWords) {
                if (word.start >= existing.start && word.end <= existing.end) {
                    isSubsumed = true;
                    break;
                }
            }
            if (!isSubsumed) {
                filteredWords.add(word);
            }
        }

        // 提取结果，返回 WrongWordInfo 列表
        return filteredWords.stream()
                .map(SensitiveWord::getInfo)
                .collect(Collectors.toList());
    }

    // 敏感词位置类
    static class SensitiveWord {
        String word;
        int start;
        int end;
        @Getter
        WrongWordInfo info;

        public SensitiveWord(String word, int start, int end, WrongWordInfo info) {
            this.word = word;
            this.start = start;
            this.end = end;
            this.info = info;
        }

        public int length() {
            return end - start;
        }

    }
}
