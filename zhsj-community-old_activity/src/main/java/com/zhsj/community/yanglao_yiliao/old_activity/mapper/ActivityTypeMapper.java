package com.zhsj.community.yanglao_yiliao.old_activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityReqDto;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chengl
 * @version 1.0
 * @Description: 活动类型实体mapper层
 * @date 2021/11/24 10:10
 */
@Repository
public interface ActivityTypeMapper extends BaseMapper<ActivityType> {
    /**
     * 删除活动类型
     * @param activityTypeCode
     */
    @Delete("DELETE FROM t_activity_type where activity_type_code=#{activityTypeCode}")
    void deleteActivityType(String activityTypeCode);

    /**
     * 查询所有的活动
     * @return
     */
    @Select("SELECT * from  t_activity")
    List<ActivityReqDto> selectActivityList();

}
