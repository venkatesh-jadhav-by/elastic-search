package com.demo.elasticsearch.service;

import com.demo.elasticsearch.document.Shipment;
import com.demo.elasticsearch.helper.Indices;
import com.demo.elasticsearch.search.SearchRequestDTO;
import com.demo.elasticsearch.search.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.openjdk.jmh.annotations.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ShipmentService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(ShipmentService.class);

    private final RestHighLevelClient client;

    @Autowired
    public ShipmentService(RestHighLevelClient client) {
        this.client = client;
    }

@Benchmark
    public List<Shipment> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.Shipment_Index,
                dto
        );

        return searchInternal(request);
    }

    private List<Shipment> searchInternal(final SearchRequest request) {
        if (request == null) {
            LOG.error("Failed to build search request");
            return Collections.emptyList();
        }
        System.out.println("requestjson=="+request);
        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[] searchHits = response.getHits().getHits();
            final List<Shipment> shipments = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                shipments.add(
                        MAPPER.readValue(hit.getSourceAsString(), Shipment.class)
                );
            }

            return shipments;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
