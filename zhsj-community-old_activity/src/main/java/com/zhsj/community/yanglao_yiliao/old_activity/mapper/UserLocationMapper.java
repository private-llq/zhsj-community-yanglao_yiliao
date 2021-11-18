package com.zhsj.community.yanglao_yiliao.old_activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.UserLocationFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import org.apache.ibatis.annotations.Select;
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
    UserLocationVo queryByUserId();

    /**
     * 获取匹配到用户的信息
     * @param
     * @return MatchedUserDTO
     */
    @Select("SELECT user_id, user_name, birthday, sex, introduction, head_image_url FROM User WHERE user_id=#{userId}")
    UserLocationFrom getMatchedUserInfo(Integer uId);
}
