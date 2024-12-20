package cn.wenxun.admin.controller.admin.webCheck;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.enums.commondao.WrongWordInfo;
import cn.wenxun.admin.model.WebCheckInfo;
import cn.wenxun.admin.service.CheckSensitiveWordsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "网站检查")
@RestController
@RequestMapping("/check")
public class WebCheckController {
    @Resource
    private CheckSensitiveWordsService checkSensitiveWordsService;

    @PostMapping("/onlineCheck")
    @Operation(summary = "实时检测")
    public CommonResult<List<WrongWordInfo>> getOrder(@Valid @RequestBody WebCheckInfo webCheckInfo) {
        List<WrongWordInfo> newsInfos = checkSensitiveWordsService.checkSensitiveWords(webCheckInfo.getContent());
        return success(newsInfos);

    }
}
