package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.AgencySosEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.AgencySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilySosService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private AgencySosMapper agencySosMapper;


    @Override
    public void sos(LoginUser loginUser,Long familyId) {
        List<FamilySosEntity> sosEntities = familySosMapper.selectList(new QueryWrapper<FamilySosEntity>().eq("family_id", familyId));
        if (sosEntities.size()!=0){
            for (FamilySosEntity sosEntity : sosEntities) {
                //发送短信
            }
        }
        AgencySosEntity sosEntity = agencySosMapper.selectOne(new QueryWrapper<AgencySosEntity>().eq("family_id", familyId));
        if (sosEntity != null) {
            //查询机构  发送短信
        }
    }

    /**
     * @Description: 查询sos家属和机构信息
     * @author: Hu
     * @since: 2021/11/12 11:03
     * @Param: [loginUser]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> selectByUid(LoginUser loginUser,Long familyId) {
        Map<String, Object> map = new HashMap<>(2);
        List<FamilySosEntity> entityList = familySosMapper.selectList(new QueryWrapper<FamilySosEntity>().eq("uid", loginUser.getAccount()).eq("family_id",familyId));
        AgencySosEntity sosEntity = agencySosMapper.selectOne(new QueryWrapper<AgencySosEntity>().eq("uid", loginUser.getAccount()).eq("family_id",familyId));
        if (sosEntity!=null){
            //查询机构
        }
        map.put("familyList",map);
        map.put("agency",null);
        return map;
    }
}
