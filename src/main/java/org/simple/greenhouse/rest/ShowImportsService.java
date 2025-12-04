package org.simple.greenhouse.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.simple.greenhouse.service.EmissionXMLImportService;
import org.simple.greenhouse.service.DescriptionsImportService;
import org.simple.greenhouse.service.EmissionJsonImportService;

@Path("/show/import")                      // base path for all import endpoints
@Produces("application/json")              // all responses will be json
public class ShowImportsService {

    @Inject
    EmissionXMLImportService xmlImportService;   // service that reads the predicted xml file

    @Inject
    EmissionJsonImportService jsonImportService; // service that reads the actual json file
    
    @Inject
    DescriptionsImportService descriptionsImportService; // service for efdb descriptions

    @POST
    @Path("/xml")
    public Response importXml() {
        try {
            int count = xmlImportService.importPredictedFromXml(); 
            // call xml parser and get how many rows were saved to db

            // create simple json string to show the result to user
            String body = "{\"imported\":" + count + ",\"type\":\"PREDICTED\"}";

            return Response.ok(body).build();      // return 200 ok with the count
        } catch (Exception e) {
            e.printStackTrace();                   // print stacktrace for debugging in console

            // return a 500 error so i know something went wrong during parsing
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"xml import failed\"}")
                    .build();
        }
    }

    @POST
    @Path("/json")
    public Response importJson() {
        try {
            int count = jsonImportService.importActualFromJson(); 
            // call json parser and get how many rows were saved to db

            // show response in same format as xml import
            String body = "{\"imported\":" + count + ",\"type\":\"ACTUAL\"}";

            return Response.ok(body).build();      // return success with imported count
        } catch (Exception e) {
            e.printStackTrace();                   // log error in console

            // return error message if json parsing failed
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"json import failed\"}")
                    .build();
        }
    }
    
    @POST 
    @Path("/descriptions/web") 
    public Response importDescriptionsFromWeb() { // rest method that triggers my description import
        try {
            int updated = descriptionsImportService.importDescriptionsFromWeb(); // call my service to add descriptions and get count
            String body = "{\"updated\":" + updated + ",\"source\":\"EFDB_WEB\"}";
            return Response.ok(body).build(); 
        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"description import failed\"}") 
                    .build(); 
        }
    }

}

