package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.myself.service.IEventService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

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

    @GetMapping("getOne")
    public R getOne(@RequestParam Long id){
        eventService.getById(id);
        return R.ok();
    }

    @PostMapping("save")
    public R save(@RequestBody EventEntity eventEntity){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        eventService.save(eventEntity,loginUser);
        return R.ok();
    }

    @PutMapping("update")
    public R update(@RequestBody EventEntity eventEntity){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        eventService.update(eventEntity,loginUser);
        return R.ok();
    }

    @GetMapping("list")
    public R list(@RequestParam("date")LocalDate date){
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        return R.ok(eventService.list(date,loginUser));
    }

    @DeleteMapping("delete")
    public R delete(@RequestParam Long id){
        eventService.delete(id);
        return R.ok();
    }

    @DeleteMapping("status")
    public R status(@RequestParam Integer status,@RequestParam Long id){
        eventService.status(id,status);
        return R.ok();
    }
}
