package com.zhsj.community.yanglao_yiliao.old_activity.controller;


import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.old_activity.common.PageVoed;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityTypeService;
import com.zhsj.community.yanglao_yiliao.old_activity.util.MyPageUtils;
import com.zhsj.community.yanglao_yiliao.old_activity.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.List;


/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description: 活动相关的接口
 * @create: 2021-11-10 17:03
 */
@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityTypeService activityTypeService;


    /**
     * @param reqBo 用户id，经度，维度
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     * @description 获取附近活动列表
     * @author liulq
     * @date 2021/11/24 19:00
     */
    @PostMapping("/queryActivityList")
    public PageInfo<ActivityDto> queryActivityList(@RequestBody @Valid ActivityReqBo reqBo) {
        log.info("reqBo的值{}", reqBo);
        List<ActivityDto> rspList = this.activityService.queryActivityList(reqBo);
        PageInfo<ActivityDto> activityDtoPageInfo = null;
        try {
            activityDtoPageInfo = MyPageUtils.pageMap(reqBo.getPage(), reqBo.getData(), rspList);
        } catch (Exception e) {
            return new PageInfo<ActivityDto>();
        }
        return activityDtoPageInfo;
    }


    /**
     * @param activityReqVoDto 用户id，经度，维度
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     * @description 活动人所在地
     * @author liulq
     * @date 2021/11/24 19:00
     */
    @PostMapping("/queryActivity")
    public PageInfo<ActivityDto> queryActivity(@RequestBody @Valid ActivityReqVoDto activityReqVoDto) {
        log.info("activityReqVo的值{}", activityReqVoDto);
        List<ActivityDto> activityDtos = this.activityService.queryActivity(activityReqVoDto);
        PageInfo<ActivityDto> activityDtoPageInfo = null;
        try {
            activityDtoPageInfo = MyPageUtils.pageMap(activityReqVoDto.getPage(), activityReqVoDto.getData(), activityDtos);
        } catch (Exception e) {
            return new PageInfo<ActivityDto>();
        }
        return activityDtoPageInfo;
    }


    /**
     * @description 删除活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @DeleteMapping("/deleteActivity")
    public R<String> deleteActivity(@RequestParam Long id) {
        log.info("id的值{}", id);
        this.activityService.delete(id);
        return R.ok("删除成功！");
    }

    /**
     * @description 发布活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("/publishActivity")
    public R<String> publishActivity(@RequestBody @Valid ActivitySaveReqBo reqBo) {
        log.info("reqBo的值{}", reqBo);
        this.activityService.publishActivity(reqBo);
        return R.ok("发布成功！");
    }

    /**
     * @description 查询活动类型
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @GetMapping("/queryActivityTypeList")
    public R<List<ActivityTypedDto>> queryActivityTypeList() {
        List<ActivityTypedDto> list = this.activityTypeService.selectList();
        return R.ok(list);
    }


    /**
     * @description 查询自己的所有活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("getUserActivityList")
    public PageInfo<ActivityDto> getUserActivityList(@RequestBody @Valid ActivityPageDto activityPageDto) {
        log.info("页码：{}", activityPageDto);
        List<ActivityDto> userActivityList = this.activityService.getUserActivityList(activityPageDto);
        PageInfo<ActivityDto> activityDtoPageInfo = MyPageUtils.pageMap(activityPageDto.getPage(), activityPageDto.getData(), userActivityList);
        return activityDtoPageInfo;
    }


    /**
     * @description 分页查询所有活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("pageListed")
    public PageInfo<ActivityDto> pageListed(@RequestBody @Valid ActivityReqBo activityReqBo) {
        log.info("activityReqBo的值{}", activityReqBo);
        List<ActivityDto> activityDtos = this.activityService.pageListed(activityReqBo);
        PageInfo<ActivityDto> activityDtoPageInfo = null;
        try {
            activityDtoPageInfo = MyPageUtils.pageMap(activityReqBo.getPage(), activityReqBo.getData(), activityDtos);
        } catch (Exception e) {
            return new PageInfo<ActivityDto>();
        }
        return activityDtoPageInfo;
    }


    /*****************************************************大后台*******************************************************/

    /**
     * @Description: 新增活动类型
     * @Param:
     * @return:
     * @Author: liulq
     * @date: 2021-12-06
     */
    @PostMapping("addActivityType")
    public R addActivityType(@RequestBody @Valid ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDto的值：{}", activityTypeDto);
        boolean b = this.activityTypeService.activityType(activityTypeDto);
        if (!b){
            R<Object> r = new R<>();
            r.setCode(400);
            r.setMessage("活动分类重复");
            return r;
        }
        return R.ok("新增活动分类成功");
    }


    /**
     * @Description: 修改活动类型
     * @Param:
     * @return:
     * @Author: liulq
     * @date: 2021-12-06
     */
    @PostMapping("updateActivityType")
    public R updateActivityType(@RequestBody @Valid ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDto的值：{}", activityTypeDto);
        boolean b = this.activityTypeService.updateActivityType(activityTypeDto);
        if (!b){
            R<Object> r = new R<>();
            r.setCode(400);
            r.setMessage("活动分类重复");
            return r;
        }
        return R.ok("修改活动类型成功");
    }

    /**
     * @Description: 删除活动类型
     * @Param: * @param null
     * @return:
     * @Author: liulq
     * @date: 2021-12-06
     */
    @DeleteMapping("deleteActivityType")
    public R deleteActivityType(@RequestParam String id) {
        log.info("activityTypeCode的值：{}", id);
        this.activityTypeService.deleteActivityType(id);
        return R.ok("删除成功");
    }

    /**
     * @Description: 大后台展示活动信息
     * @Param: * @param null
     * @return:
     * @Author: liulq
     * @date: 2021-12-09
     */
    @PostMapping("selectActivityList")
    public PageInfo<?> selectActivityList(@RequestBody @Valid PageVoed pageVoed) {
        log.info("页面：{}",pageVoed);
        List<ActivityReqDto> activityReqDtos = this.activityTypeService.selectActivityList();
        PageInfo<ActivityReqDto> activityReqDtoPageInfo = MyPageUtils.pageMap(pageVoed.getPage(), pageVoed.getData(), activityReqDtos);
        return activityReqDtoPageInfo;
    }


    /**
     * @Description: 根据活动id查询活动
     * @Param: * @param null
     * @return:
     * @Author: liulq
     * @date: 2021-12-09
     */
    @GetMapping("getByIdActivity")
    public R getByIdActivity(@RequestParam Long id) {
        log.info("活动id的值{}", id);
        ActivityReqDto byIdActivity = this.activityService.getByIdActivity(id);
        return R.ok(byIdActivity);
    }


    /**
     * @Description: 大后台模糊查询活动
     * @Param: * @param null
     * @return:
     * @Author: liulq
     * @date: 2021-12-09
     */
    @PostMapping("likeActivity")
    public R<PageInfo<ActivityReqDto>> likeActivity(@RequestBody LikeActivityDto likeActivity) {
        log.info("likeActivity的值{}", likeActivity);
        List<ActivityReqDto> activityReqDtos = this.activityService.likeActivity(likeActivity);
        PageInfo<ActivityReqDto> activityDtoPageInfo = MyPageUtils.pageMap(likeActivity.getPage(), likeActivity.getData(), activityReqDtos);
        R<PageInfo<ActivityReqDto>> ok = R.ok(activityDtoPageInfo);
        return ok;
    }


}
