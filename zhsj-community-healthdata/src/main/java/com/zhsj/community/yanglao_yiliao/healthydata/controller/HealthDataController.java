package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataRspBo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.HealthDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户健康数据控制层
 * @date 2021/11/12 14:49
 */
@RestController
@RequestMapping("/healthData")
public class HealthDataController {

    @Autowired
    private HealthDataService healthDataService;

    @PostMapping("/realTimeHealthData")
    public R<RealTimeHealthDataRspBo> realTimeHealthData(@RequestBody @Valid RealTimeHealthDataReqBo reqBo) {
        healthDataService.realTimeHealthData(reqBo);
        return R.ok();
    }



}
