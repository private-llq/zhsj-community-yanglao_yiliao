package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseAuthRpcService;
import com.zhsj.base.api.rpc.IBaseUpdateUserRpcService;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.base.api.vo.PageVO;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.constant.BusinessEnum;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.qo.FamilysQo;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.bo.FamilyRecordPageReqBo;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilyRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案
 * @author: Hu
 * @create: 2021-11-10 11:53
 **/
@Slf4j
@Service
public class FamilyRecordServiceImpl extends ServiceImpl<FamilyRecordMapper, FamilyRecordEntity> implements IFamilyRecordService {
    @Autowired
    private FamilyRecordMapper familyRecordMapper;

    @DubboReference(version = com.zhsj.im.chat.api.constant.RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseAuthRpcService baseAuthRpcService;

    @DubboReference(version = com.zhsj.im.chat.api.constant.RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUserInfoRpcService userInfoRpcService;
    @DubboReference(version = com.zhsj.im.chat.api.constant.RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUpdateUserRpcService iBaseUpdateUserRpcService;

    /**
     * @Description: 添加家人档案
     * @author: Hu
     * @since: 2021/11/30 14:01
     * @Param: [familyRecordEntity]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(FamilyRecordEntity familyRecordEntity, LoginUser loginUser) {
        log.info("添加家人档案, FamilyRecordEntity = {}, LoginUser = {}", familyRecordEntity, loginUser);
        UserDetail detail = userInfoRpcService.getUserDetailByPhone(familyRecordEntity.getMobile());
        if (detail == null) {
            // ---注册用户
            detail = baseAuthRpcService.eHomeUserPhoneRegister(familyRecordEntity.getMobile());
        }
        iBaseUpdateUserRpcService.updateUserInfo(detail.getId(), familyRecordEntity.getName(), null, (Integer) null, null);
        familyRecordEntity.setId(SnowFlake.nextId());
        familyRecordEntity.setUid(detail.getAccount());
        familyRecordEntity.setCreateTime(LocalDateTime.now());
        familyRecordEntity.setCreateUid(loginUser.getAccount());
        familyRecordEntity.setMobile(null);
        familyRecordEntity.setName(null);
        familyRecordMapper.insert(familyRecordEntity);
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
        log.info("查询家人档案列表userList，userId = {}", loginUser.getAccount());
        List<FamilyRecordEntity> list = null;
        FamilyRecordEntity recordEntity = familyRecordMapper.selectOne(new QueryWrapper<FamilyRecordEntity>()
                .eq("create_uid", loginUser.getAccount())
                .eq("relation", 0)
                .eq("deleted", 0));
        if (Objects.isNull(recordEntity)) {
            FamilyRecordEntity familyRecordEntity = new FamilyRecordEntity();
            familyRecordEntity.setRelation(0);
            familyRecordEntity.setRelationText("我自己");
            familyRecordEntity.setUid(loginUser.getAccount());
            familyRecordEntity.setId(SnowFlake.nextId());
            familyRecordEntity.setCreateUid(loginUser.getAccount());
            familyRecordEntity.setCreateTime(LocalDateTime.now());
            familyRecordMapper.insert(familyRecordEntity);
        }

        list = familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>()
                .eq("create_uid", loginUser.getAccount())
                .eq("deleted", 0));
        if (CollectionUtil.isNotEmpty(list)) {
            for (FamilyRecordEntity familyRecordEntity : list) {
                UserDetail detail = userInfoRpcService.getUserDetail(familyRecordEntity.getUid());
                if (detail == null) {
                    log.error("根据用户account没有查询到相应的UserDetail，用户有可能已经被注销,或用户uid数据可能被修改");
                    continue;
                }
                familyRecordEntity.setName(detail.getNickName());
                familyRecordEntity.setSex(detail.getSex());
                familyRecordEntity.setAvatarUrl(detail.getAvatarThumbnail());
                familyRecordEntity.setMobile(detail.getPhone());
                if (StrUtil.isNotBlank(detail.getBirthday())) {
                    familyRecordEntity.setBirthday(LocalDate.parse(detail.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }

                if (familyRecordEntity.getRelation() != null) {
                    if (familyRecordEntity.getRelation() != 0) {
                        familyRecordEntity.setRelationText(BusinessEnum.FamilyRelationTextEnum.getName(familyRecordEntity.getRelation()));
                        familyRecordEntity.setOneself(0);
                    } else {
                        familyRecordEntity.setRelationText("我自己");
                        familyRecordEntity.setOneself(1);
                    }
                }

                if (familyRecordEntity.getRelation() != null && familyRecordEntity.getRelation() == 0) {
                    if (StringUtils.isEmpty(familyRecordEntity.getAvatarUrl()) ||
                            StringUtils.isEmpty(familyRecordEntity.getName()) ||
                            StringUtils.isEmpty(familyRecordEntity.getBirthday()) ||
                            StringUtils.isEmpty(familyRecordEntity.getSex()) ||
                            StringUtils.isEmpty(familyRecordEntity.getRelation()) ||
                            StringUtils.isEmpty(familyRecordEntity.getMobile())) {
                        familyRecordEntity.setStatus(0);
                    } else {
                        familyRecordEntity.setStatus(1);
                    }
                } else {
                    if (StringUtils.isEmpty(familyRecordEntity.getRelation())) {
                        familyRecordEntity.setStatus(0);
                    } else {
                        familyRecordEntity.setStatus(1);
                    }
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
    public void importFamily(FamilysQo familysQo, LoginUser loginUser) {
        log.info("导入社区房间成员, FamilysQo = {}, LoginUser = {}", familysQo, loginUser);
        List<FamilyRecordEntity> list = new LinkedList<>();
        for (FamilysQo family : familysQo.getFamilies()) {
            if (StrUtil.isNotBlank(family.getUid())) {
                FamilyRecordEntity recordEntity = familyRecordMapper.selectOne(new QueryWrapper<FamilyRecordEntity>()
                        .eq("create_uid", loginUser.getAccount())
                        .eq("uid", family.getUid())
                        .eq("deleted", 0));
                if (recordEntity == null) {
                    FamilyRecordEntity familyRecordEntity = new FamilyRecordEntity();
                    familyRecordEntity.setId(SnowFlake.nextId());
                    familyRecordEntity.setUid(family.getUid());
                    familyRecordEntity.setCreateUid(loginUser.getAccount());
                    list.add(familyRecordEntity);
                }
            } else {
                UserDetail detail = userInfoRpcService.getUserDetailByPhone(family.getMobile());
                if (detail == null) {
                    detail = baseAuthRpcService.eHomeUserPhoneRegister(family.getMobile());
                }
                FamilyRecordEntity familyRecordEntity = new FamilyRecordEntity();
                familyRecordEntity.setId(SnowFlake.nextId());
                familyRecordEntity.setUid(detail.getAccount());
                familyRecordEntity.setCreateUid(loginUser.getAccount());
                list.add(familyRecordEntity);
            }
        }
        if (list.size() != 0) {
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
        return familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>()
                .eq("create_uid", uid).eq("deleted", 0));
    }


    /**
     * @Description: 大后台分页查询家庭关系列表
     * @author: Hu
     * @since: 2021/12/17 16:11
     * @Param: [qo]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    @Override
    public Map<String, Object> selectPage(FamilyRecordPageReqBo reqBo) {
        log.info("大后台分页查询家庭关系列表, FamilyRecordPageReqBo = {}", reqBo);
        // ---模糊查询条件是空
        Map<String, Object> map = new HashMap<>(5);
        if (StrUtil.isBlank(reqBo.getName())) {
            Page<FamilyRecordEntity> page = page(new Page<FamilyRecordEntity>(reqBo.getPageNo(), reqBo.getPageSize()),
                    new QueryWrapper<FamilyRecordEntity>().eq("deleted", 0));
            List<FamilyRecordEntity> records = page.getRecords();
            return resultFormat(records, map, page);
        } else {
            // ---模糊查询条件不是空
            PageVO<UserDetail> pageVO = userInfoRpcService.queryUser(null, reqBo.getName(), 1, 100000);
            List<UserDetail> list = pageVO.getData();
            if (CollectionUtil.isEmpty(list)) {
                map.put("totalCount", 0);
                map.put("totalPage", 0);
                map.put("currentPage", reqBo.getPageNo());
                map.put("pageSize", reqBo.getPageSize());
                map.put("list", new ArrayList<>());
                return map;
            } else {
                ArrayList<String> arr = new ArrayList<>();
                for (UserDetail userDetail : list) {
                    arr.add(userDetail.getAccount());
                }
                Page<FamilyRecordEntity> page = page(new Page<FamilyRecordEntity>(reqBo.getPageNo(), reqBo.getPageSize()),
                        new QueryWrapper<FamilyRecordEntity>().eq("deleted", 0).in("uid", arr));
                List<FamilyRecordEntity> records = page.getRecords();
                return resultFormat(records, map, page);
            }
        }
    }

    // 格式化返回值
    private Map<String, Object> resultFormat(List<FamilyRecordEntity> records,
                                             Map<String, Object> map,
                                             Page<FamilyRecordEntity> page) {
        if (CollectionUtil.isEmpty(records)) {
            map.put("totalCount", page.getTotal());
            map.put("totalPage", page.getPages());
            map.put("currentPage", page.getCurrent());
            map.put("pageSize", page.getSize());
            map.put("list", new ArrayList<>());
            return map;
        } else {
            for (FamilyRecordEntity familyRecordEntity : records) {
                UserDetail detail = userInfoRpcService.getUserDetail(familyRecordEntity.getUid());
                if (detail == null) {
                    log.error("根据用户account没有查询到相应的UserDetail，用户有可能已经被注销,或用户uid数据可能被修改");
                    continue;
                }
                familyRecordEntity.setName(detail.getNickName());
                familyRecordEntity.setMobile(detail.getPhone());
                if (StrUtil.isNotBlank(detail.getBirthday())) {
                    familyRecordEntity.setBirthday(LocalDate.parse(detail.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                familyRecordEntity.setSex(detail.getSex());
                familyRecordEntity.setAvatarUrl(detail.getAvatarThumbnail());
                if (familyRecordEntity.getRelation() != null) {
                    if (familyRecordEntity.getRelation() != 0) {
                        familyRecordEntity.setRelationText(BusinessEnum.FamilyRelationTextEnum.getName(familyRecordEntity.getRelation()));
                        familyRecordEntity.setOneself(0);
                    } else {
                        familyRecordEntity.setRelationText("我自己");
                        familyRecordEntity.setOneself(1);
                    }
                }
                List<FamilyRecordEntity> list = familyRecordMapper.selectList(new QueryWrapper<FamilyRecordEntity>()
                        .eq("uid", familyRecordEntity.getUid())
                        .eq("deleted", 0));
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    UserDetail userDetail = userInfoRpcService.getUserDetail(list.get(i).getCreateUid());
                    if (userDetail == null) {
                        log.error("根据用户account没有查询到相应的UserDetail，用户有可能已经被注销,或用户uid数据可能被修改");
                        continue;
                    }
                    builder.append(userDetail.getNickName());
                    if (i != list.size() - 1) {
                        builder.append(",");
                    }
                }
                familyRecordEntity.setIncidenceRelation(builder.toString());
            }
            map.put("totalCount", page.getTotal());
            map.put("totalPage", page.getPages());
            map.put("currentPage", page.getCurrent());
            map.put("pageSize", page.getSize());
            map.put("list", records);
            return map;
        }
    }
}
