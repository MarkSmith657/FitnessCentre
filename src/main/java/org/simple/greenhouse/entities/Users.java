package org.simple.greenhouse.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity // marks this class as a jpa entity
@Table(name = "users") // maps this entity to the users table in mysql
public class Users { // entity class representing an application user

    @Id // primary key field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // use mysql auto increment strategy to asasign id 
    private int id; // unique id for each user

    private String username; 

    private String passwordHash; // stores the hashed password might use this later if have time to implement hash 

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)// one user can approve many emission records, load records when user is loaded and delete all associated records to user if user deleted 
    private List<EmissionRecord> approvedEmissions = new ArrayList<>();// list of emissions this user approved
    // used unidirectional again to showcase what hibernate does under the hood

    Users() { 
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public List<EmissionRecord> getApprovedEmissions() {
		return approvedEmissions;
	}

	public void setApprovedEmissions(List<EmissionRecord> approvedEmissions) {
		this.approvedEmissions = approvedEmissions;
	}

   
}
