package kth.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kth.weather.save.dto.ItemsDto;
import kth.weather.save.entity.Item;
import kth.weather.save.repository.ItemRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {

    @Autowired
    private ItemRepository itemRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${weather}")
    private String apiKey;

    @Test
    public void aa() throws JSONException {
        System.out.println(apiKey);

        String sendUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

        Map<String, Object> paramMap = new HashMap<>();

        LocalDate localDate = LocalDate.now();
        String yyyyMMdd = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        paramMap.put("serviceKey", apiKey);
        paramMap.put("numOfRows", 10);
//        paramMap.put("numOfRows", 216);
        paramMap.put("pageNo", 1);
        paramMap.put("dataType", "JSON");
        paramMap.put("base_date", yyyyMMdd);
        paramMap.put("base_time", "0500");
        paramMap.put("nx", "58");
        paramMap.put("ny", "126");

        String param = mapToParam(paramMap);

        String inputLine = null;
        StringBuilder outResult = new StringBuilder();

        try {
            System.out.println(("REST API Start"));
            URL url = new URL(sendUrl + param);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 리턴된 결과 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            conn.disconnect();
            System.out.println(("REST API End"));
        } catch (
                Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("outResult = " + outResult);

        JSONObject object = new JSONObject(outResult.toString()).getJSONObject("response").getJSONObject("body").getJSONObject("items");

        String data = object.toString();
        ObjectMapper mapper = new ObjectMapper();
        ItemsDto testVo = new ItemsDto();
        try {
            testVo = mapper.readValue(data, ItemsDto.class);
            System.out.println("testVo = " + testVo);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<Item> collect = testVo.getItem()
                .stream()
                .map(p -> modelMapper.map(p, Item.class))
                .collect(Collectors.toList());
        List<Item> s = itemRepository.saveAll(collect);
        System.out.println("done");
    }

    private String mapToParam(Map<String, Object> paramMap) {
        if (paramMap != null) {
            StringBuffer sb = new StringBuffer();
            for (String key : paramMap.keySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(paramMap.get(key));
            }
            return "?" + sb.toString();
        }
        return null;
    }


}
