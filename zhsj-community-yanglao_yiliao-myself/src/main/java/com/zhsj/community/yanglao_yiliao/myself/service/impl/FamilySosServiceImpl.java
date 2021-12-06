package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.rpc.IBaseSmsRpcService;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.AgencySosEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.AgencySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilyRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilySosService;
import org.apache.dubbo.config.annotation.DubboReference;
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
    private FamilyRecordMapper familyRecordMapper;

    @Resource
    private AgencySosMapper agencySosMapper;

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER)
    private IBaseSmsRpcService baseSmsRpcService;


    @Override
    public void sos(LoginUser loginUser,Long familyId) {
        FamilyRecordEntity recordEntity = familyRecordMapper.selectById(familyId);

        List<FamilySosEntity> sosEntities = familySosMapper.selectList(new QueryWrapper<FamilySosEntity>().eq("family_id", familyId));
        if (sosEntities.size()!=0){
            //发送短信
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name",recordEntity.getName());
            for (FamilySosEntity sosEntity : sosEntities) {
                baseSmsRpcService.sendSms(sosEntity.getMobile(),"纵横世纪","SMS_229095430",hashMap);
            }
        }
        AgencySosEntity sosEntity = agencySosMapper.selectOne(new QueryWrapper<AgencySosEntity>().eq("family_id", familyId));
        if (sosEntity != null) {
            //发送短信
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name1",recordEntity.getName());
            hashMap.put("name2",recordEntity.getName());
            //查询机构  发送短信
            baseSmsRpcService.sendSms("","纵横世纪","SMS_228851666",hashMap);
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
        List<FamilySosEntity> entityList = familySosMapper.selectList(new QueryWrapper<FamilySosEntity>().eq("uid", loginUser.getAccount()));
        AgencySosEntity sosEntity = agencySosMapper.selectOne(new QueryWrapper<AgencySosEntity>().eq("uid", loginUser.getAccount()));
        if (sosEntity!=null){
            //查询机构
        } else {
            sosEntity = new AgencySosEntity();
            sosEntity.setAgencyId(123L);
            sosEntity.setAgencyText("流光福诊所");
            sosEntity.setAgencyMobile("15102345678");
            sosEntity.setFamilyId(familyId);
        }
        map.put("familyList",entityList);
        map.put("agency",sosEntity);
        return map;
    }
}
