package com.tech.brain.controller;

import com.tech.brain.model.Product;
import com.tech.brain.service.QueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/query/v1")
public class QueryController {
    private final QueryService queryService;

    public QueryController(QueryService commandService) {
        this.queryService = commandService;
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieves a list of all available products in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products returned")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Product>> allProduct(){
        return ResponseEntity.ok().body(queryService.getAllProduct());
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieves product details using its unique database ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/by_id/{id}")
    public ResponseEntity<Product> byProductId(@PathVariable long id){
        return ResponseEntity.ok().body(queryService.getProductById(id));
    }

    @Operation(
            summary = "Get product by product code",
            description = "Retrieves product using its business product code (e.g., PROD001)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @GetMapping("/by_product_code/{name}")
    public ResponseEntity<Product> byProductCode(@PathVariable String name){
        return ResponseEntity.ok(queryService.getProductByProductCode(name));
    }
}
