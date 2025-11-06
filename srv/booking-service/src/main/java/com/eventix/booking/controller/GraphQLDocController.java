package com.eventix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GraphQL", description = "GraphQL endpoint for advanced statistics and flexible queries")
@RestController
public class GraphQLDocController {

    @Operation(
        summary = "Execute a GraphQL query",
        description = """
            Executes GraphQL queries or mutations.
            <br><br>
            Example payload:
            <pre>{
              "query": "query { eventStats(eventId: 202) { eventId totalBookings avgRating city } }"
            }</pre>
            """
    )
    @ApiResponse(responseCode = "200", description = "GraphQL query executed successfully")
    @PostMapping("/graphql")
    public ResponseEntity<String> graphql(@RequestBody String body) {
        return ResponseEntity.ok("GraphQL endpoint placeholder for Swagger UI");
    }
}

