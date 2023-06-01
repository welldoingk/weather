package kth.weather.push.service;

import kth.weather.push.entity.UserSubscriInfo;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.jose4j.base64url.Base64;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PwaPushService {
    private Logger logger = LoggerFactory.getLogger(PwaPushService.class);

    @Value("${publicKey}")
    private String publicKey;
    @Value("${privateKey}")
    private String privateKey;

    @SuppressWarnings("unchecked")
    public void sendMessage(Map<String, Object> messageMap, List<UserSubscriInfo> userSubscriInfo) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {

        logger.debug("publicKey=======>" + publicKey);
        logger.debug("privateKey=======>" + privateKey);

        logger.debug("sendMessage=======>" + messageMap);
        logger.debug("userSubscriInfo=======>" + userSubscriInfo);

        /* 화면에 출력할 내용 셋팅 인자로 넘어온 값을 셋팅해준다. */
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("title", Base64.encode(URLEncoder.encode(messageMap.get("title").toString(), "UTF-8").getBytes()));
        jsonObject.put("body", Base64.encode(URLEncoder.encode(messageMap.get("body").toString(), "UTF-8").getBytes()));
        jsonObject.put("icon", messageMap.get("icon"));  //나타날이미지
        jsonObject.put("badge", messageMap.get("badge"));
        jsonObject.put("vibrate", messageMap.get("vibrate"));
        jsonObject.put("params", messageMap.get("params"));
        jsonObject.put("image", messageMap.get("image"));
        jsonObject.put("requireInteraction", messageMap.get("requireInteraction"));
        jsonObject.put("actions", messageMap.get("actions"));

        String payload = jsonObject.toString();

        logger.debug("payload=======>" + payload);

        PushSubscription pushSubscription = new PushSubscription();
        Notification notification;
        PushService pushService;

        //db에 등록되어 있는 구독자를 가지고 와서 메세지를 전송한다.
        for (UserSubscriInfo userInfo : userSubscriInfo) {
            pushSubscription.setEndpoint(userInfo.getEndpoint());
            pushSubscription.setKey(userInfo.getP256dh());
            pushSubscription.setAuth(userInfo.getAuthor());

            notification = new Notification(pushSubscription.getEndpoint(), pushSubscription.getUserPublicKey(), pushSubscription.getAuthAsBytes(), payload.getBytes());

            pushService = new PushService();
            pushService.setPublicKey(publicKey);
            pushService.setPrivateKey(privateKey);
            pushService.setSubject("mailto:admin@domain.com");
            // Send the notification
            pushService.send(notification);
        }

    }

}