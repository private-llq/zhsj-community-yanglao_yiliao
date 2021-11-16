package com.zhsj.community.yanglao_yiliao.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 查询入参基础类
 * @Author chq459799974
 * @Date 2020/11/11 9:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseQo<T> implements Serializable {
	
	private Long page=1L;
	
	private Long size=10L;

	private T query;

}
