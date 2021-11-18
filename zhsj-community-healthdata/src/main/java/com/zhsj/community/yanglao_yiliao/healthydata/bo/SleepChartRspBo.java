package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import com.zhsj.community.yanglao_yiliao.healthydata.dto.SleepTitleTimeValueDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 查询用户睡眠图表信息返回实体
 * @date 2021/11/17 13:52
 */
@Data
@Accessors(chain = true)
public class SleepChartRspBo {

    /**
     * 最近七天的数据时间、值
     */
    List<SleepTitleTimeValueDto> list;

    /**
     * 最近七天总的睡眠时间(按周查,按天查为null)
     */
    private Integer lastSevenDayTotalSleepTime;

    /**
     * 最近七天平均睡眠时间(按周查,按天查为null)
     */
    private Integer lastSevenDayAvgSleepTime;

    /**
     * 最近七天平均睡眠时间同比上一个七天平均睡眠时间（负数：平均睡眠时间减少，正数：平均睡眠时间增加，按周查,按天查为null）
     */
    private Integer compareAvgSleepTime;

}
