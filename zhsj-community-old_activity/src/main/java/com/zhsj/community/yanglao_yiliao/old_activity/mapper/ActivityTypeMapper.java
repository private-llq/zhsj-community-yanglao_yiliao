package com.zhsj.community.yanglao_yiliao.old_activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypedDto;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
     * @Description: 查询所有活动类型
     * @Author: liulq
     * @date: 2022-01-13
     */
    @Select("select * from t_activity_type  as t ORDER BY t.activity_type_code ASC")
    List<ActivityTypedDto> selectListctivityType();

    /**
     * @Description: 修改activityTypeCode
     * @Author: liulq
     * @date: 2022-01-13
     */
    void addByIdActivityType(Integer activityTypeCode);

    /**
     * @Description: 根据id删除活动类型
     * @Author: liulq
     * @date: 2022-01-13
     */
    @Update("DELETE FROM t_activity_type WHERE id=#{id} ")
    void deleteByIdactivityType(Long id);
}
