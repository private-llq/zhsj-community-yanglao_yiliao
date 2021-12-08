package com.zhsj.yanglao_yiliao.interfaces.myself;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: Hu
 * @create: 2021-12-08 15:28
 **/
@FeignClient(name = "community-yanglao-yiliao-myself-service",path = "family")
public interface FamilyRecordFeign {

    /**
     * @Description: 根据uid查询家人档案
     * @author: Hu
     * @since: 2021/12/8 15:27
     * @Param:
     * @return:
     */
    @PostMapping("userList")
    R<List<FamilyRecordEntity>> userList(@RequestParam String uid);
}
