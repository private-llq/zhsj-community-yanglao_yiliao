package com.zhsj.yanglao_yiliao.interfaces.myself;

import com.zhsj.basecommon.vo.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos
 * @author: Hu
 * @create: 2021-12-08 15:19
 **/
@FeignClient(name = "community-yanglao-yiliao-myself-service",path = "/sos")
public interface SosFeign {

    /**
     * @Description: 根据用户uid查询绑定家属和绑定机构
     * @author: Hu
     * @since: 2021/12/8 15:20
     * @Param: [uid]
     * @return: com.zhsj.basecommon.vo.R<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @GetMapping("selectUser")
    R<Map<String,Object>> selectUser(@RequestParam("uid") String uid);
}
