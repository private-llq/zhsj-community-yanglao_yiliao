package com.zhsj.community.yanglao_yiliao.myself.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseAuthRpcService;
import com.zhsj.base.api.rpc.IBaseSmsRpcService;
import com.zhsj.base.api.rpc.IBaseUpdateUserRpcService;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.annotation.LoginIgnore;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.constant.BusinessEnum;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.qo.FamilysQo;
import com.zhsj.community.yanglao_yiliao.common.utils.ValidatorUtils;
import com.zhsj.community.yanglao_yiliao.myself.bo.FamilyRecordPageReqBo;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventFamilyMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilyRecordService;
import com.zhsj.community.yanglao_yiliao.myself.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案
 * @author: Hu
 * @create: 2021-11-10 11:55
 **/
@Slf4j
@RestController
@RequestMapping("family")
public class FamilyRecordController {

    @Autowired
    private IFamilyRecordService familyRecordService;
    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseSmsRpcService baseSmsRpcService;
    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseAuthRpcService baseAuthRpcService;
    private final String[] img = {"jpg", "png", "jpeg"};
    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUserInfoRpcService userInfoRpcService;
    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUpdateUserRpcService iBaseUpdateUserRpcService;

    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/10 14:01
     * @Param: [familyRecordEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Boolean>
     */
    @PostMapping("save")
    public R<Boolean> save(@RequestBody FamilyRecordEntity familyRecordEntity) {
        ValidatorUtils.validateEntity(familyRecordEntity, FamilyRecordEntity.AddFamilyValidate.class);
        boolean verification = baseAuthRpcService.smsVerification(familyRecordEntity.getMobile(), familyRecordEntity.getCode());
        if (!verification) {
            log.error("验证码错误！");
            return R.fail("验证码错误！");
        }
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (loginUser.getPhone().equals(familyRecordEntity.getMobile())) {
            log.error("不能添加自己为家人，FamilyRecordEntity = {}，LoginUser = {}", familyRecordEntity, loginUser);
            return R.fail("不能添加自己！");
        }
        UserDetail detail = userInfoRpcService.getUserDetailByPhone(familyRecordEntity.getMobile());
        List<FamilyRecordEntity> list = familyRecordService.list(new LambdaQueryWrapper<FamilyRecordEntity>()
                .eq(FamilyRecordEntity::getCreateUid, loginUser.getAccount())
                .eq(FamilyRecordEntity::getDeleted, 0));
        for (FamilyRecordEntity recordEntity : list) {
            if (detail != null) {
                if (detail.getAccount().equals(recordEntity.getUid())) {
                    log.error("被添加人已经在家人列表！");
                    return R.fail("被添加人已经在家人列表！");
                }
            }
        }
        familyRecordService.saveUser(familyRecordEntity, loginUser);
        return R.ok();
    }

    /**
     * @Description: 查询详情（大后台通用）
     * @author: Hu
     * @since: 2021/11/10 14:03
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity>
     */
    @GetMapping("getOne")
    public R<FamilyRecordEntity> getOne(@RequestParam Long id) {
        log.info("查询家人关系详情");
        FamilyRecordEntity entity = familyRecordService.getById(id);
        if (entity.getRelation() != null) {
            if (entity.getRelation() != 0) {
                entity.setRelationText(BusinessEnum.FamilyRelationTextEnum.getName(entity.getRelation()));
                entity.setOneself(0);
            } else {
                entity.setRelationText("我自己");
                entity.setOneself(1);
            }
        }
//        // ---动态查询用户信息
        UserDetail userDetail = userInfoRpcService.getUserDetail(entity.getUid());
        entity.setAvatarUrl(userDetail.getAvatarThumbnail());
        if (StrUtil.isNotBlank(userDetail.getBirthday())) {
            entity.setBirthday(LocalDate.parse(userDetail.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        entity.setMobile(userDetail.getPhone());
        entity.setName(userDetail.getNickName());
        entity.setSex(userDetail.getSex());

        R<FamilyRecordEntity> ok = R.ok(entity);
        ok.setMessage("OK");
        return ok;
    }

    /**
     * @Description: 头像上传
     * @author: Hu
     * @since: 2021/11/23 10:10
     * @Param: [file]
     * @return: com.zhsj.basecommon.vo.R<java.lang.String>
     */
    @PostMapping("upload")
    public R<String> upload(@RequestParam MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String s = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!Arrays.asList(img).contains(s)) {
            return R.fail("请上传图片！可用后缀" + Arrays.toString(img));
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
    public R<Void> code(@RequestParam String mobile) {
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
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody FamilyRecordEntity familyRecordEntity) {
        log.info("修改家人关系, FamilyRecordEntity = {}", familyRecordEntity);
        ValidatorUtils.validateEntity(familyRecordEntity, FamilyRecordEntity.UpdateFamilyValidate.class);
        FamilyRecordEntity entity = familyRecordService.getById(familyRecordEntity.getId());
        if (entity.getRelation() != null && entity.getRelation() == 0 && familyRecordEntity.getRelation() != null && familyRecordEntity.getRelation() != 0) {
            return R.fail("自己的家人关系不能修改！");
        }
        if (entity.getRelation() != null && entity.getRelation() == 0) {
            UserDetail userDetail = userInfoRpcService.getUserDetail(entity.getUid());
            // ---同步修改社区用户信息
            if (ObjectUtil.isNotNull(familyRecordEntity.getBirthday())) {
                iBaseUpdateUserRpcService.updateUserInfo(userDetail.getId(), familyRecordEntity.getName(), familyRecordEntity.getAvatarUrl(), familyRecordEntity.getSex(), familyRecordEntity.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            iBaseUpdateUserRpcService.updateUserInfo(userDetail.getId(), familyRecordEntity.getName(), familyRecordEntity.getAvatarUrl(), familyRecordEntity.getSex(), null);
            entity.setUpdateTime(LocalDateTime.now());
            familyRecordService.updateById(entity);
            return R.ok(true);
        }
        entity.setRelation(familyRecordEntity.getRelation());
        entity.setUpdateTime(LocalDateTime.now());
        return R.ok(familyRecordService.updateById(entity));
    }


    /**
     * @Description: 查询列表
     * @author: Hu
     * @since: 2021/11/10 14:07
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<java.util.List < com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity>>
     */
    @PostMapping("list")
    public R<List<FamilyRecordEntity>> list() {
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
    public R<List<FamilyRecordEntity>> userList(@RequestParam String uid) {
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
    public R<Boolean> delete(@RequestParam Long id) {
        FamilyRecordEntity entity = familyRecordService.getById(id);
        if (entity == null) {
            log.error("已经被删除！");
            throw new BaseException(ErrorEnum.DELETE_FAIL);
        }
        if (entity.getRelation() == 0) {
            log.error("不能删除自己！");
            throw new BaseException(ErrorEnum.DELETE_FAIL);
        }
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
    public R<Void> importFamily(@RequestBody FamilysQo familysQo) {
        familyRecordService.importFamily(familysQo, ContextHolder.getContext().getLoginUser());
        return R.ok();
    }


    //////////////////////////////////////////////////大后台///////////////////////////////////////////////////////

    /**
     * @Description: 大后台分页查询
     * @author: Hu
     * @since: 2021/12/17 16:11
     * @Param: [qo]
     * @return: com.zhsj.basecommon.vo.R<java.util.Map < java.lang.String, java.lang.Object>>
     */
    @PostMapping("page")
    public R<Map<String, Object>> importFamily(@RequestBody @Valid FamilyRecordPageReqBo reqBo) {
        Map<String, Object> map = familyRecordService.selectPage(reqBo);
        R<Map<String, Object>> ok = R.ok(map);
        ok.setMessage("OK");
        return ok;
    }


}
