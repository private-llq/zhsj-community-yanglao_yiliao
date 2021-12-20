package com.zhsj.community.yanglao_yiliao.myself.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.UserDataRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 健康数据档案
 * @author: Hu
 * @create: 2021-11-22 14:53
 **/
public interface DataRecordMapper extends BaseMapper<DataRecordEntity> {
    /**
     * @Description: 查询我的健康档案
     * @author: Hu
     * @since: 2021/11/23 14:38
     * @Param:
     * @return:
     */
    List<UserDataRecordEntity> getList(@Param("data")DataRecordEntity dataRecordEntity,@Param("uid") String uid);
}
