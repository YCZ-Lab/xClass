package com.ysh.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class MobilePhoneCodeSendService {

    public String send(String mobilePhone, String code) {
        if (mobilePhone.length() != 10) {
            return "Phone number length is wrong !";
        }
        try {
            Long.valueOf(mobilePhone);
        } catch (NumberFormatException e) {
            return "Phone number format is incorrect !";
        }
        try {
            mobilePhone = "+1" + mobilePhone;
            System.out.println(mobilePhone + " --> " + code);
            SnsClient snsClient = SnsClient.builder().region(Region.US_EAST_2).build();
            PublishRequest request = PublishRequest.builder().message("[xClass] Verification code: " + code).phoneNumber(mobilePhone).build();
            PublishResponse response = snsClient.publish(request);
            System.out.println(response.messageId() + " Message Sent.");
            System.out.println("Status was " + response.sdkHttpResponse().statusCode());
            return null;
        } catch (SnsException e) {
            System.out.println(e);
        }
        return "Unknow Error !";
        //            SnsClient snsClient = SnsClient.builder().region(Region.US_EAST_2).build();
//            PublishRequest publishRequest = PublishRequest.builder().topicArn("arn:aws:sns:us-east-2:092515459278:TEST").subject("subject-test").message("messages-TEST").build();
//            PublishResponse publishResponse = snsClient.publish(publishRequest);
//            System.out.println(publishResponse);

    }
}
