package com.cl.entity.view;

import com.cl.entity.CaipinxinxiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.cl.utils.EncryptUtil;
 

/**
 * 菜品信息
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2024-04-26 10:51:21
 */
@TableName("caipinxinxi")
public class CaipinxinxiView  extends CaipinxinxiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public CaipinxinxiView(){
	}
 
 	public CaipinxinxiView(CaipinxinxiEntity caipinxinxiEntity){
 	try {
			BeanUtils.copyProperties(this, caipinxinxiEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
