package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.basecommon.interfaces.IBaseSmsRpcService;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.BaseQo;
import com.zhsj.community.yanglao_yiliao.common.utils.PageVo;
import com.zhsj.community.yanglao_yiliao.common.utils.ValidatorUtils;
import com.zhsj.community.yanglao_yiliao.myself.service.IEventService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
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

    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER)
    private IBaseSmsRpcService baseSmsRpcService;

    /**
     * @Description: 单查详情
     * @author: Hu
     * @since: 2021/11/15 9:37
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<com.zhsj.community.yanglao_yiliao.common.entity.EventEntity>
     */
    @GetMapping("getOne")
    public R<EventEntity> getOne(@RequestParam Long id) {
        baseSmsRpcService.sendVerificationCode("17687075014");
        EventEntity eventEntity = eventService.getById(id);
        return R.ok(eventEntity);
    }


    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/15 9:37
     * @Param: [eventEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("save")
    public R<Void> save(@RequestBody EventEntity eventEntity) {
        ValidatorUtils.validateEntity(eventEntity, EventEntity.EventValidate.class);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        eventService.save(eventEntity, loginUser);
        return R.ok();
    }


    /**
     * @Description: 修改
     * @author: Hu
     * @since: 2021/11/15 9:36
     * @Param: [eventEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PutMapping("update")
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
    @GetMapping("list")
    public R<List<EventEntity>> list(@RequestParam("date") LocalDate date) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        return R.ok(eventService.list(date, loginUser));
    }

    /**
     * @Description: 分页查询
     * @author: Hu
     * @since: 2021/11/15 9:36
     * @Param: [date]
     * @return: com.zhsj.basecommon.vo.R<java.util.List < com.zhsj.community.yanglao_yiliao.common.entity.EventEntity>>
     */
    @GetMapping("pageList")
    public R<PageVo<EventEntity>> pageList(@RequestBody BaseQo baseQo) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        PageVo<EventEntity> pageVo = eventService.pageList(baseQo, loginUser);
        return R.ok(pageVo);
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
