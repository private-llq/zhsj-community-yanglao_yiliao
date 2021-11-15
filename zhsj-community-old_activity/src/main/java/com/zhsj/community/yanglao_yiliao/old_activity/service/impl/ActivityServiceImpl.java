package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.common.OSSFile;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.ActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public int addActivity(String voice,String textContent, String location, String longitude, String latitude, String multipartFile) {
        Activity activityVo = new Activity();
        activityVo.setActivityExplain(textContent);
        activityVo.setVoice(voice);
        activityVo.setLongitude(longitude);
        activityVo.setLatitude(latitude);
        activityVo.setPathUrl(multipartFile);
        List<ActivityFrom> activityTyped = activityMapper.getActivityTyped();
        List<Activity>  list = new ArrayList<>();
        list.add(activityVo);
        BeanUtils.copyProperties(activityTyped,list);
        BeanUtils.copyProperties(list,activityVo);
        return  activityMapper.insert(activityVo);
    }


    /**
     * 删除发布活动
     */
    @Override
    public int deletedActivity(LoginUser loginUser) {
        log.info("用户的字段：{}",loginUser);
        return this.activityMapper.deleteById(loginUser.getId());
    }



}
