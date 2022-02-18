package com.zhsj.community.yanglao_yiliao.myself.bo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description:
 * @date 2021/12/30 17:24
 */
@Data
public class FamilyRecordPageReqBo {

    /**
     * 用户姓名
     */
    private String name;

    @NotNull(message = "页码不能为空")
    @Range(min = 1)
    private Integer pageNo;

    @NotNull(message = "每页显示条数不能为空")
    @Range(min = 1)
    private Integer pageSize;
}
