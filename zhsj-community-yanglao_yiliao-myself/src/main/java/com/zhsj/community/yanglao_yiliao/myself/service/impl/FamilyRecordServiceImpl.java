package com.zhsj.community.yanglao_yiliao.myself.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.constant.BusinessEnum;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilyRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案
 * @author: Hu
 * @create: 2021-11-10 11:53
 **/
@Service
public class FamilyRecordServiceImpl extends ServiceImpl<FamilyRecordMapper, FamilyRecordEntity> implements IFamilyRecordService {
    @Autowired
    private FamilyRecordMapper familyRecordMapper;


    /**
     * @Description: 查列表
     * @author: Hu
     * @since: 2021/11/10 14:42
     * @Param: [loginUser]
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity>
     */
    @Override
    public List<FamilyRecordEntity> userList(LoginUser loginUser) {
        List<FamilyRecordEntity> list = null;
        list = familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>().eq("uid", loginUser.getCurrentIp()));
        if (list.size()!=0){
            for (FamilyRecordEntity familyRecordEntity : list) {
                if (familyRecordEntity.getRelation()!=0){
                    familyRecordEntity.setRelationText(BusinessEnum.FamilyRelationTextEnum.getName(familyRecordEntity.getRelation()));
                } else {
                    familyRecordEntity.setRelationText("我自己");
                }
            }
            return list;
        }

        FamilyRecordEntity familyRecordEntity = new FamilyRecordEntity();
        familyRecordEntity.setRelation(0);
        familyRecordEntity.setName(loginUser.getNickName());
        familyRecordEntity.setMobile(loginUser.getPhone());
        familyRecordEntity.setSex(0);
        familyRecordEntity.setUid(loginUser.getCurrentIp());
        familyRecordEntity.setId(SnowFlake.nextId());
        familyRecordEntity.setCreateTime(LocalDateTime.now());
        list = new LinkedList<>();
        list.add(familyRecordEntity);
        return list;
    }
}
