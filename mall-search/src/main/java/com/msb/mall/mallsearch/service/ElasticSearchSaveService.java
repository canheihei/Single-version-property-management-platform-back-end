package com.msb.mall.mallsearch.service;


import com.chhei.common.dto.es.SkuESModel;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchSaveService {

    Boolean productStatusUp(List<SkuESModel> skuESModels) throws IOException;
}
