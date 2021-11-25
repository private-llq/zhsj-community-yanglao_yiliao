package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.UserDataRecordEntity;
import com.zhsj.community.yanglao_yiliao.myself.service.IUserDataRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:  用户健康信息
 * @author: Hu
 * @create: 2021-11-22 14:54
 **/
@RestController
@RequestMapping("data")
public class UserDataRecordController {

    @Resource
    private IUserDataRecordService userDataRecordService;


    /**
     * @Description: 查询健康档案树形菜单
     * @author: Hu
     * @since: 2021/11/22 15:21
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity>>
     */
    @GetMapping("treeForm")
    public R<List<DataRecordEntity>> treeForm(){
        return R.ok(userDataRecordService.treeForm());
    }


    /**
     * @Description: 查询我的健康档案列表
     * @author: Hu
     * @since: 2021/11/23 14:41
     * @Param: [loginUser]
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.UserDataRecordEntity>
     */
    @GetMapping("list")
    public R<List<UserDataRecordEntity>> list(){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<UserDataRecordEntity> list = userDataRecordService.getList(loginUser);
        return R.ok(list);
    }

    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/22 15:22
     * @Param: [userDataRecordEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("save")
    public R<Void> save(@RequestBody UserDataRecordEntity userDataRecordEntity){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        userDataRecordEntity.setUid(loginUser.getAccount());
        userDataRecordService.save(userDataRecordEntity);
        return R.ok();
    }


    /**
     * @Description: 删除
     * @author: Hu
     * @since: 2021/11/22 15:22
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @DeleteMapping("delete")
    public R<Void> treeForm(@RequestParam Long id){
        userDataRecordService.removeById(id);
        return R.ok();
    }

}
