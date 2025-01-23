package cn.wenxun.admin.controller.admin.webCheck;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
import cn.iocoder.yudao.module.wenxun.enums.commondao.WrongWordInfo;
import cn.iocoder.yudao.module.wenxun.model.MeiliSearchInfo;
import cn.iocoder.yudao.module.wenxun.model.WebCheckInfo;
import cn.wenxun.admin.core.service.CheckSensitiveWordsService;
import cn.wenxun.admin.core.service.MeiliSearchService;
import cn.wenxun.admin.job.SpiderCrawlJob;
import cn.wenxun.admin.utils.LongestHighlightExtractor;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.meilisearch.sdk.model.SearchResultPaginated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "网站检查")
@RestController
@RequestMapping("/check")
public class WebCheckController {
    @Resource
    private CheckSensitiveWordsService checkSensitiveWordsService;
    @Resource
    public MeiliSearchService meiliSearchService;
    @Resource
    private WenXunDictDataService wenXunDictDataService;


    @PostMapping("/onlineCheck")
    @Operation(summary = "实时检测")
    public CommonResult<List<WrongWordInfo>> getOrder(@Valid @RequestBody WebCheckInfo webCheckInfo) {
        List<WrongWordInfo> newsInfos = checkSensitiveWordsService.checkSensitiveWords(webCheckInfo.getContent());
        return success(newsInfos);

    }

    /**
     * 连通性检查
     */
    @GetMapping("/ping/get")
    @Operation(summary = "连通性检查")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> getUrlPingInfo(@RequestParam("id") Integer id) {
        Boolean newsInfos = checkSensitiveWordsService.ping(id);
        return success(newsInfos);

    }

    /**
     * 更新检查
     */
    @PutMapping("/change")
    @Operation(summary = "更新检查")
    public CommonResult<Boolean> updateUrlPingInfo(@Valid @RequestBody MeiliSearchInfo webPingInfo) {
        return success(false);
    }


    /**
     * 信息检索
     */
    @PostMapping("/search")
    @Operation(summary = "信息检索")
    public CommonResult<PageResult<HashMap<String, Object>>> meiliSearch(@Valid @RequestBody MeiliSearchInfo webPingInfo) {
        SearchResultPaginated searchResult = meiliSearchService.searchPage(webPingInfo);
        PageResult<HashMap<String, Object>> pageResult = new PageResult<>();
        searchResult.getHits().forEach(item -> {
            String text = ((LinkedTreeMap<String, Object>) item.get("_formatted")).get("content").toString();
            text = LongestHighlightExtractor.HighlightExtractor(text);
            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) item.get("_formatted");
            map.put("content", text);
            item.put("_formatted", map);
        });
        pageResult.setList(searchResult.getHits());
        pageResult.setTotal((long) searchResult.getTotalHits());
        return success(pageResult);
    }


    @PostMapping("/checkFile")
    @Operation(summary = "附件检测")
    public CommonResult<JSONObject> uploadFile(@RequestParam("file") MultipartFile file) {
        String content = checkSensitiveWordsService.checkFile(file);

        return success(SpiderCrawlJob.checkSensitiveWords(content, wenXunDictDataService));
    }
}
