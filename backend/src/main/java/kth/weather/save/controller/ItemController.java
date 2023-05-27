package kth.weather.save.controller;

import kth.weather.save.dto.ItemDto;
import kth.weather.save.dto.ItemsDto;
import kth.weather.save.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @RequestMapping("/save")
    public ItemsDto saveData() {
        return itemService.saveData();
    }

    @RequestMapping("/get")
    public List<ItemDto> getData() {
        return itemService.getData();

    }

}
