package cn.wenxun.admin.controller.admin.webCheck;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wenxun.enums.commondao.WrongWordInfo;
import cn.wenxun.admin.core.service.MeiliSearchService;
import cn.wenxun.admin.model.MeiliSearchInfo;
import cn.wenxun.admin.model.WebCheckInfo;
import cn.wenxun.admin.service.CheckSensitiveWordsService;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.SearchResultPaginated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
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
        pageResult.setList(searchResult.getHits());
        pageResult.setTotal((long) searchResult.getTotalHits());
        return success(pageResult);
    }
}
