package com.chhei.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 二级分类需要展示的数据VO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catalog2VO {

    private String  catalog1Id; // 二级分类对应的一级父类的编号
    private List<Catalog3VO> catalog3List; // 二级分类对应的三级分类的数据
    private String id; // 二级分类的编号
    private String name ; // 二级分类对应的类别名称


    /**
     * 三级分类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catalog3VO{

        private String catalog2Id ; // 三级分类对应的二级分类的编号
        private String id; // 三级分类编号
        private String name; // 三级分类名称

    }
}
