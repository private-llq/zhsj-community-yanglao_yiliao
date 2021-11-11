package com.zhsj.community.yanglao_yiliao.myself.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos家属信息
 * @author: Hu
 * @create: 2021-11-11 14:05
 **/
public interface FamilySosMapper extends BaseMapper<FamilySosEntity> {
    /**
     * @Description: 批量新增
     * @author: Hu
     * @since: 2021/11/11 14:40
     * @Param: [families]
     * @return: [void]
     */
    void saveAll(@Param("list") List<FamilySosEntity> families);
}
