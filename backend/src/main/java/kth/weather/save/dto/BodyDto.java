package kth.weather.save.dto;

import lombok.Data;

@Data
public class BodyDto {

    private String dataType;
    private ItemsDto items;
    private String pageNo;
    private String numOfRows;
    private String totalCount;

}
