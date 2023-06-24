package kth.weather.push.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kth.weather.push.dto.ResultDto;
import kth.weather.push.entity.UserSubscriInfo;
import kth.weather.push.repository.UserSuvscriInfoRepository;
import kth.weather.push.service.PwaPushService;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.cli.commands.GenerateKeyCommand;
import nl.martijndwars.webpush.cli.handlers.GenerateKeyHandler;
import org.jose4j.lang.JoseException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
public class PwaPushController {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(PwaPushController.class);


    private final PwaPushService pwaPushService;
    private final UserSuvscriInfoRepository userSuvscriInfoRepository;

    /**
     * 알림허용 등록 브라우저에서 호출하는 주소다.
     *
     * @return ResultDto
     * @throws Exception
     */
    @RequestMapping({"/push/subscribe"})
    @ResponseBody
    public ResultDto subscribe(HttpServletRequest request, HttpServletResponse response, @RequestBody String transactionConfig) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {

        logger.debug("/push/subscribe 호출");
        //@RequestBody 형식으로 전달 받는 데이터는 문자로 넘어온다. 문자 데이터를
        //객체형식으로 바꿔야 한다.
        logger.debug("transactionConfig 문자형식 : " + transactionConfig);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> subscribeMap = new HashMap<String, Object>();
        ResultDto resultDto = new ResultDto();

        try {
            // convert JSON string to Map
            // 문자 형식 데이터를 Map 형식으로 변경하였다.
            subscribeMap = mapper.readValue(transactionConfig, Map.class);
            logger.debug("subscribeMap : " + subscribeMap);
            logger.debug("keys : " + subscribeMap.get("keys"));

            Map<String, Object> mapKeys = (Map<String, Object>) subscribeMap.get("keys");

            logger.debug("mapKeys===> : " + mapKeys);
            //화면에서 구독 알림 허용 요청시 파라미터를 UserSubscriInfo
            // 엔티티 객체에 담는다. 엔티티가 db에서 테이블이라고 생각하면 된다.
            UserSubscriInfo userSubscriInfo = new UserSubscriInfo();
            userSubscriInfo.setAuthor(mapKeys.get("auth").toString());

            userSubscriInfo.setP256dh(mapKeys.get("p256dh").toString());
            userSubscriInfo.setEndpoint(subscribeMap.get("endpoint").toString());
            // userSubscriInfo.setExpirationTime(subscribeMap.get("expirationTime").toString());
            userSubscriInfo.setUseYn("Y");
            //객체에 담은후에 jpa에서 제공해주는 save함수 호출하여 db에 정보를 저장한다.
            userSuvscriInfoRepository.save(userSubscriInfo);

            resultDto.setResultCode("SUCCESS");
            resultDto.setErrorMsg("");

        } catch (IOException e) {
            e.printStackTrace();
            resultDto.setResultCode("ERROR");
            resultDto.setErrorMsg("IOException");
        } catch (Exception e) {
            resultDto.setResultCode("ERROR");
            resultDto.setErrorMsg("Exception");
        }
        return resultDto;
    }

    @RequestMapping({"/push/unsubscribe"})
    public ResultDto unsubscribe(HttpServletRequest request, HttpServletResponse response, @RequestBody String transactionConfig) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {

        logger.debug("/push/unsubscribe 호출");


        logger.debug("transactionConfig 문자형식 : " + transactionConfig);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> subscribeMap = new HashMap<String, Object>();
        ResultDto resultDto = new ResultDto();

        try {
            // convert JSON string to Map
            // 문자를 Map 형식으로 변환한다.
            subscribeMap = mapper.readValue(transactionConfig, Map.class);
            logger.debug("subscribeMap : " + subscribeMap);
            logger.debug("keys : " + subscribeMap.get("keys"));

            Map<String, Object> mapKeys = (Map<String, Object>) subscribeMap.get("keys");

            logger.debug("mapKeys===> : " + mapKeys);
            //구독취소 요청이 오면 db에 저장된 구독 정보를 삭제한다.
            userSuvscriInfoRepository.deleteByP256dh(mapKeys.get("p256dh").toString());

            resultDto.setResultCode("SUCCESS");
            resultDto.setErrorMsg("");

        } catch (IOException e) {
            e.printStackTrace();
            resultDto.setResultCode("ERROR");
            resultDto.setErrorMsg("IOException");
        } catch (Exception e) {
            resultDto.setResultCode("ERROR");
            resultDto.setErrorMsg("Exception");
        }
        return resultDto;
    }

    @RequestMapping({"/push/sendMessage"})
    public ResultDto sendMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody String transactionConfig) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {

        logger.debug("/push/sendMessage");


        logger.debug("transactionConfig 문자형식 : " + transactionConfig);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> messageMap = new HashMap<String, Object>();
        ResultDto resultDto = new ResultDto();

        try {
            // convert JSON string to Map
            // 알림메세지를 읽어서 map형식으로 변환한다.
            messageMap = mapper.readValue(transactionConfig, Map.class);
            logger.debug("messageMap : " + messageMap);
            //구독요청한 정보를 list로 읽어서 push알림을 전송한다.
            List<UserSubscriInfo> userSubscriInfo = userSuvscriInfoRepository.findAll();

            pwaPushService.sendMessage(messageMap, userSubscriInfo);

        } catch (IOException e) {
            e.printStackTrace();
            resultDto.setResultCode("ERROR");
            resultDto.setErrorMsg("IOException");
        } catch (Exception e) {
            resultDto.setResultCode("ERROR");
            resultDto.setErrorMsg("Exception");
        }
        return resultDto;
    }

    /**
     * 암호화 키생성
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping({"/push/keyGenerater"})
    public void keyGenerater(HttpServletRequest request, HttpServletResponse response) {

        logger.debug("/push/keyGenerater 호출");

        GenerateKeyCommand generateKeyCommand = new GenerateKeyCommand();

        try {
            //암호화 키를 생성하여 console창에 public키와 private키를 출력한다.
            new GenerateKeyHandler(generateKeyCommand).run();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
