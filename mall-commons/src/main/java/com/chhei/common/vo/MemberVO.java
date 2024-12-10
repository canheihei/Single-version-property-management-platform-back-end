package com.chhei.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员
 *
 * @author dpb
 * @email dengpbs@163.com
 * @date 2021-11-24 19:47:00
 */
@Data
public class MemberVO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long levelId;

	private String username;

	private String password;

	private String nickname;

	private String mobile;

	private String email;

	private String header;

	private Integer gender;

	private Date birth;

	private String city;

	private String job;

	private String sign;

	private Integer sourceType;

	private Integer integration;

	private Integer growth;

	private Integer status;

	private Date createTime;

	private String socialUid;

	private String accessToken;

	private long expiresId;
}
