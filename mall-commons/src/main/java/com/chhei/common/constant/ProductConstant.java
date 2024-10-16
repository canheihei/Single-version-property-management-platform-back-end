package com.chhei.common.constant;

public class ProductConstant {
	/*public static final Integer ATTR_TYPE_BASE = 1;
	public static final Integer ATTR_TYPE_SALE = 1;*/
	public enum AttrEnum{
		ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");
		private int code;
		private String msg;
		AttrEnum(int code,String msg){
			this.code = code;
			this.msg = msg;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}
