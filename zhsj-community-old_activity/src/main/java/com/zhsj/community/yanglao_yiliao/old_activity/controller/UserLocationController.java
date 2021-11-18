package com.zhsj.community.yanglao_yiliao.old_activity.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.UserLocationFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.service.UserLocationService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_yiliao
 * @description: 距离相关的接口
 * @create: 2021-11-11 15:13
 */
@RestController
@RequestMapping("userLocation")
public class UserLocationController {

    @Resource
    private UserLocationService userLocationService;

    /**
     * 搜索附近
     */
    @GetMapping("search")
    public R<List<UserLocationVo>> queryNearUser(@RequestBody UserLocationFrom userLocationForm){
            List<UserLocationVo> userLocationVos = this.userLocationService.queryNearUser(userLocationForm);
            return R.ok(userLocationVos);
    }




}
