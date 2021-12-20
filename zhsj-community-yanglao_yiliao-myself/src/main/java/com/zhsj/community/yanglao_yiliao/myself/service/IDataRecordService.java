package com.zhsj.community.yanglao_yiliao.myself.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 大数据管理
 * @author: Hu
 * @create: 2021-12-14 15:39
 **/
public interface IDataRecordService extends IService<DataRecordEntity> {

    /**
     * @Description: 树形菜单查询
     * @author: Hu
     * @since: 2021/12/14 15:44
     * @Param:
     * @return:
     */
    List<DataRecordEntity> treeForm(DataRecordEntity dataRecordEntity);

    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/12/14 15:49
     * @Param:
     * @return:
     */
    void saveData(DataRecordEntity dataRecordEntity);
    /**
     * @Description: 删除
     * @author: Hu
     * @since: 2021/12/14 15:50
     * @Param:
     * @return:
     */
    void delete(Long id);

    /**
     * @Description: 修改
     * @author: Hu
     * @since: 2021/12/14 15:51
     * @Param:
     * @return:
     */
    void updateData(DataRecordEntity dataRecordEntity);
}
