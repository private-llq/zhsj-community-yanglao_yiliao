package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.annotation.LoginIgnore;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.AgencySosEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.RouteSosEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.common.utils.ValidatorUtils;
import com.zhsj.community.yanglao_yiliao.myself.service.IAgencySosService;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilySosService;
import com.zhsj.community.yanglao_yiliao.myself.service.IRouteSosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos
 * @author: Hu
 * @create: 2021-11-11 14:07
 **/
@RestController
@RequestMapping("sos")
public class SosController {

    @Autowired
    private IFamilySosService familySosService;

    @Autowired
    private IAgencySosService agencySosService;

    @Autowired
    private IRouteSosService routeSosService;

    private static final int NUMBER=3;


    /**
     * @Description: 紧急救援
     * @author: Hu
     * @since: 2021/11/12 11:07
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R
     */
    @GetMapping
    public R sos(@RequestParam Long familyId){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        familySosService.sos(loginUser,familyId);
        return R.ok();
    }



    /**
     * @Description: 查询sos家属和机构信息
     * @author: Hu
     * @since: 2021/11/12 11:06
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @GetMapping("select")
    public R<Map<String,Object>> select(@RequestParam Long familyId){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Map<String,Object> map = familySosService.selectByUid(loginUser,familyId);
        return R.ok(map);
    }


    /**
     * @Description: 根据用户uid查询绑定家属和绑定机构
     * @author: Hu
     * @since: 2021/12/8 15:20
     * @Param: [uid]
     * @return: com.zhsj.basecommon.vo.R<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @GetMapping("selectUser")
    @LoginIgnore
    public R<Map<String,Object>> selectUser(@RequestParam String uid){
        Map<String,Object> map = familySosService.selectUser(uid);
        return R.ok(map);
    }

    /**
     * @Description: 绑定家属
     * @author: Hu
     * @since: 2021/11/11 16:26
     * @Param: [agencySosEntity]
     * @return: com.zhsj.basecommon.vo.R
     */
    @PostMapping("saveFamily")
    public R<Boolean> saveFamily(@RequestBody FamilySosEntity familySosEntity){
        ValidatorUtils.validateEntity(familySosEntity,FamilySosEntity.SosValidate.class);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<FamilySosEntity> list = familySosService.list(new QueryWrapper<FamilySosEntity>().eq("uid", loginUser.getAccount()));
        if (list.size()>=NUMBER) {
            return R.fail("个人最大添加数量3！");
        }
        familySosEntity.setUid(loginUser.getAccount());
        familySosEntity.setId(SnowFlake.nextId());
        familySosEntity.setCreateTime(LocalDateTime.now());
        return R.ok(familySosService.save(familySosEntity));
    }


    /**
     * @Description: 绑定机构
     * @author: Hu
     * @since: 2021/11/11 16:26
     * @Param: [familySosEntity]
     * @return: com.zhsj.basecommon.vo.R
     */
    @PostMapping("saveAgency")
    public R<Boolean> saveAgency(@RequestBody AgencySosEntity agencySosEntity){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<AgencySosEntity> list = agencySosService.list(new QueryWrapper<AgencySosEntity>().eq("uid", loginUser.getAccount()));
        if (list.size()!=0){
            return R.fail("机构最大添加数量1！");
        }
        agencySosEntity.setId(SnowFlake.nextId());
        agencySosEntity.setCreateTime(LocalDateTime.now());
        agencySosEntity.setUid(loginUser.getAccount());
        return R.ok(agencySosService.save(agencySosEntity));
    }


    /**
     * @Description: 修改联系人
     * @author: Hu
     * @since: 2021/11/11 17:20
     * @Param: [familySosEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @PutMapping("updateFamily")
    public R<Boolean> updateFamily(@RequestBody FamilySosEntity familySosEntity){
        ValidatorUtils.validateEntity(familySosEntity,FamilySosEntity.SosValidate.class);
        familySosEntity.setUpdateTime(LocalDateTime.now());
        return R.ok(familySosService.updateById(familySosEntity));
    }


    /**
     * @Description: 修改求救机构
     * @author: Hu
     * @since: 2021/11/11 17:21
     * @Param: [agencySosEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @PutMapping("updateAgency")
    public R<Boolean> updateAgency(@RequestBody AgencySosEntity agencySosEntity){
        agencySosEntity.setUpdateTime(LocalDateTime.now());
        return R.ok(agencySosService.updateById(agencySosEntity));
    }



    /**
     * @Description: 我要找路
     * @author: Hu
     * @since: 2021/11/30 15:10
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.community.yanglao_yiliao.common.entity.RouteSosEntity>
     */
    @GetMapping("findTheWay")
    public R<RouteSosEntity> findTheWay(){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        RouteSosEntity entity = routeSosService.getOne(new QueryWrapper<RouteSosEntity>().eq("uid", loginUser.getAccount()));
        return R.ok(entity);
    }


    /**
     * @Description: 新增修改sos我要找路终点地址
     * @author: Hu
     * @since: 2021/11/30 15:16
     * @Param: [routeSosEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("saveRoute")
    public R<Void> saveRoute(@RequestBody RouteSosEntity routeSosEntity){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        routeSosService.saveRoute(routeSosEntity,loginUser);
        return R.ok();
    }



    /**
     * @Description: 删除联系人
     * @author: Hu
     * @since: 2021/11/11 17:21
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @DeleteMapping("deleteFamily")
    public R<Boolean> deleteFamily(@RequestParam Long id){
        return R.ok(familySosService.removeById(id));
    }

    /**
     * @Description: 删除求救机构
     * @author: Hu
     * @since: 2021/11/11 17:21
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @DeleteMapping("deleteAgency")
    public R<Boolean> deleteAgency(@RequestParam Long id){
        return R.ok(agencySosService.removeById(id));
    }


}
