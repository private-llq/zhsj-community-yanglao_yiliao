package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.ValidatorUtils;
import com.zhsj.community.yanglao_yiliao.myself.service.IDataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 大数据管理
 * @author: Hu
 * @create: 2021-12-14 15:36
 **/
@RestController
@RequestMapping("data")
public class DataRecordController {

    @Autowired
    private IDataRecordService dataRecordService;

    /**
     * @Description: 查询健康档案树形菜单
     * @author: Hu
     * @since: 2021/11/22 15:21
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.DataRecordEntity>>
     */
    @GetMapping("treeForm")
    public R<List<DataRecordEntity>> treeForm(@RequestBody DataRecordEntity dataRecordEntity){
        ValidatorUtils.validateEntity(dataRecordEntity,DataRecordEntity.DataRecordValidate.class);
        return R.ok(dataRecordService.treeForm(dataRecordEntity));
    }


    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/12/14 16:01
     * @Param: [dataRecordEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("save")
    public R<Void> save(@RequestBody DataRecordEntity dataRecordEntity){
        ValidatorUtils.validateEntity(dataRecordEntity,DataRecordEntity.DataRecordValidate.class);
        dataRecordService.saveData(dataRecordEntity);
        return R.ok();
    }


    /**
     * @Description: 修改
     * @author: Hu
     * @since: 2021/12/14 16:01
     * @Param: [dataRecordEntity]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PutMapping("update")
    public R<Void> update(@RequestBody DataRecordEntity dataRecordEntity){
        ValidatorUtils.validateEntity(dataRecordEntity,DataRecordEntity.DataRecordValidate.class);
        dataRecordService.updateData(dataRecordEntity);
        return R.ok();
    }


    /**
     * @Description: 删除
     * @author: Hu
     * @since: 2021/12/14 16:01
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @DeleteMapping("delete")
    public R<Void> delete(@RequestParam Long id){
        dataRecordService.delete(id);
        return R.ok();
    }


}
