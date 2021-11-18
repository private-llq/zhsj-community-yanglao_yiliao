package com.zhsj.community.yanglao_yiliao.old_activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import org.springframework.stereotype.Repository;



/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description: 距离的mapper层
 * @create: 2021-11-11 15:10
 */
@Repository
public interface UserLocationMapper extends BaseMapper<UserLocation> {
    /**
     * 查询用户地理位置
     *
     */
    UserLocationVo queryByUserId(LoginUser loginUser);


}
