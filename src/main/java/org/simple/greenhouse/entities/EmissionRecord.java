package org.simple.greenhouse.entities;

import jakarta.persistence.Entity;           
import jakarta.persistence.GeneratedValue;  
import jakarta.persistence.GenerationType;  
import jakarta.persistence.Id;              
import jakarta.persistence.Table;           

@Entity                                      // tells hibernate this is an entity
@Table(name = "emissions")                  // map to emissions table in mysql
public class EmissionRecord {              // represents one emission row

    @Id                                      // primary key field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql auto increment
    private int id;                          // unique id for each emission record

    private int year;                        // year of the emission (must be 2023 as per requirements )
    private String scenario;                 // scenario, must be with exisiting measures "wem"
    private double value;                    // emission value must be > 0
    private String gasUnit;                     // unit of measurement 
    private String categoryCode;             // ipcc category code
    private String categoryDescription;      // description from ipcc site
    private String sourceType;               // "predicted" or "actual"

    private boolean approved;                // true if this record is approved

    // no reference to user here - unidirectional relationship from user only

    public EmissionRecord() {                
    }


    public int getId() {                     
        return id;                           
    }

    public void setId(int id) {        
        this.id = id;                      
    }

    public int getYear() {                   
        return year;                         
    }

    public void setYear(int year) {          
        this.year = year;                    
    }

    public String getScenario() {            
        return scenario;                     
    }

    public void setScenario(String scenario) { 
        this.scenario = scenario;           
    }

    public double getValue() {               
        return value;                     
    }

    public void setValue(double value) {     
        this.value = value;                 
    }

    public String getGasUnit() {                
        return gasUnit;                         
    }

    public void setGasUnit(String gasUnit) {       
        this.gasUnit = gasUnit;                    
    }

    public String getCategoryCode() {        
        return categoryCode;                 
    }

    public void setCategoryCode(String categoryCode) { 
        this.categoryCode = categoryCode;   
    }

    public String getCategoryDescription() { 
        return categoryDescription;          
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription; 
    }

    public String getSourceType() {          
        return sourceType;                  
    }

    public void setSourceType(String sourceType) { 
        this.sourceType = sourceType;        
    }

    public boolean isApproved() {            
        return approved;                     
    }
    

    public void setApproved(boolean approved) { 
        this.approved = approved;            
    }


}

