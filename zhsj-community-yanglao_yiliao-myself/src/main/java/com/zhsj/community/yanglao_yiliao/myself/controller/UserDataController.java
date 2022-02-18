package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.UserSettingEntity;
import com.zhsj.community.yanglao_yiliao.myself.service.IUserSettingService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: Hu
 * @create: 2021-11-23 14:47
 **/
@RestController
@RequestMapping("user")
public class UserDataController {

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUserInfoRpcService baseUserInfoRpcService;

    @Resource
    private IUserSettingService userSettingService;

    /**
     * @Description: 查询用户详情
     * @author: Hu
     * @since: 2021/11/23 14:51
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.base.api.entity.UserDetail>
     */
    @GetMapping("details")
    public R<UserDetail> list(){
        LoginUser user = ContextHolder.getContext().getLoginUser();
        return R.ok(baseUserInfoRpcService.getUserDetail(user.getAccount()));
    }
    /**
     * @Description: 查询个人设置
     * @author: Hu
     * @since: 2021/11/25 16:21
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.community.yanglao_yiliao.common.entity.UserSettingEntity>
     */
    @GetMapping("setting")
    public R<UserSettingEntity> setting(){
        LoginUser user = ContextHolder.getContext().getLoginUser();
        UserSettingEntity setting = userSettingService.getSetting(user);
        return R.ok(setting);
    }
    /**
     * @Description: 开启关闭消息提醒
     * @author: Hu
     * @since: 2021/11/25 16:25
     * @Param: [id, status]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @GetMapping("updateNotification")
    public R<Void> updateNotification(@RequestParam Long id,@RequestParam Integer status){
        UserSettingEntity entity = userSettingService.getById(id);
        if (entity != null) {
            entity.setNotificationStatus(status);
            entity.setUpdateTime(LocalDateTime.now());
            userSettingService.updateById(entity);
            return R.ok();
        } else {
            return R.fail("数据不存在！");
        }
    }
    /**
     * @Description: 开启关闭消息震动
     * @author: Hu
     * @since: 2021/11/25 16:25
     * @Param: [id, status]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @GetMapping("updateShake")
    public R<Void> updateShake(@RequestParam Long id,@RequestParam Integer status){
        UserSettingEntity entity = userSettingService.getById(id);
        if (entity != null) {
            entity.setUpdateTime(LocalDateTime.now());
            entity.setShakeStatus(status);
            userSettingService.updateById(entity);
            return R.ok();
        } else {
            return R.fail("数据不存在！");
        }
    }
}
