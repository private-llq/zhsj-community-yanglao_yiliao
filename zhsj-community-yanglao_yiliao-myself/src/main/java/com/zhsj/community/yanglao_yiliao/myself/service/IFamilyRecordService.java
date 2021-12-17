package com.zhsj.community.yanglao_yiliao.myself.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.qo.FamilysQo;
import com.zhsj.community.yanglao_yiliao.common.utils.BaseQo;

import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案
 * @author: Hu
 * @create: 2021-11-10 11:52
 **/
public interface IFamilyRecordService extends IService<FamilyRecordEntity> {
    /**
     * @Description: 查列表
     * @author: Hu
     * @since: 2021/11/10 14:41
     * @Param:
     * @return:
     */
    List<FamilyRecordEntity> userList(LoginUser loginUser);

    /**
     * @Description: 添加家人档案
     * @author: Hu
     * @since: 2021/11/30 14:01
     * @Param:
     * @return: 
     */
    void saveUser(FamilyRecordEntity familyRecordEntity,LoginUser loginUser);

    /**
     * @Description: 导入社区房间成员
     * @author: Hu
     * @since: 2021/12/2 16:22
     * @Param:
     * @return:
     */
    void importFamily(FamilysQo familysQo,LoginUser loginUser);

    /**
     * @Description: 查询用户家人集合
     * @author: Hu
     * @since: 2021/12/8 15:26
     * @Param:
     * @return:
     */
    List<FamilyRecordEntity> userByList(String uid);

    /**
     * @Description: 大后台分页查询
     * @author: Hu
     * @since: 2021/12/17 16:02
     * @Param:
     * @return:
     */
    Map<String, Object> selectPage(BaseQo<String> qo);
}
