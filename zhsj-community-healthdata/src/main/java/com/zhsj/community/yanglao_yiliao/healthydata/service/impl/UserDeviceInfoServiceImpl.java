package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserBindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserUnbindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.UserDeviceInfoMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.UserDeviceInfo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.UserDeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户设备实体service实现层
 * @date 2021/11/10 13:50
 */
@Slf4j
@Service
public class UserDeviceInfoServiceImpl extends ServiceImpl<UserDeviceInfoMapper, UserDeviceInfo> implements UserDeviceInfoService {

    /***************************************************************************************************************************
     * @description 用户绑定设备
     * @author zzm
     * @date 2021/11/10 14:18
     * @param bo 绑定信息
     **************************************************************************************************************************/
    @Override
    public void userBindDevice(UserBindDeviceReqBo bo) {
        log.info("Binding user device parameters,UserBindDeviceReqBo = {}", bo);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        UserDeviceInfo deviceInfo = getOne(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, user.getAccount())
                .eq(UserDeviceInfo::getMDeviceAddress, bo.getDeviceAddress())
                .eq(UserDeviceInfo::getBind, true));
        if (deviceInfo != null) {
            log.error("The user has bound the device, userUuid = {}, mDeviceAddress = {}", deviceInfo.getUserUuid(), deviceInfo.getMDeviceAddress());
            throw new BaseException(ErrorEnum.THE_DEVICE_IS_ALREADY_BOUND);
        }
        UserDeviceInfo userDeviceInfo = UserDeviceInfo.build(user, bo);
        save(userDeviceInfo);
    }

    /***************************************************************************************************************************
     * @description 用户解绑设备
     * @author zzm
     * @date 2021/11/10 15:37
     * @param bo 解绑信息
     **************************************************************************************************************************/
    @Override
    public void userUnbindDevice(UserUnbindDeviceReqBo bo) {
        log.info("User unbinds device parameters,UserUnbindDeviceReqBo = {}", bo);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        UserDeviceInfo deviceInfo = getOne(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, user.getAccount())
                .eq(UserDeviceInfo::getMDeviceAddress, bo.getDeviceAddress())
                .eq(UserDeviceInfo::getBind, true));
        if (deviceInfo == null) {
            log.error("User device does not exist, userUuid = {}, mDeviceAddress = {}", user.getAccount(), bo.getDeviceAddress());
            throw new BaseException(ErrorEnum.NOT_FOUND_DEVICE);
        }
        deviceInfo.setUpdateTime(LocalDateTime.now());
        removeById(deviceInfo.getId());
    }

    // ------------------------------------------------inner-----------------------------------------------------------------

    // token = e50fb3ef-8427-4b4a-8116-77d23845a074   bfb1d0f7-21e0-45ab-bca3-1be16e64f712


}
