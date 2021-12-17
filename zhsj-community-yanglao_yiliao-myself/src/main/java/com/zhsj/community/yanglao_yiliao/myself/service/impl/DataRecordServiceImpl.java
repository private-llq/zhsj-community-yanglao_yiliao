package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.DataRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IDataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: Hu
 * @create: 2021-12-14 15:39
 **/
@Service
public class DataRecordServiceImpl extends ServiceImpl<DataRecordMapper, DataRecordEntity> implements IDataRecordService {

    @Autowired
    private DataRecordMapper dataRecordMapper;

    @Override
    public List<DataRecordEntity> treeForm(DataRecordEntity record) {
        List<DataRecordEntity> fuList = new LinkedList<>();
        List<DataRecordEntity> ziList = new LinkedList<>();
        QueryWrapper<DataRecordEntity> wrapper = new QueryWrapper<>();
        if (record.getSite()!=null&&record.getSite()!=0){
            wrapper.eq("site",record.getSite());
        }
        if (record.getType()!=null&&record.getType()!=0){
            wrapper.eq("type",record.getType());
        }
        List<DataRecordEntity> entityList = dataRecordMapper.selectList(wrapper);
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


    @Override
    public void saveData(DataRecordEntity dataRecordEntity) {
            dataRecordEntity.setId(SnowFlake.nextId());
            dataRecordMapper.insert(dataRecordEntity);

    }



    @Override
    public void delete(Long id) {
        DataRecordEntity entity = dataRecordMapper.selectById(id);
        if (entity != null) {
            if (entity.getPid()!=null){
                dataRecordMapper.deleteById(id);
            } else {
                dataRecordMapper.deleteById(id);
                dataRecordMapper.delete(new QueryWrapper<DataRecordEntity>().eq("pid",id));
            }
        }

    }

    @Override
    public void updateData(DataRecordEntity dataRecordEntity) {
        dataRecordMapper.updateById(dataRecordEntity);
    }
}
