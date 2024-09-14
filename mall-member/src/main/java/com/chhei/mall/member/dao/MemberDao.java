package com.chhei.mall.member.dao;

import com.chhei.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:10:04
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
