package com.zhsj.community.yanglao_yiliao.myself.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.common.constant.BusinessEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 公共常量
 * @author: Hu
 * @create: 2021-11-11 11:16
 **/
@RestController
@RequestMapping("source")
public class SourceController {


    /**
     * @Description: 初始化静态代码块
     * @author: Hu
     * @since: 2021/11/11 11:14
     * @Param: []
     * @return: void
     */
    @PostConstruct
    public void initSource(){
        System.out.println(BusinessEnum.FamilyRelationTextEnum.familyRelationTextList);
    }

    /**
     * @Description: 获取公共常量
     * @author: Hu
     * @since: 2021/11/11 10:43
     * @Param: []
     * @return: com.zhsj.basecommon.vo.R<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    @GetMapping("typeSource")
    public R<List<Map<String, Object>>> source(@RequestParam String typeName){
        return R.ok(BusinessEnum.sourceMap.get(typeName));
    }

}
