package cn.iocoder.yudao.module.wenxun.service.wenxun;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.vo.WenxunLoginRespVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WenxunUserDO;

/**
 * 文巡用户 Service 接口
 */
public interface WenxunUserService {

    /**
     * 创建文巡用户
     *
     * @param user 用户信息
     * @return 用户编号
     */
    Long createUser(WenxunUserDO user);

    /**
     * 更新文巡用户
     *
     * @param user 用户信息
     */
    void updateUser(WenxunUserDO user);

    /**
     * 获取文巡用户
     *
     * @param id 用户编号
     * @return 用户信息
     */
    WenxunUserDO getUser(Long id);

    /**
     * 获取文巡用户
     *
     * @param appId 应用ID
     * @return 用户信息
     */
    WenxunUserDO getUserByAppId(String appId);

    /**
     * 登录
     *
     * @return 登录结果
     */
    CommonResult<WenxunLoginRespVO> login();

} 