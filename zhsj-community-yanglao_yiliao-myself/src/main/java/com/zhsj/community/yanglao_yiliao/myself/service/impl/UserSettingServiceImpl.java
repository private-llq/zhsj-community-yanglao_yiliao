package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.UserSettingEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.UserSettingMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IUserSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 个人设置
 * @author: Hu
 * @create: 2021-11-25 16:15
 **/
@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSettingEntity> implements IUserSettingService {
    @Resource
    private UserSettingMapper userSettingMapper;


    /**
     * @Description: 查询个人设置
     * @author: Hu
     * @since: 2021/11/25 16:21
     * @Param: [user]
     * @return: com.zhsj.community.yanglao_yiliao.common.entity.UserSettingEntity
     */
    @Override
    public UserSettingEntity getSetting(LoginUser user) {
        UserSettingEntity entity = userSettingMapper.selectOne(new QueryWrapper<UserSettingEntity>().eq("uid", user.getAccount()));
        if (Objects.isNull(entity)){
            entity = new UserSettingEntity();
            entity.setId(SnowFlake.nextId());
            entity.setUid(user.getAccount());
            entity.setNotificationStatus(1);
            entity.setShakeStatus(1);
            entity.setCreateTime(LocalDateTime.now());
            userSettingMapper.insert(entity);
            return entity;
        } else {
            return entity;
        }
    }


}
