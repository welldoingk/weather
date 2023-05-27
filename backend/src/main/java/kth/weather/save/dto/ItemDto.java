package kth.weather.save.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Long no;
    private String baseDate;
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private int nx;
    private int ny;
}
