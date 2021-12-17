package com.zhsj.community.yanglao_yiliao.myself.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseAuthRpcService;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.constant.BusinessEnum;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.qo.FamilysQo;
import com.zhsj.community.yanglao_yiliao.common.utils.BaseQo;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilyRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilyRecordService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

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

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER)
    private IBaseAuthRpcService baseAuthRpcService;

    /**
     * @Description: 添加家人档案
     * @author: Hu
     * @since: 2021/11/30 14:01
     * @Param: [familyRecordEntity]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(FamilyRecordEntity familyRecordEntity,LoginUser loginUser) {
        FamilyRecordEntity entity = familyRecordMapper.selectOne(new QueryWrapper<FamilyRecordEntity>().eq("uid", loginUser.getAccount()).eq("relation", 0));

        //注册用户
       UserDetail userDetail = baseAuthRpcService.eHomeUserPhoneRegister(familyRecordEntity.getMobile());

        familyRecordEntity.setId(SnowFlake.nextId());
        familyRecordEntity.setUid(userDetail.getAccount());
        familyRecordEntity.setCreateTime(LocalDateTime.now());
        familyRecordEntity.setCreateUid(loginUser.getAccount());
        familyRecordMapper.insert(familyRecordEntity);

        FamilyRecordEntity recordEntity = new FamilyRecordEntity();
        recordEntity.setName(entity.getName());
        recordEntity.setMobile(entity.getMobile());
        recordEntity.setUid(entity.getUid());
        recordEntity.setCreateUid(userDetail.getAccount());
        recordEntity.setId(SnowFlake.nextId());
        recordEntity.setCreateTime(LocalDateTime.now());
        familyRecordMapper.insert(recordEntity);
    }

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
        FamilyRecordEntity recordEntity = familyRecordMapper.selectOne(new QueryWrapper<FamilyRecordEntity>().eq("uid", loginUser.getAccount()).eq("relation", 0));
        if (Objects.isNull(recordEntity)) {
            FamilyRecordEntity familyRecordEntity = new FamilyRecordEntity();
            familyRecordEntity.setRelation(0);
            familyRecordEntity.setRelationText("我自己");
            familyRecordEntity.setName(loginUser.getNickName());
            familyRecordEntity.setMobile(loginUser.getPhone());
            familyRecordEntity.setUid(loginUser.getAccount());
            familyRecordEntity.setId(SnowFlake.nextId());
            familyRecordEntity.setCreateUid(loginUser.getAccount());
            familyRecordEntity.setCreateTime(LocalDateTime.now());
            familyRecordMapper.insert(familyRecordEntity);
        }

        list = familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>().eq("create_uid", loginUser.getAccount()));
        if (list.size()!=0){
            for (FamilyRecordEntity familyRecordEntity : list) {
                if (familyRecordEntity.getRelation()!=null){
                    if (familyRecordEntity.getRelation()!=0){
                        familyRecordEntity.setRelationText(BusinessEnum.FamilyRelationTextEnum.getName(familyRecordEntity.getRelation()));
                        familyRecordEntity.setOneself(0);
                    } else {
                        familyRecordEntity.setRelationText("我自己");
                        familyRecordEntity.setOneself(1);
                    }
                }

                if (StringUtils.isEmpty(familyRecordEntity.getAvatarUrl())||
                        StringUtils.isEmpty(familyRecordEntity.getName())||
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



    /**
     * @Description: 导入社区房间成员
     * @author: Hu
     * @since: 2021/12/2 16:23
     * @Param: [familysQo]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importFamily(FamilysQo familysQo,LoginUser loginUser) {
        List<FamilyRecordEntity> list = new LinkedList<>();
        FamilyRecordEntity familyRecordEntity = null;

        for (FamilysQo family : familysQo.getFamilies()) {
            FamilyRecordEntity recordEntity = familyRecordMapper.selectOne(new QueryWrapper<FamilyRecordEntity>().eq("create_uid", loginUser.getAccount()).eq("mobile", family.getMobile()));
            if (Objects.isNull(recordEntity)){
                if (!"".equals(family.getUid())&&family.getUid()!=null){
                    familyRecordEntity = new FamilyRecordEntity();
                    familyRecordEntity.setId(SnowFlake.nextId());
                    familyRecordEntity.setMobile(family.getMobile());
                    familyRecordEntity.setName(family.getName());
                    familyRecordEntity.setUid(family.getUid());
                    familyRecordEntity.setCreateUid(loginUser.getAccount());
                    list.add(familyRecordEntity);

                    familyRecordEntity = new FamilyRecordEntity();
                    familyRecordEntity.setName(family.getName());
                    familyRecordEntity.setMobile(family.getMobile());
                    familyRecordEntity.setUid(loginUser.getAccount());
                    familyRecordEntity.setCreateUid(family.getUid());
                    familyRecordEntity.setId(SnowFlake.nextId());
                    list.add(familyRecordEntity);
                } else {
                    //注册用户
                    UserDetail userDetail = baseAuthRpcService.eHomeUserPhoneRegister(family.getMobile());

                    familyRecordEntity = new FamilyRecordEntity();
                    familyRecordEntity.setId(SnowFlake.nextId());
                    familyRecordEntity.setName(family.getName());
                    familyRecordEntity.setMobile(family.getMobile());
                    familyRecordEntity.setUid(userDetail.getAccount());
                    familyRecordEntity.setCreateUid(loginUser.getAccount());
                    list.add(familyRecordEntity);

                    familyRecordEntity = new FamilyRecordEntity();
                    familyRecordEntity.setName(family.getName());
                    familyRecordEntity.setMobile(family.getMobile());
                    familyRecordEntity.setUid(loginUser.getAccount());
                    familyRecordEntity.setCreateUid(userDetail.getAccount());
                    familyRecordEntity.setId(SnowFlake.nextId());
                    list.add(familyRecordEntity);
                }
            }
        }
        if (list.size()!=0){
            familyRecordMapper.saveAll(list);
        }
    }



    /**
     * @Description: 查询用户家人集合
     * @author: Hu
     * @since: 2021/12/8 15:26
     * @Param: [uid]
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity>
     */
    @Override
    public List<FamilyRecordEntity> userByList(String uid) {
        List<FamilyRecordEntity> recordEntities = familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>().eq("create_uid", uid));
        return recordEntities;
    }



    /**
     * @Description: 大后台分页查询
     * @author: Hu
     * @since: 2021/12/17 16:11
     * @Param: [qo]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> selectPage(BaseQo<String> qo) {
        QueryWrapper<FamilyRecordEntity> wrapper = new QueryWrapper<>();
        if (qo.getQuery()!=null){
            wrapper.like("name",qo.getQuery());
        }
        Page<FamilyRecordEntity> page = familyRecordMapper.selectPage(new Page<FamilyRecordEntity>(qo.getPage(), qo.getSize()), wrapper);
        List<FamilyRecordEntity> records = page.getRecords();
        for (FamilyRecordEntity record : records) {
            List<FamilyRecordEntity> list = familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>().eq("create_uid", record.getUid()));
            for (int i = 0; i < list.size()-1; i++) {
                record.setIncidenceRelation(list.get(i).getName());
                if (i!=list.size()-1){
                    record.setIncidenceRelation(",");
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total",page.getTotal());
        map.put("list",records);
        return map;
    }
}
