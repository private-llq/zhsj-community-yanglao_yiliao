package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.common.PageResult;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.ActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.addActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 活动的实现类
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
        //用逗号隔开分别存
        ArrayList<String> strings = ImagesList(addActivityFrom.getPathUrl());
        for (String s:strings){
            activity.setPathUrl(s);
        }
        activity.setLongitude(addActivityFrom.getLongitude());
        activity.setDistance(addActivityFrom.getDistance());
        //执行新增操作
        int addActivity = this.activityMapper.insert(activity);
        return addActivity;
    }

    /**
     * 拆分 ,隔开的图片url地址
     *
     */
    public static ArrayList<String> ImagesList(String images) {
        ArrayList<String> urls = new ArrayList<>();
        String[] split = images.split(",");
        if (split.length > 1) {
            for (String s : split) {
                urls.add(s);
            }
        } else {
            urls.add(images);
        }
        return urls;
    }

    /**
     * 删除发布活动
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedActivity(Long id) {
        log.info("用户id{}", id);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        this.activityMapper.delete(new QueryWrapper<Activity>().eq("u_id",user.getId()));
        this.activityMapper.deleteById(id);
    }

    /**
     * 查询附近的活动或者好友的活动
     *
     */
    @Override
    public List<Activity> listActivities(addActivityFrom addActivityFrom) {
        log.info("addActivityFrom的值{}",addActivityFrom);
        //根据距离查询活动
        List<Activity> activities = this.activityMapper.selectList(new QueryWrapper<Activity>()
                .eq("distance", addActivityFrom.getDistance()).orderByAsc(addActivityFrom.getDistance()));
        //



        return activities;
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
        queryWrapper.eq("deleted", 0);
        Page<Activity> page = new Page<>(pageResult.getCounts(), pageResult.getPagesize());
        Page<Activity> paged = this.activityMapper.selectPage(page, queryWrapper);
        return paged;
    }



    /**
     * 编辑资料
     *
     */
    @Override
    public int updateUserInfo(ActivityVo activityVo) {
        Activity activity = new Activity();
        activity.setNickname(activityVo.getNickname());
        activity.setSex(activityVo.getSex());
        activity.setAge(activityVo.getAge());
        QueryWrapper<Activity> query =new QueryWrapper<>();
        query.eq("u_id",activityVo.getUId());
        int update = this.activityMapper.update(activity, query);
        return update;
    }


}
