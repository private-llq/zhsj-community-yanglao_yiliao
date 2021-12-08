package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.ValidatorUtils;
import com.zhsj.community.yanglao_yiliao.myself.service.IEventService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒
 * @author: Hu
 * @create: 2021-11-12 14:44
 **/
@RestController
@RequestMapping("event")
public class EventController {

    @Resource
    private IEventService eventService;

    /**
     * @Description: 单查详情
     * @author: Hu
     * @since: 2021/11/15 9:37
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.community.yanglao_yiliao.common.entity.EventEntity>
     */
    @GetMapping("getOne")
    public R<EventEntity> getOne(@RequestParam Long id) {
        EventEntity eventEntity = eventService.getById(id);
        return R.ok(eventEntity);
    }


    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/15 9:37
     * @Param: [eventEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Long>
     */
    @PostMapping("save")
    public R<Long> save(@RequestBody EventEntity eventEntity) {
        ValidatorUtils.validateEntity(eventEntity, EventEntity.EventValidate.class);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Long aLong = eventService.save(eventEntity, loginUser);
        return R.ok(aLong);
    }


    /**
     * @Description: 修改
     * @author: Hu
     * @since: 2021/11/15 9:36
     * @Param: [eventEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PutMapping(value = "update")
    public R<Void> update(@RequestBody EventEntity eventEntity) {
        ValidatorUtils.validateEntity(eventEntity, EventEntity.EventValidate.class);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        eventService.update(eventEntity, loginUser);
        return R.ok();
    }


    /**
     * @Description: 查询列表
     * @author: Hu
     * @since: 2021/11/15 9:36
     * @Param: [date]
     * @return: com.zhsj.basecommon.vo.R<java.util.List < com.zhsj.community.yanglao_yiliao.common.entity.EventEntity>>
     */
    @GetMapping(value = "list")
    public R<List<EventEntity>> list(@RequestParam("week") Integer week) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        return R.ok(eventService.list(week, loginUser));
    }

    @GetMapping(value = "pageList")
    public R<List<EventEntity>> pageList() {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        return R.ok(eventService.pageList(loginUser));
    }


    /**
     * @Description: 删除
     * @author: Hu
     * @since: 2021/11/15 9:36
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @DeleteMapping("delete")
    public R<Void> delete(@RequestParam Long id) {
        eventService.delete(id);
        return R.ok();
    }


    /**
     * @Description: 启用停用
     * @author: Hu
     * @since: 2021/11/15 9:36
     * @Param: [status, id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @GetMapping("status")
    public R<Void> status(@RequestParam Integer status, @RequestParam Long id) {
        eventService.status(id, status);
        return R.ok();
    }
}
