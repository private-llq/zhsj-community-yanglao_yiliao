package com.zhsj.community.yanglao_yiliao.healthydata.async;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhsj.base.api.rpc.IBaseSmsRpcService;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.base.api.vo.UserImVo;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.community.yanglao_yiliao.common.entity.AgencySosEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.RedisUtils;
import com.zhsj.community.yanglao_yiliao.healthydata.constant.HealthDataConstant;
import com.zhsj.im.chat.api.appmsg.impl.TextAppMsg;
import com.zhsj.im.chat.api.constant.RpcConst;
import com.zhsj.im.chat.api.rpc.IImChatPublicPushRpcService;
import com.zhsj.yanglao_yiliao.interfaces.myself.FamilyRecordFeign;
import com.zhsj.yanglao_yiliao.interfaces.myself.SosFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zzm
 * @version 1.0
 * @Description 异步推送健康数据APP消息以及发送短信
 * @date 2021/12/8 11:35
 */
@Slf4j
@Component
public class AsyncMsg {

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_IM_CHAT, check = false)
    private IImChatPublicPushRpcService iImChatPublicPushRpcService;
    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseSmsRpcService iBaseSmsRpcService;
    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUserInfoRpcService iBaseUserInfoRpcService;
    @Autowired
    private RedisUtils redisService;
    @Autowired
    private FamilyRecordFeign familyRecordFeign;
    @Autowired
    private SosFeign sosFeign;

    @Async
    public void asyncMsg(@NotNull String userId,
                         @NotNull Integer healthDataState) {
        // 判断是否在规定时间内已经推送了消息（一天推两次）
        String uid = (String) redisService.get(HealthDataConstant.HEALTH_DATA_PUSH_MSG + userId);
        if (uid != null) {
            return;
        }
        // 推送APP消息（身体健康状态一般）
        if (healthDataState.equals(HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW)) {
            List<FamilyRecordEntity> list = getFamilyId(userId);
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            for (FamilyRecordEntity family : list) {
                UserImVo eHomeUserIm = iBaseUserInfoRpcService.getEHomeUserIm(family.getUid());
                if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                    log.error("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，familyId = {},UserImVo = {}", family.getUid(), eHomeUserIm);
                    continue;
                }
                iImChatPublicPushRpcService.sendMessage(
                        new TextAppMsg("养老医疗健康数据",
                                "健康数据提醒",
                                "",
                                "",
                                "老人健康数据提醒：您的家人【" + family.getName() + "】数据比较危险，建议近期带他检查身体，多注意一些服药习惯、饮食生活习惯。",
                                null,
                                "pensionMedicalNotice",
                                1,
                                eHomeUserIm.getImId(),
                                null));
            }
        }
        // 发送短信（身体健康状态极差）
        if (healthDataState.equals(HealthDataConstant.HEALTH_COLOR_STATUS_RED)) {
            List<FamilySosEntity> list = getSosUid(userId);
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            for (FamilySosEntity sosEntity : list) {
                if (sosEntity == null || sosEntity.getMobile() == null) {
                    log.error("调用【SosFeign】服务的【selectUser】方法获取用户的SOS通讯录为null, FamilySosEntity = {}", sosEntity);
                    continue;
                }
                iBaseSmsRpcService.sendSms(
                        sosEntity.getMobile(),
                        "纵横世纪",
                        "SMS_228846724",
                        null);
            }
        }
        // 设置推送消息缓存
        redisService.set(HealthDataConstant.HEALTH_DATA_PUSH_MSG + userId, userId, 720L, TimeUnit.MINUTES);
    }

    /**
     * 根据当前登录用户的id获取所有绑定的家人
     */
    private List<FamilyRecordEntity> getFamilyId(@NotNull String loginUserId) {
        log.info("调用【FamilyRecordFeign】服务的【userList】方法获取用户的家人关系");
        ArrayList<FamilyRecordEntity> arr = new ArrayList<>();
        List<FamilyRecordEntity> list = familyRecordFeign.userList(loginUserId).getData();
        if (CollectionUtil.isEmpty(list)) {
            log.error("调用【FamilyRecordFeign】服务的【userList】方法获取用户的家人关系为null, loginUserId = {}", loginUserId);
            return arr;
        }
        for (FamilyRecordEntity familyRecordEntity : list) {
            if (familyRecordEntity != null) {
                if (familyRecordEntity.getRelation() != null && familyRecordEntity.getRelation() == 0) {
                    continue;
                }
                if (familyRecordEntity.getUid() == null) {
                    continue;
                }
                arr.add(familyRecordEntity);
            }
        }
        return arr;
    }

    /**
     * 根据当前登录用户的id获取所有SOS通讯录用户
     */
    private List<FamilySosEntity> getSosUid(@NotNull String loginUserId) {
        log.info("调用【SosFeign】服务的【selectUser】方法获取用户的SOS通讯录");
        Map<String, Object> map = sosFeign.selectUser(loginUserId).getData();
        if (CollectionUtil.isEmpty(map)) {
            log.error("调用【SosFeign】服务的【selectUser】方法获取用户的SOS通讯录为null, loginUserId = {}", loginUserId);
            return null;
        }
        // 获取SOS绑定的家人
        String familyJson = JSON.toJSONString(map.get("familyList"));
        if (familyJson == null) {
            return null;
        }
        // TODO 获取SOS绑定的机构
//        String agencyJson = JSON.toJSONString(map.get("agency"));
//        if (familyJson == null && agencyJson == null) {
//            return null;
//        }
//        if (agencyJson != null) {
//            List<AgencySosEntity> agencySosEntities = JSONArray.parseArray(agencyJson, AgencySosEntity.class);
//        }
        return JSONArray.parseArray(familyJson, FamilySosEntity.class);
    }

//    PublicRegister publicRegister = new PublicRegister("纵横世纪-养老医疗", "pensionMedicalNotice", "养老医疗信息通知", null, null);
//        iImChatPublicPushRpcService.registerPublicPush(publicRegister);

    //                // todo 测试用的，正式需删除--------------------------------------------------------------------------
//                if (familyRecordEntity.getUid().equals("55374") || familyRecordEntity.getUid().equals("666666")
//                        || familyRecordEntity.getUid().equals("55333") || familyRecordEntity.getUid().equals("test0001")
//                        || familyRecordEntity.getUid().equals("test0002")) {
//                    continue;
//                }

}
