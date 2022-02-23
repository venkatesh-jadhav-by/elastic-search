package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.search.SearchRequestDTO;
import com.demo.elasticsearch.service.ShipmentServiceDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

//import com.demo.elasticsearch.service.ShipmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.demo.elasticsearch.document.Shipment;
//import com.demo.elasticsearch.search.SearchRequestDTO;
//import com.demo.elasticsearch.service.ShipmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/shipment")
//public class ShipmentController {
//    @Autowired
//    ShipmentService service;
//
//
//    @PostMapping("/search")
//    public List<Object> search(@RequestBody final SearchRequestDTO dto) {
//        System.out.println("inside controller");
//        return  service.search(dto);
//
//    }
//
//    @GetMapping("/searc")
//    public String searc() {
//        System.out.println("inside controller");
//
//        return "index";
//    }
//
//}
@Controller
public class ShipmentController {
    @Autowired
    ShipmentServiceDemo service;

    @GetMapping("/search")
    public String index(Model model, String keyword) {
        SearchRequestDTO dto = new SearchRequestDTO();
        List<String> field = new ArrayList<>();
//        field.add("userStatus");
//        field.add("trackingNumber");
//        field.add("shipmentId");
//        field.add("sourceLocation");
//        field.add("destinationLocation");
//        field.add("itemValidationType");
//        field.add("loactionName");
//        field.add("customerClass");
//        field.add("statusVerificationCode");
//        field.add("priority");
//        field.add("units");


        dto.setFields(field);
        dto.setSearchTerm(keyword);
        dto.setSortBy("_index");
        List<Object> list = service.search(dto);
        System.out.println("listSize" + list.size());
        model.addAttribute("list", list);

        return "index";
    }


}
