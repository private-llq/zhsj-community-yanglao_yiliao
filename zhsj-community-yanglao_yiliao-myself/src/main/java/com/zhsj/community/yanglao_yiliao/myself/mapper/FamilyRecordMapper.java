package com.zhsj.community.yanglao_yiliao.myself.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案
 * @author: Hu
 * @create: 2021-11-10 11:46
 **/
public interface FamilyRecordMapper extends BaseMapper<FamilyRecordEntity> {
    /**
     * @Description: 批量新增
     * @author: Hu
     * @since: 2021/12/2 16:45
     * @Param:
     * @return:
     */
    void saveAll(@Param("list") List<FamilyRecordEntity> list);
}
