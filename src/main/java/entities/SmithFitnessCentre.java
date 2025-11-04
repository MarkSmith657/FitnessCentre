package entities;

//COPY AND PASTED FROM PREVIOUS CA ATTEMPT COMMUNITYFITNESS CENTRE 
//https://github.com/MarkSmith657/CommunityFitnessCentre

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement(name = "smithfitnesscentre")
@NamedQuery(name = "SmithFitnessCentre.findAll", query = "SELECT f FROM SmithFitnessCentre f")
public class SmithFitnessCentre {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int id;

    private String FitnessCentreName;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true) // a fitness centre can have many members but memebers doesnt need not reference it because hibernate will create joint table to link the two 
    // changed fetch type to EAGER so that all members of the fitness centre are loaded immediately when the centre is retrieved preventing LazyInitializationException errors since the collection is accessed after the database session closes
    private List<Members> members = new ArrayList<>();
    
    public SmithFitnessCentre() {}
    
    public SmithFitnessCentre(String FitnessCentreName, List<Members> members) {
    	this.FitnessCentreName = FitnessCentreName;
    	this.members = members;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFitnessCentreName() {
		return FitnessCentreName;
	}

	public void setFitnessCentreName(String fitnessCentreName) {
		FitnessCentreName = fitnessCentreName;
	}

	public List<Members> getMembers() {
		return members;
	}

	public void setMembers(List<Members> members) {
		this.members = members;
	}

}