package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.common.PageResult;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.ActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.addActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.exception.BootException;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private ActivityMapper activityMapper;


    /**
     * 查询活动类型
     */
    @Override
    public List<ActivityFrom> getactivit() {
        List<ActivityFrom> activityTyped = this.activityMapper.getActivityTyped();
        return activityTyped;
    }

    /**
     * 新增发布活动
     */
    @Override
    public int addActivity(addActivityFrom addActivityFrom) {
        log.info("活动参数{}", addActivityFrom);
        //新增赋值
        Activity activity = new Activity();
        activity.setActivityType(addActivityFrom.getActivityType());
        activity.setActivityExplain(addActivityFrom.getActivityExplain());
        activity.setVoice(addActivityFrom.getVoice());
        activity.setLatitude(addActivityFrom.getLatitude());
        List<String> image =addActivityFrom.getPathUrl();
        if (image.size() > 3){
           throw  new BootException("照片只能上传3张");
        }
        for (String s : image){
            activity.setPathUrl(s);
        }
        activity.setLongitude(addActivityFrom.getLongitude());
        activity.setBeatadistancefrom(addActivityFrom.getBeatadistancefrom());
        //执行新增操作
        int addActivity = this.activityMapper.insert(activity);
        return addActivity;
    }


    /**
     * 删除发布活动
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletedActivity(Long id) {
        log.info("用户id{}", id);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        this.activityMapper.delete(new QueryWrapper<Activity>().eq("u_id",user.getId()));
        this.activityMapper.deleteById(id);
        return 0;
    }

    /**
     * 查询附近的活动或者好友的活动
     *
     */
    @Override
    public List<ActivityVo> listActivities(addActivityFrom addActivityFrom) {
        log.info("addActivityFrom的值{}",addActivityFrom);
        //查询距离自己近的
        Activity beatadistancefrom = this.activityMapper.selectOne(new QueryWrapper<Activity>()
                .eq("beatadistancefrom", addActivityFrom.getBeatadistancefrom()).orderByAsc(addActivityFrom.getBeatadistancefrom()));
        //好友的距离，我想想..........




        return null;
    }

    /**
     * 点击头像查询查看活动、个人资料
     *
     */
    @Override
    public IPage<Activity> queryAlbumList(PageResult pageResult) {
        PageResult pageResulted = new PageResult();
        pageResulted.setPage(pageResult.getPage());
        pageResulted.setPagesize(pageResult.getPagesize());
        pageResulted.setCounts(0);
        pageResulted.setPages(0);
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);
        Page<Activity> page = new Page<>(pageResult.getCounts(), pageResult.getPagesize());
        Page<Activity> paged = this.activityMapper.selectPage(page, queryWrapper);
        return paged;
    }


}
