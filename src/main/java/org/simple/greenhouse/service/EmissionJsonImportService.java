package org.simple.greenhouse.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.simple.greenhouse.dao.EmissionRecordDao;
import org.simple.greenhouse.entities.EmissionRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@ApplicationScoped  // tells quarkus to only create one shared instance of this class for the whole life of the application                                 
public class EmissionJsonImportService {

    @Inject
    EmissionRecordDao emissionDao;                  // dao used to save emissionrecord entities
    

    @Transactional                                  // wrap whole import in one transaction
    public int importActualFromJson() throws Exception {

   
    	InputStream is = getClass().getResourceAsStream("/GreenhouseGasEmissions2025.json");
        if (is == null) {
            throw new IllegalStateException("GreenhouseGasEmissions2025.json not found on classpath");
        }

        // read the whole file into a single string (similar idea to scanner in lecture)
        StringBuilder jsonString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        }

        // parse the json using json-simple (same library as jsonrequestandparse)
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(jsonString.toString());

        // optional: read year from the "data" section to show we understand the format
        int year = 2023; // default
        JSONObject dataObj = (JSONObject) root.get("data");
        if (dataObj != null) {
            Object dateValue = dataObj.get("date");        // e.g. "2023"
            if (dateValue != null) {
                try {
                    year = Integer.parseInt(dateValue.toString().trim());
                } catch (NumberFormatException ignored) {
                    // if parse fails we just keep default year 2023
                }
            }
        }

        // get the emissions array from the root object
        JSONArray emissionsArray = (JSONArray) root.get("Emissions");
        if (emissionsArray == null) {
            throw new IllegalStateException("json does not contain 'Emissions' array");
        }

        int count = 0;                                    // track how many records we insert

        for (Object o : emissionsArray) {
            JSONObject item = (JSONObject) o;

            String category  = (String) item.get("Category");
            String gasUnits  = (String) item.get("Gas Units");
            Number valueNum  = (Number) item.get("Value");
     

            // skip entries with no numeric value
            if (valueNum == null) {
                continue;
            }

            double value = valueNum.doubleValue();

            // apply assignment rules: value > 0 and year must be 2023
            if (value <= 0) {
                continue;
            }
            if (year != 2023) {
                continue;                                  // keeps us in sync with ca spec
            }

            // create new emissionrecord and map json fields onto entity fields
            EmissionRecord record = new EmissionRecord();
            record.setYear(year);
            record.setScenario("WEM");                     // assume wem scenario for actuals
            record.setValue(value);
            record.setGasUnit(gasUnits);
            record.setCategoryCode(category);
            // later you can plug in categorydescription from your ipcc mapping
            record.setSourceType("ACTUAL");                // mark these as actual readings
            record.setApproved("Not Approved");                     // imported data starts unapproved

            // persist entity to mysql using hibernate via dao
            emissionDao.save(record);

            count++;
        }

        // return number of actual records we imported – useful in the admin endpoint
        return count;
    }
}
