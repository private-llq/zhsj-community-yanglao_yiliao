package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.UserDataRecordEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.DataRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.UserDataRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IUserDataRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 用户健康信息
 * @author: Hu
 * @create: 2021-11-22 14:55
 **/
@Service
public class UserDataRecordServiceImpl extends ServiceImpl<UserDataRecordMapper, UserDataRecordEntity> implements IUserDataRecordService {
    @Resource
    private DataRecordMapper dataRecordMapper;


    /**
     * @Description: 查询我的健康档案列表
     * @author: Hu
     * @since: 2021/11/23 14:41
     * @Param: [loginUser]
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.UserDataRecordEntity>
     */
    @Override
    public List<UserDataRecordEntity> getList(DataRecordEntity dataRecordEntity,LoginUser loginUser) {
        return dataRecordMapper.getList(dataRecordEntity,loginUser.getAccount());
    }

    /**
     * @Description: 查询健康数据树形菜单
     * @author: Hu
     * @since: 2021/11/22 15:20
     * @Param: []
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity>
     */
    @Override
    public List<DataRecordEntity> treeForm(DataRecordEntity record) {
        List<DataRecordEntity> fuList = new LinkedList<>();
        List<DataRecordEntity> ziList = new LinkedList<>();

        List<DataRecordEntity> entityList = dataRecordMapper.selectList(new QueryWrapper<DataRecordEntity>().eq("site",record.getSite()).eq("type",record.getType()));
        if (entityList.size()!=0){
            for (DataRecordEntity dataRecordEntity : entityList) {
                if (dataRecordEntity.getPid()==null){
                    fuList.add(dataRecordEntity);
                } else {
                    ziList.add(dataRecordEntity);
                }
            }
            if (fuList.size()!=0){
                for (DataRecordEntity entity : fuList) {
                    for (DataRecordEntity recordEntity : ziList) {
                        if (recordEntity.getPid().equals(entity.getId())){
                            entity.getSubmenu().add(recordEntity);
                        }
                    }
                }
            }
        }

        return fuList;
    }
}
