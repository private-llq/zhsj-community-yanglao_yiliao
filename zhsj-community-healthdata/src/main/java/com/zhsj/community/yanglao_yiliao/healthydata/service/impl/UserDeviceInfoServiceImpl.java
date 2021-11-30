package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserBindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserUnbindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.UserDeviceInfoMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.UserDeviceInfo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.UserDeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .eq(UserDeviceInfo::getMDeviceAddress, bo.getDeviceAddress())
                .eq(UserDeviceInfo::getBind, true));
        if (deviceInfo != null) {
            log.error("The user has bound the device, mDeviceAddress = {}", deviceInfo.getMDeviceAddress());
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

    /***************************************************************************************************************************
     * @description 获取用户最后一次绑定设备信息
     * @author zzm
     * @date 2021/11/13 13:57
     * @param reqBo 用户信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo
     **************************************************************************************************************************/
    @Override
    public DeviceInfoRspBo deviceInfo(DeviceInfoReqBo reqBo) {
        log.info("Get user binding equipment information request parameters, DeviceInfoReqBo = {}", reqBo);
        List<UserDeviceInfo> list = list(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, reqBo.getFamilyMemberId())
                .eq(UserDeviceInfo::getBind, true)
                .orderByDesc(UserDeviceInfo::getCreateTime));
        if (CollectionUtil.isEmpty(list)) {
            log.error("The user has no device bound, familyMemberId = {}", reqBo.getFamilyMemberId());
            throw new BaseException(ErrorEnum.NOT_FOUND_DEVICE);
        }
        return BeanUtil.copyProperties(list.get(0), DeviceInfoRspBo.class);
    }

    /***************************************************************************************************************************
     * @description 获取当前登录用户绑定的设备信息列表
     * @author zzm
     * @date 2021/11/27 17:53
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo>
     **************************************************************************************************************************/
    @Override
    public List<DeviceInfoRspBo> currentLoginUserDeviceInfo() {
        log.info("Get the device information list bound by the current login user");
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<UserDeviceInfo> list = list(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, loginUser.getAccount())
                .eq(UserDeviceInfo::getBind, true)
                .orderByDesc(UserDeviceInfo::getCreateTime));
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.stream().map(
                s -> BeanUtil.copyProperties(s, DeviceInfoRspBo.class)).collect(Collectors.toList());
    }
}
