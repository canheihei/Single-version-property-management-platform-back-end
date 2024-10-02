package com.chhei.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.sql.ClientInfoStatus;
import java.util.HashSet;

/**
 * 对应的校验注解的校验器
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
	private HashSet<Integer> set = new HashSet<>();

	/**
	 * 初始化的方法
	 * 举例：@ListValue(val={1,0})
	 * 获取到 1 0
	 * @param constraintAnnotation
	 */
	@Override
	public void initialize(ListValue constraintAnnotation) {
		int[] val = constraintAnnotation.val();// 0 1
		for (int i : val) {
			set.add(i);
		}
	}

	/**
	 * 判断校验是否成功的方法
	 * @param value 客户端传递的对应的属性的值 判断value是否在0 ， 1 中
	 * @param context
	 * @return
	 */
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return set.contains(value);
	}
}
