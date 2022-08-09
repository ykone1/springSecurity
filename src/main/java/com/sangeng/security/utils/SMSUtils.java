package com.sangeng.security.utils;

import org.springframework.stereotype.Component;

/**
 * @Auther:yukemeng Date:2022/4/14-04-14-9:51
 * Description:
 * version:1.0
 */
@Component
public class SMSUtils {

    public String sendSmsAliyun(String mobile) {
       /* DefaultProfile profile = DefaultProfile.getProfile(
                this.aliyunSMSConfig.getRegionId(),
                this.aliyunSMSConfig.getAccessKeyId(),
                this.aliyunSMSConfig.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        String code = RandomUtils.nextInt(100000, 999999) + "";

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(this.aliyunSMSConfig.getDomain());
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", this.aliyunSMSConfig.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", this.aliyunSMSConfig.getSignName());
        request.putQueryParameter("TemplateCode", this.aliyunSMSConfig.getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            if (StringUtils.contains(response.getData(), "\"Code\":\"OK\"")) {
                return code;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
        return "123456";
    }
}
