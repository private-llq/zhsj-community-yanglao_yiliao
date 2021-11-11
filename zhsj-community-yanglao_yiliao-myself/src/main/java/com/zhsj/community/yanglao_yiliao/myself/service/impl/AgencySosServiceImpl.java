package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.community.yanglao_yiliao.common.entity.AgencySosEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.AgencySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IAgencySosService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos机构信息
 * @author: Hu
 * @create: 2021-11-11 14:10
 **/
@Service
public class AgencySosServiceImpl extends ServiceImpl<AgencySosMapper, AgencySosEntity> implements IAgencySosService {

    @Resource
    private AgencySosMapper agencySosMapper;


}
