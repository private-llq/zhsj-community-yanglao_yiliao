package com.zhsj.community.yanglao_yiliao.myself.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;

import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: Hu
 * @create: 2021-11-11 17:07
 **/
public interface IFamilySosService extends IService<FamilySosEntity> {
    /**
     * @Description: 查询sos家属和机构信息
     * @author: Hu
     * @since: 2021/11/12 10:54
     * @Param: [loginUser]
     * @return: [java.util.Map]
     */
    Map<String, Object> selectByUid(LoginUser loginUser,Long familyId);
}
