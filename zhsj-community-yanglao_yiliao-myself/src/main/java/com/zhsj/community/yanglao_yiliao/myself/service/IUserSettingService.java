package com.zhsj.community.yanglao_yiliao.myself.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.UserSettingEntity;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 个人设置
 * @author: Hu
 * @create: 2021-11-25 16:14
 **/
public interface IUserSettingService extends IService<UserSettingEntity> {
    /**
     * @Description: 查询个人设置
     * @author: Hu
     * @since: 2021/11/25 16:17
     * @Param:
     * @return:
     */
    UserSettingEntity getSetting(LoginUser user);
}
