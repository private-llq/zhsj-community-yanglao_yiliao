package com.zhsj.community.yanglao_yiliao.myself.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.UserDataRecordEntity;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 用户健康信息
 * @author: Hu
 * @create: 2021-11-22 14:55
 **/
public interface IUserDataRecordService extends IService<UserDataRecordEntity> {
    /**
     * @Description: 查询健康档案树形菜单
     * @author: Hu
     * @since: 2021/11/22 15:08
     * @Param:
     * @return:
     */
    List<DataRecordEntity> treeForm();

    /**
     * @Description: 查询列表
     * @author: Hu
     * @since: 2021/11/23 9:17
     * @Param:
     * @return:
     */
    void getList();
}
