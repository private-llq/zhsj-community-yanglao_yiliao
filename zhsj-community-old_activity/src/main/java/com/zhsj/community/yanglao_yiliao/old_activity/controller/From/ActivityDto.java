package com.zhsj.community.yanglao_yiliao.old_activity.controller.From;

import com.zhsj.community.yanglao_yiliao.old_activity.po.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chengl
 * @version 1.0
 * @Description: 附近活动返回实体
 * @date 2021/11/24 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto extends Activity {

    /**
     * 距离
     */
    private Double dist;


}
