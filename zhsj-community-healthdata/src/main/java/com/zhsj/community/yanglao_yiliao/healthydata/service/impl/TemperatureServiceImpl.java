package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorTemperatureReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.TemperatureMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Temperature;
import com.zhsj.community.yanglao_yiliao.healthydata.service.TemperatureService;
import com.zhsj.community.yanglao_yiliao.healthydata.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户温度service层对应实现
 * @date 2021/11/11 13:57
 */
@Slf4j
@Service
public class TemperatureServiceImpl extends ServiceImpl<TemperatureMapper, Temperature> implements TemperatureService {

    /***************************************************************************************************************************
     * @description 检测用户体温并保存
     * @author zzm
     * @date 2021/11/11 14:08
     * @param list 用户体温信息
     **************************************************************************************************************************/
    @Override
    public void monitorTemperature(List<MonitorTemperatureReqBo> list) {
        log.info("Monitor user temperature request parameters,List<MonitorTemperatureReqBo> = {}", list);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        List<Temperature> arr = new ArrayList<Temperature>();
        for (MonitorTemperatureReqBo reqBo : list) {
            LocalDateTime localDateTime = TimeUtils.formatTimestamp(reqBo.getCreateTime());
            Temperature temperature = getOne(new LambdaQueryWrapper<Temperature>()
                    .eq(Temperature::getUserUuid, user.getAccount())
                    .eq(Temperature::getCreateTime, localDateTime)
                    .eq(Temperature::getDeleted, true));
            if (temperature != null) {
                continue;
            }
            arr.add(Temperature.build(user, reqBo, localDateTime));
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            saveBatch(arr);
        }
    }

    /***************************************************************************************************************************
     * @description 批量删除用户体温数据
     * @author zzm
     * @date 2021/11/11 17:19
     * @param list 体温id列表
     **************************************************************************************************************************/
    @Override
    public void batchDeleteTemperature(List<Long> list) {
        log.info("Delete user temperature in batch request parameter, list = {}", list);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        if (CollectionUtil.isEmpty(list)) {
            log.error("Please check the body temperature to be deleted");
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        for (Long id : list) {
            Temperature temperature = getOne(new LambdaQueryWrapper<Temperature>()
                    .eq(Temperature::getId, id)
                    .eq(Temperature::getDeleted, true)
                    .eq(Temperature::getUserUuid, user.getAccount()));
            if (temperature == null) {
                log.error("Body temperature to delete not found, temperatureId = {}", id);
                throw new BaseException(ErrorEnum.SERVER_BUSY);
            }
        }
        removeByIds(list);
    }
}
