package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public void getList() {

    }

    /**
     * @Description: 查询健康数据树形菜单
     * @author: Hu
     * @since: 2021/11/22 15:20
     * @Param: []
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity>
     */
    @Override
    public List<DataRecordEntity> treeForm() {
        List<DataRecordEntity> fuList = new LinkedList<>();
        List<DataRecordEntity> ziList = new LinkedList<>();

        List<DataRecordEntity> entityList = dataRecordMapper.selectList(null);
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
                        if (recordEntity.getPid()==entity.getId()){
                            entity.getSubmenu().add(recordEntity);
                        }
                    }
                }
            }
        }

        return fuList;
    }
}
