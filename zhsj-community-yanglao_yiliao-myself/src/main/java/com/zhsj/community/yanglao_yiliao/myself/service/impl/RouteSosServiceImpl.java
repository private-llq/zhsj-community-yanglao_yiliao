package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.RouteSosEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.RouteSosMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IRouteSosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos我要找路
 * @author: Hu
 * @create: 2021-11-30 15:07
 **/
@Service
public class RouteSosServiceImpl extends ServiceImpl<RouteSosMapper, RouteSosEntity> implements IRouteSosService {

    @Resource
    private RouteSosMapper routeSosMapper;


    /**
     * @Description: 新增修改我要找路终点地址
     * @author: Hu
     * @since: 2021/11/30 15:13
     * @Param: [routeSosEntity]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoute(RouteSosEntity routeSosEntity, LoginUser loginUser) {
        RouteSosEntity entity = routeSosMapper.selectOne(new QueryWrapper<RouteSosEntity>().eq("uid", loginUser.getAccount()));
        if (entity!=null){
            entity.setLat(routeSosEntity.getLat());
            entity.setLon(routeSosEntity.getLon());
            routeSosMapper.updateById(entity);
        } else {
            routeSosEntity.setUid(loginUser.getAccount());
            routeSosEntity.setId(SnowFlake.nextId());
            routeSosEntity.setUpdateTime(LocalDateTime.now());
            routeSosMapper.insert(routeSosEntity);
        }

    }
}
