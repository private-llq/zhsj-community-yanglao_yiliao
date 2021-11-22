package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.common.PageResult;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.*;
import com.zhsj.community.yanglao_yiliao.old_activity.exception.BootException;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.UserLocationMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserLocationMapper userLocationMapper;


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
        LoginUser user = ContextHolder.getContext().getLoginUser();
        activity.setSex(addActivityFrom.getSex());
        activity.setAge(addActivityFrom.getAge());
        activity.setNickname(user.getNickName());
        activity.setUId(user.getId());
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
        log.info("图片：{}",images);
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
    public void deletedActivity(Long uid) {
        log.info("用户id{}", uid);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        this.activityMapper.delete(new QueryWrapper<Activity>().eq("u_id",user.getId()));
        this.activityMapper.deleteById(uid);
    }

    /**
     * 查询附近的活动或者好友的活动
     *
     * @return
     */
    @Override
    public HashSet<LinkedList<UserLocation>> listActivities(UserLocationFrom userLocationFrom) {
        //根据距离查询活动
        UserLocation userLocation = this.userLocationMapper.selectOne(new QueryWrapper<UserLocation>()
                .eq("address", userLocationFrom.getAddress()).orderByAsc());
        //根据好友去查询是否是好友
        UserLocation userFriend = this.userLocationMapper.selectOne(new QueryWrapper<UserLocation>()
                .eq("user_friend", userLocationFrom.getUserFriend()).orderByAsc());
        if (userFriend.getUserFriend() == "1"){
            throw  new BootException("你们不是好友");
        }
        UserLocation userLocationFrom1 = new UserLocation();
        UserLocationFrom userLocationFrom2 = new UserLocationFrom();
        BeanUtils.copyProperties(userLocationFrom1,userLocationFrom2);
        LinkedList<UserLocation> linkedList = new LinkedList<>();
        linkedList.add(userLocation);
        linkedList.add(userFriend);

        HashSet<LinkedList<UserLocation>> hset = new HashSet<>();
        hset.add(linkedList);

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(hset);
        return  hset;
    }

    /**
     * 点击头像查询查看活动、个人资料
     *
     * @return
     */
    @Override
    public Page<Activity> queryAlbumList(PageResult pageResult) {
        PageResult pageResulted = new PageResult();
        pageResulted.setPage(pageResult.getPage());
        pageResulted.setPagesize(pageResult.getPagesize());
        pageResulted.setCounts(0);
        pageResulted.setPages(0);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0).eq("u_id",user.getId());
        Page<Activity> page = new Page<>(pageResult.getCounts(), pageResult.getPagesize());
        Page<Activity> paged = this.activityMapper.selectPage(page, queryWrapper);
        return  paged;
    }



    /**
     * 编辑资料
     *
     */
    @Override
    public int updateUserInfo(ActivityUpdateFrom activityUpdateFrom) {
        Activity activity = new Activity();
        activity.setNickname(activityUpdateFrom.getNickname());
        activity.setSex(activityUpdateFrom.getSex());
        activity.setAge(activityUpdateFrom.getAge());
        QueryWrapper<Activity> query =new QueryWrapper<>();
        LoginUser user = ContextHolder.getContext().getLoginUser();
        query.eq("u_id",user.getId());
        int update = this.activityMapper.update(activity, query);
        return update;
    }


}
