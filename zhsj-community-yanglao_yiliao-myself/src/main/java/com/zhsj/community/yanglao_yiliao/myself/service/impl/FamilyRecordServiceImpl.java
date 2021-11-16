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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
        FamilyRecordEntity recordEntity = familyRecordMapper.selectOne(new QueryWrapper<FamilyRecordEntity>().eq("uid", loginUser.getCurrentIp()).eq("relation", 0));
        if (Objects.isNull(recordEntity)) {
            FamilyRecordEntity familyRecordEntity = new FamilyRecordEntity();
            familyRecordEntity.setRelation(0);
            familyRecordEntity.setRelationText("我自己");
            familyRecordEntity.setName(loginUser.getNickName());
            familyRecordEntity.setMobile(loginUser.getPhone());
            familyRecordEntity.setUid(loginUser.getCurrentIp());
            familyRecordEntity.setId(SnowFlake.nextId());
            familyRecordEntity.setCreateTime(LocalDateTime.now());
            familyRecordMapper.insert(familyRecordEntity);
        }

        list = familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>().eq("uid", loginUser.getCurrentIp()));
        if (list.size()!=0){
            for (FamilyRecordEntity familyRecordEntity : list) {
                if (familyRecordEntity.getRelation()!=0){
                    familyRecordEntity.setRelationText(BusinessEnum.FamilyRelationTextEnum.getName(familyRecordEntity.getRelation()));
                } else {
                    familyRecordEntity.setRelationText("我自己");
                }

                if (StringUtils.isEmpty(familyRecordEntity.getAvatarUrl())||
                        StringUtils.isEmpty(familyRecordEntity.getName())||
                        StringUtils.isEmpty(familyRecordEntity.getIdCard())||
                        StringUtils.isEmpty(familyRecordEntity.getBirthday())||
                        StringUtils.isEmpty(familyRecordEntity.getSex())||
                        StringUtils.isEmpty(familyRecordEntity.getRelation())||
                        StringUtils.isEmpty(familyRecordEntity.getMobile())) {

                    familyRecordEntity.setStatus(0);

                } else {
                    familyRecordEntity.setStatus(1);
                }
            }
            return list;
        }

        return list;
    }
}
