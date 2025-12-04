package org.simple.greenhouse.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.simple.greenhouse.dao.EmissionRecordDao;
import org.simple.greenhouse.entities.EmissionRecord;

import java.util.List;

@ApplicationScoped // quarkus will manage one instance of this service
public class DescriptionsImportService {

    @Inject
    EmissionRecordDao emissionDao; // dao for emissions table

    @Transactional // run whole update in a single transaction
    public int importDescriptionsFromWeb() { // endpoint used in ShowImportsService

        List<EmissionRecord> emissions = emissionDao.findAll(); // load all emission records from db
        int updated = 0; // counter for how many rows i update

        for (EmissionRecord rec : emissions) { // loop through each emission row

            String code = rec.getCategoryCode(); // grab the category code
            String existingDesc = rec.getCategoryDescription(); // current description value

            if (existingDesc != null && !existingDesc.isBlank()) { // if it already has a description
                continue; // skip this row and do nothing
            }

            if (code == null) { // if there is no category code
                continue; // nothing to match, skip
            }

            String trimmed = code.trim(); // clean up spaces around the code

            // if the category starts with 4.A 
            if (trimmed.startsWith("4.A")) { // check for this category
                rec.setCategoryDescription("Municipal Solid Waste (MSW) Generation Rate"); // set my hard coded description
                emissionDao.update(rec); // persist the change
                updated++; // increment the updated counter
            }
        }

        return updated; // return how many records were updated so the rest endpoint can show it
    }
}
