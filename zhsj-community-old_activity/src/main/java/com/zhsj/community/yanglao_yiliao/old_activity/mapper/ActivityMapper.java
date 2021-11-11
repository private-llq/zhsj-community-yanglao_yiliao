package com.zhsj.community.yanglao_yiliao.old_activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-10 17:02
 */
public interface ActivityMapper extends BaseMapper<Activity> {
    /**
     * 查询活动类型
     * @return
     */
    @Select("SELECT id,activity_type from t_activity")
    public ActivityVo getActivityTyped();


    /**
     * 新增发布活动
     * @return
     */
    public List<ActivityVo> addActivity();


    /**
     * 删除发布活动
     * @return
     */
    public boolean deletedActivity();


}
