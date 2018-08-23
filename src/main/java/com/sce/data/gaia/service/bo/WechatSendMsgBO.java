package com.sce.data.gaia.service.bo;

import lombok.Data;

import java.util.Map;

@Data
public class WechatSendMsgBO {
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype;
    private String agentid;
    private Map<String,String> text;
    private int safe;
}
