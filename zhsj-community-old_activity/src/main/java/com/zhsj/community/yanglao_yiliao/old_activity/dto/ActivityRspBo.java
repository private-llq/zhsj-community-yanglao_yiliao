package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import lombok.Data;

import java.util.List;

/**
 * @author chengl
 * @version 1.0
 * @Description: 附近活动返回数据实体
 * @date 2021/11/23 19:41
 */
@Data
public class ActivityRspBo {

    /**
     * 时间title
     */
    private String timeTitle;

    /**
     * 时间数据值集合
     */
    private List<ActivityDto> list;

    public ActivityRspBo() {
    }

    public ActivityRspBo(String timeTitle, List<ActivityDto> list) {
        this.timeTitle = timeTitle;
        this.list = list;
    }
}
