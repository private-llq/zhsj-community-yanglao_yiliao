package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.ActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-10 17:03
 */
public interface ActivityService {


    /**
     * 查询具体的活动
     */
    List<ActivityFrom> getactivit();


    /**
     * 新增活动
     * textContent:文字动态
     * location：位置名称
     * longitude：经度
     * latitude: 纬度
     * multipartFile：图片
     * voice:语音
     */
    int addActivity(String voice,String textContent, String location, String longitude, String latitude, String multipartFile);


    /**
     * 删除发布活动
     */
    int deletedActivity(LoginUser loginUser);




}
