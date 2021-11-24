package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: Hu
 * @create: 2021-11-23 14:47
 **/
@RestController
@RequestMapping("user")
public class UserDataController {

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER)
    private IBaseUserInfoRpcService baseUserInfoRpcService;


    /**
     * @Description: 查询用户详情
     * @author: Hu
     * @since: 2021/11/23 14:51
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.base.api.entity.UserDetail>
     */
    @GetMapping("details")
    public R<UserDetail> list(){
        LoginUser user = ContextHolder.getContext().getLoginUser();
        return R.ok(baseUserInfoRpcService.getUserDetail(user.getAccount()));
    }

}
