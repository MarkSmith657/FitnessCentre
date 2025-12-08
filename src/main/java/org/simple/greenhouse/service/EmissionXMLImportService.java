package org.simple.greenhouse.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.simple.greenhouse.dao.EmissionRecordDao;
import org.simple.greenhouse.entities.EmissionRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

@ApplicationScoped                           
public class EmissionXMLImportService {

    @Inject
    EmissionRecordDao emissionDao;           // dao that hides entitymanager / sql from this class

    @Transactional                           // quarkus opens a transaction for this whole method
    public int importPredictedFromXml() throws Exception {

        // load xml file from my classpath 
        InputStream is = getClass().getResourceAsStream("/GasEmissionsProjections.xml");
        if (is == null) {
            // if file is missing we crash early so i can see the problem in the logs
            throw new IllegalStateException("GasEmissionsProjections.xml not found on classpath");
        }

        // standard dom setup – same pattern as my the readusers example
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);     // parses the xml bytes into a dom document tree 

        doc.getDocumentElement().normalize(); // normalises whitespace nodes inside the dom

        // every data record is inside a <row> element in the xml file
        NodeList rows = doc.getElementsByTagName("Row");

        int count = 0;                        // sets count to 0

        for (int i = 0; i < rows.getLength(); i++) {

            Node row = rows.item(i);

            // only process element nodes ignore all other nodes 
            if (row.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) row; // safe cast now that we know it is an element and name it elem

                // pull out the child tags necessary - no nk
                String category    = getTagText(elem, "Category__1_3"); // helper method used here to reduce repetitve code as seen below
                /* 
                  NodeList categoryNodes = elem.getElementsByTagName("Category__1_3");
                  String category = null;
                  if (categoryNodes != null && categoryNodes.getLength() > 0) {
                  category = categoryNodes.item(0).getTextContent();
                  }
                 */
                String yearString  = getTagText(elem, "Year");
                String scenario    = getTagText(elem, "Scenario");
                String gasUnits    = getTagText(elem, "Gas___Units");
                String valueString = getTagText(elem, "Value");

            
                // if any key field is missing or blank we skip this row and move on
                if (yearString == null || yearString.trim().isEmpty() ||
                    scenario == null   || scenario.trim().isEmpty()   ||
                    valueString == null || valueString.trim().isEmpty()) {
                    continue;
                }


                int year   = Integer.parseInt(yearString.trim());
                double value = Double.parseDouble(valueString.trim());

                // apply the rules from requirements
                //  only year 2023
                //  only scenario wem ( with existing measures ) 
                //  value must be > 0
                if (year != 2023) continue;
                if (!"WEM".equalsIgnoreCase(scenario.trim())) continue;
                if (value <= 0) continue;

                // at this point the row is valid, so we map it into a jpa entity
                EmissionRecord record = new EmissionRecord();
                record.setYear(year);                       // year column in db
                record.setScenario(scenario);               // scenario column in db
                record.setValue(value);                     // numeric emissions value
                record.setGasUnit(gasUnits);                // e.g. "co2 (kt)"
                record.setCategoryCode(category);           // ipcc category code from xml
                record.setSourceType("PREDICTED");          // marks this as predicted data
                record.setApproved("Not Approved");                  // newly imported data is not approved

                // persist the entity using hibernate via my dao
                emissionDao.save(record);

                count++;                                   // track how many we successfully stored
            }
        }

        // method returns the number of rows imported 
        return count;
    }

    // small helper so i don't repeat getelementsbytagname logic in the main loop
    private String getTagText(Element parent, String tagName) {
        // look for child elements with this tag name under the parent <Row> element
        NodeList list = parent.getElementsByTagName(tagName);

        // if there is no such tag, say "there is no value" by returning null
        if (list == null || list.getLength() == 0) {
            return null;                                   // tag did not exist under this row
        }

        // otherwise take the first matching tag and return its inner text
        return list.item(0).getTextContent();              
    }

}
