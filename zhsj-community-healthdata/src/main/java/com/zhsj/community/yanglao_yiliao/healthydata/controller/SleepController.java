package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorSleepReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.SleepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户睡眠控制层
 * @date 2021/11/11 15:54
 */
@RestController
@RequestMapping("/sleep")
@Validated
public class SleepController {

    @Autowired
    private SleepService sleepService;

    /***************************************************************************************************************************
     * @description 监控用户睡眠并保存
     * @author zzm
     * @date 2021/11/11 15:32
     * @param list 用户睡眠信息
     **************************************************************************************************************************/
    @PostMapping("/monitorSleep")
    public R<Void> monitorSleep(@RequestBody @Valid List<MonitorSleepReqBo> list) {
        sleepService.monitorSleep(list);
        return R.ok();
    }
}
