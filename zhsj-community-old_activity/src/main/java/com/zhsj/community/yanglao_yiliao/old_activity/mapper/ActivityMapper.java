package com.zhsj.community.yanglao_yiliao.old_activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.ActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description:
 * @create: 2021-11-10 17:02
 */
@Repository
public interface ActivityMapper extends BaseMapper<Activity> {
    /**
     * 查询活动类型
     */
    @Select("select id,activity_type from t_activity")
    List<ActivityFrom> getActivityTyped();


}
