package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.common.utils.ValidatorUtils;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案
 * @author: Hu
 * @create: 2021-11-10 11:55
 **/
@RestController
@RequestMapping("family")
public class FamilyRecordController {

    @Autowired
    private IFamilyRecordService familyRecordService;

//    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER)
//    private IBaseSmsRpcService baseSmsRpcService;


    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/10 14:01
     * @Param: [familyRecordEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @PostMapping("save")
    public R<Boolean> save(@RequestBody FamilyRecordEntity familyRecordEntity){
        ValidatorUtils.validateEntity(familyRecordEntity,FamilyRecordEntity.AddFamilyValidate.class);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        familyRecordEntity.setId(SnowFlake.nextId());
        familyRecordEntity.setUid(loginUser.getAccount());
        familyRecordEntity.setCreateTime(LocalDateTime.now());
        return R.ok(familyRecordService.save(familyRecordEntity));
    }

    /**
     * @Description: 查询详情
     * @author: Hu
     * @since: 2021/11/10 14:03
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity>
     */
    @GetMapping("getOne")
    public R<FamilyRecordEntity> getOne(@RequestParam Long id){
        return R.ok(familyRecordService.getById(id));
    }

    /**
     * @Description: 发送短信验证码
     * @author: Hu
     * @since: 2021/11/10 14:03
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity>
     */
    @GetMapping("sendCode")
    public R<Void> code(@RequestParam String mobile){
//        baseSmsRpcService.sendVerificationCode(mobile);
        return R.ok();
    }


    /**
     * @Description: 修改
     * @author: Hu
     * @since: 2021/11/10 14:07
     * @Param: [familyRecordEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @PutMapping("update")
    public R<Boolean> update(@RequestBody FamilyRecordEntity familyRecordEntity){
        ValidatorUtils.validateEntity(familyRecordEntity,FamilyRecordEntity.UpdateFamilyValidate.class);
        familyRecordEntity.setUpdateTime(LocalDateTime.now());
        return R.ok(familyRecordService.updateById(familyRecordEntity));
    }



    /**
     * @Description: 查询列表
     * @author: Hu
     * @since: 2021/11/10 14:07
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity>>
     */
    @PostMapping("list")
    public R<List<FamilyRecordEntity>> list(){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<FamilyRecordEntity> list = familyRecordService.userList(loginUser);
        return R.ok(list);
    }



    /**
     * @Description: 删除
     * @author: Hu
     * @since: 2021/11/10 14:07
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @DeleteMapping("delete")
    public R<Boolean> delete(@RequestParam Long id){
        return R.ok(familyRecordService.removeById(id));
    }


}
