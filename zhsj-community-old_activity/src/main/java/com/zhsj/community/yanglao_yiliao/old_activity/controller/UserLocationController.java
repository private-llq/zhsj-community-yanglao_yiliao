package com.zhsj.community.yanglao_yiliao.old_activity.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.old_activity.service.UserLocationService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-11 15:13
 */
@RestController
@RequestMapping("userLocation")
public class UserLocationController {


    @Autowired
    private UserLocationService userLocationService;
    /**
     * 搜索附近
     * @return
     */
    @GetMapping("search")
    public R<List<UserLocationVo>> queryNearUser(@RequestParam(value = "gender", required = false) String sex,
                                             @RequestParam(value = "distance", defaultValue = "2000") String distance){
            List<UserLocationVo> userLocationVos = this.userLocationService.queryNearUser(sex, distance);
            return R.ok(userLocationVos);
    }

}
