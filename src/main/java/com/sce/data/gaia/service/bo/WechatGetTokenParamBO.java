package com.sce.data.gaia.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WechatGetTokenParamBO {
    private String corId;
    private String corSecret;

}
