package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.utils.PageVo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.*;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.UserDeviceInfoMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.UserDeviceInfo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.UserDeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUserInfoRpcService iBaseUserInfoRpcService;

    /***************************************************************************************************************************
     * @description 用户绑定切换设备
     * @author zzm
     * @date 2021/11/10 14:18
     * @param bo 绑定信息
     **************************************************************************************************************************/
    @Override
    public void userBindDevice(UserBindDeviceReqBo bo) {
        log.info("用户绑定切换设备请求参数, UserBindDeviceReqBo = {}", bo);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        UserDeviceInfo deviceInfo = getOne(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, user.getAccount())
                .eq(UserDeviceInfo::getMDeviceAddress, bo.getDeviceAddress())
                .eq(UserDeviceInfo::getBind, true));
        if (deviceInfo == null) {
            UserDeviceInfo userDeviceInfo = UserDeviceInfo.build(user, bo);
            save(userDeviceInfo);
        } else {
            deviceInfo.setUpdateTime(LocalDateTime.now());
            updateById(deviceInfo);
        }

    }

    /***************************************************************************************************************************
     * @description 用户解绑设备
     * @author zzm
     * @date 2021/11/10 15:37
     * @param bo 解绑信息
     **************************************************************************************************************************/
    @Override
    public void userUnbindDevice(UserUnbindDeviceReqBo bo) {
        log.info("用户解绑设备请求参数, UserUnbindDeviceReqBo = {}", bo);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        UserDeviceInfo deviceInfo = getOne(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, user.getAccount())
                .eq(UserDeviceInfo::getMDeviceAddress, bo.getDeviceAddress())
                .eq(UserDeviceInfo::getBind, true));
        if (deviceInfo == null) {
            log.error("要解绑定的设备不存在, userUuid = {}, mDeviceAddress = {}", user.getAccount(), bo.getDeviceAddress());
            throw new BaseException(ErrorEnum.NOT_FOUND_DEVICE);
        }
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
        log.info("获取用户最后一次绑定设备信息请求参数, DeviceInfoReqBo = {}", reqBo);
        List<UserDeviceInfo> list = list(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, reqBo.getFamilyMemberId())
                .eq(UserDeviceInfo::getBind, true)
                .orderByDesc(UserDeviceInfo::getUpdateTime));
        if (CollectionUtil.isEmpty(list)) {
            log.info("该用户没有绑定任何设备, familyMemberId = {}", reqBo.getFamilyMemberId());
            return null;
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
        log.info("获取当前登录用户绑定的设备信息列表");
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<UserDeviceInfo> list = list(new LambdaQueryWrapper<UserDeviceInfo>()
                .eq(UserDeviceInfo::getUserUuid, loginUser.getAccount())
                .eq(UserDeviceInfo::getBind, true)
                .orderByDesc(UserDeviceInfo::getUpdateTime));
        if (CollectionUtil.isEmpty(list)) {
            log.info("该用户没有绑定任何设备, familyMemberId = {}", loginUser.getAccount());
            return null;
        }
        return list.stream().map(
                s -> BeanUtil.copyProperties(s, DeviceInfoRspBo.class)).collect(Collectors.toList());
    }

    // --------------------------------------------------------后台管理接口-----------------------------------------------------

    /***************************************************************************************************************************
     * @description 大后台医疗养老设备管理-获取用户绑定设备列表
     * @author zzm
     * @date 2021/12/6 14:50
     * @param reqBo 查询参数
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceListRspBo>
     **************************************************************************************************************************/
    @Override
    public PageVo<DeviceListRspBo> deviceList(DeviceListReqBo reqBo) {
        log.info("大后台医疗养老设备管理-获取用户绑定设备列表, DeviceListReqBo = {}", reqBo);
        Page<UserDeviceInfo> page = page(new Page<>(reqBo.getPageNo(), reqBo.getPageSize()),
                new QueryWrapper<UserDeviceInfo>()
                        .like(StrUtil.isNotBlank(reqBo.getDeviceName()), "m_device_name", reqBo.getDeviceName())
                        .like(StrUtil.isNotBlank(reqBo.getDeviceAddress()), "m_device_address", reqBo.getDeviceAddress())
                        .eq("bind", true)
                        .orderByDesc("create_time"));
        Page<DeviceListRspBo> rspBoPage = new Page<>();
        rspBoPage.setCurrent(page.getCurrent())
                .setSize(page.getSize())
                .setPages(page.getPages())
                .setTotal(page.getTotal());
        ArrayList<DeviceListRspBo> arr = new ArrayList<>();
        if (CollectionUtil.isEmpty(page.getRecords())) {
            rspBoPage.setRecords(arr);
        } else {
            for (UserDeviceInfo deviceInfo : page.getRecords()) {
                DeviceListRspBo deviceListRspBo = new DeviceListRspBo();
                BeanUtils.copyProperties(deviceInfo, deviceListRspBo);
                UserDetail userDetail = iBaseUserInfoRpcService.getUserDetail(deviceInfo.getUserUuid());
                deviceListRspBo.setNickName(userDetail.getNickName());
                deviceListRspBo.setSex(userDetail.getSex());
                deviceListRspBo.setAge(userDetail.getAge());
                deviceListRspBo.setPhone(userDetail.getPhone());
                deviceListRspBo.setFilingTime(formatTime(deviceInfo.getCreateTime()));
                arr.add(deviceListRspBo);
            }
        }
        rspBoPage.setRecords(arr);
        return PageVo.newPageVO(rspBoPage);
    }

    /***************************************************************************************************************************
     * @description 大后台医疗养老设备管理-删除用户设备绑定信息
     * @author zzm
     * @date 2021/12/6 17:21
     * @param ids 绑定设备的id集合
     **************************************************************************************************************************/
    @Override
    public void deleteDevice(List<Long> ids) {
        log.info("大后台医疗养老设备管理-删除用户设备绑定信息, ids = {}", ids);
        if (CollectionUtil.isEmpty(ids)) {
            log.error("请求参数为空");
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            UserDeviceInfo userDeviceInfo = getOne(new LambdaQueryWrapper<UserDeviceInfo>().eq(UserDeviceInfo::getId, id).eq(UserDeviceInfo::getBind, true));
            if (userDeviceInfo == null) {
                log.error("要删除的用户设备不存在, id = {}", id);
                continue;
            }
            list.add(id);
        }
        if (CollectionUtil.isNotEmpty(list)) {
            removeByIds(list);
        }
    }

    /**
     * 格式化时间（yyyy-MM-dd HH:mm）
     */
    private String formatTime(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }
}





