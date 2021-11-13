package com.zhsj.community.yanglao_yiliao.myself.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.common.entity.EventFamilyEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒中间表
 * @author: Hu
 * @create: 2021-11-12 14:42
 **/
public interface EventFamilyMapper extends BaseMapper<EventFamilyEntity> {
    /**
     * @Description: 批量新增
     * @author: Hu
     * @since: 2021/11/12 15:07
     * @Param:
     * @return:
     */
    void saveAll(@Param("list") List<EventFamilyEntity> families);

    @Select("select family_id from t_event_family where event_id=#{id} and deleted=0")
    Set<Long> getByFamilyId(@Param("id") Long id);
}
