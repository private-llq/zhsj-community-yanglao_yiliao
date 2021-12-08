package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.base.api.rpc.IBaseSmsRpcService;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.annotation.LoginIgnore;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.constant.BusinessEnum;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.qo.FamilysQo;
import com.zhsj.community.yanglao_yiliao.common.utils.ValidatorUtils;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilyRecordService;
import com.zhsj.community.yanglao_yiliao.myself.utils.MinioUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER)
    private IBaseSmsRpcService baseSmsRpcService;

    private final String[] img ={"jpg","png","jpeg"};


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
        familyRecordService.saveUser(familyRecordEntity,loginUser);
        return R.ok();
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
        FamilyRecordEntity entity = familyRecordService.getById(id);
        if (entity.getRelation()!=null){
            if (entity.getRelation()==0){
                entity.setRelationText("我自己");
                entity.setOneself(1);
            } else {
                entity.setRelationText(BusinessEnum.FamilyRelationTextEnum.getName(entity.getRelation()));
                entity.setOneself(0);
            }
        }
        return R.ok(entity);
    }

    /**
     * @Description: 头像上传
     * @author: Hu
     * @since: 2021/11/23 10:10
     * @Param: [file]
     * @return: com.zhsj.basecommon.vo.R<java.lang.String>
     */
    @PostMapping("upload")
    public R<String> upload(@RequestParam MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String s = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!Arrays.asList(img).contains(s)) {
            return R.fail("请上传图片！可用后缀"+ Arrays.toString(img));
        }
        String upload = MinioUtils.upload(file, "portrait");
        return R.ok(upload);
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
        baseSmsRpcService.sendVerificationCode(mobile);
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
        FamilyRecordEntity entity = familyRecordService.getById(familyRecordEntity.getId());
        if (entity.getRelation()!=null&&entity.getRelation()==0&&familyRecordEntity.getRelation()!=null&&familyRecordEntity.getRelation()!=0){
            return R.fail("自己的关系不能修改！");
        }
        if (!entity.getMobile().equals(familyRecordEntity.getMobile())){
            return R.fail("手机号不能在这里修改哦！");
        }
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
     * @Description: 根据uid查询家人档案
     * @author: Hu
     * @since: 2021/12/8 15:27
     * @Param:
     * @return:
     */
    @PostMapping("userList")
    @LoginIgnore
    public R<List<FamilyRecordEntity>> userList(@RequestParam String uid){
        List<FamilyRecordEntity> list = familyRecordService.userByList(uid);
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


    /**
     * @Description: 导入社区房间成员
     * @author: Hu
     * @since: 2021/12/2 16:21
     * @Param: [familysQo]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("importFamily")
    public R<Void> importFamily(@RequestBody FamilysQo familysQo){
        familyRecordService.importFamily(familysQo,ContextHolder.getContext().getLoginUser());
        return R.ok();
    }


}
