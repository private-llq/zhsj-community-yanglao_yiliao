package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilySosService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos家属信息
 * @author: Hu
 * @create: 2021-11-11 17:07
 **/
@Service
public class FamilySosServiceImpl extends ServiceImpl<FamilySosMapper, FamilySosEntity> implements IFamilySosService {

    @Resource
    private FamilySosMapper familySosMapper;

}
