package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;



import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-10 17:03
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {


    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 查询活动类型
     * @return
     */
    @Override
    public ActivityVo geted() {
        ActivityVo activityType = this.activityMapper.getActivityTyped();
        return activityType;
    }

    /**
     * 新增发布活动
     * @return
     */
    @Override
    public List<ActivityVo> addActivity() {
        return null;
    }

    /**
     * 删除发布活动
     * @return
     */
    @Override
    public int deletedActivity(LoginUser loginUser) {
        log.info("用户的字段：{}",loginUser);
        int userId = this.activityMapper.deleteById(loginUser.getId());
        return userId;
    }





    /**
     *  ***************************************获取当前登录用户**********************************************************
     *       *****************************************************************************************************
     */
    private LoginUser UserAuth() {
        return ContextHolder.getContext().getLoginUser();
    }

}
