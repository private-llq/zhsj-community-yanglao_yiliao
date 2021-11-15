package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import com.zhsj.community.yanglao_yiliao.healthydata.dto.TimeValueDto;
import lombok.Data;

import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户心率、体温、睡眠时间数据格式化返回数据实体
 * @date 2021/11/15 15:55
 */
@Data
public class AbnormalDataRspBo {

    /**
     * 时间title
     */
    private String timeTitle;

    /**
     * 时间数据值集合
     */
    private List<TimeValueDto> list;

    public AbnormalDataRspBo() {
    }

    public AbnormalDataRspBo(String timeTitle, List<TimeValueDto> list) {
        this.timeTitle = timeTitle;
        this.list = list;
    }
}
