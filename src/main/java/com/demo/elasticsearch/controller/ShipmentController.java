package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.document.Shipment;
import com.demo.elasticsearch.search.SearchRequestDTO;
import com.demo.elasticsearch.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {
    @Autowired
    ShipmentService service;


    @PostMapping("/search")
    public List<Shipment> search(@RequestBody final SearchRequestDTO dto) {
        System.out.println("inside controller");
        return service.search(dto);
    }

}
