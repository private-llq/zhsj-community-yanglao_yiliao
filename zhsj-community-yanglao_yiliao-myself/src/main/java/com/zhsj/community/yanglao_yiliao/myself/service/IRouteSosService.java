package com.zhsj.community.yanglao_yiliao.myself.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.RouteSosEntity;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos我要找路
 * @author: Hu
 * @create: 2021-11-30 15:07
 **/
public interface IRouteSosService extends IService<RouteSosEntity> {

    /**
     * @Description: 新增修改我要找路终点经纬度
     * @author: Hu
     * @since: 2021/11/30 15:12
     * @Param:
     * @return:
     */
    void saveRoute(RouteSosEntity routeSosEntity, LoginUser loginUser);
}
