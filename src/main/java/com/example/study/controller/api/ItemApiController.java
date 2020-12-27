package com.example.study.controller.api;

import com.example.study.controller.CrudController;
import com.example.study.model.entity.Item;
import com.example.study.model.network.request.ItemApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemApiController extends CrudController<ItemApiRequest, ItemApiResponse, Item> {

}
