package com.demo.elasticsearch.service;

import com.demo.elasticsearch.helper.Indices;
import com.demo.elasticsearch.search.SearchRequestDTO;
import com.demo.elasticsearch.search.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 200, time = 1, timeUnit = TimeUnit.MILLISECONDS)
//@Fork(value = 3, jvmArgsAppend = {"-XX:+UseParallelGC", "-Xms1g", "-Xmx1g"})
@Fork(value = 1)
@BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(org.openjdk.jmh.annotations.Scope.Benchmark)
@Service("myelastic")
public class ShipmentServiceDemo {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceDemo.class);

    private final RestHighLevelClient client;

    @Autowired
    public ShipmentServiceDemo(RestHighLevelClient client) {
        this.client = client;
    }

@Benchmark
    public List<Object> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.Bridge,
                dto
        );

        return searchInternal(request);
    }

    private List<Object> searchInternal(final SearchRequest request) {
        if (request == null) {
            LOG.error("Failed to build search request");
            return Collections.emptyList();
        }
        System.out.println("requestjson=="+request);
        try {
           // Query query=new SearchQuery("{ \"match\": { \"firstname\": { \"query\": \"Jack\" } } } ");
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[] searchHits = response.getHits().getHits();
            final List<Object> shipments = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                shipments.add(
                        MAPPER.readValue(hit.getSourceAsString(), Object.class)
                );
            }

            return shipments;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    public Boolean index(final Object obj, String id) {
        try {
            final String vehicleAsString = MAPPER.writeValueAsString(obj);

            final IndexRequest request = new IndexRequest(Indices.Shipment_Index);


            request.id(id);
            request.source(vehicleAsString, XContentType.JSON);

            //     final IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            BulkRequest bulk=new BulkRequest(Indices.Shipment_Index);
            bulk.add(request);
            final BulkResponse response  =  client.bulk(bulk, RequestOptions.DEFAULT);
            return response != null && response.status().equals(RestStatus.OK);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

}
