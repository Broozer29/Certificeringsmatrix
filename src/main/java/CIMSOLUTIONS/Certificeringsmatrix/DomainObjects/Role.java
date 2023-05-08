package CIMSOLUTIONS.Certificeringsmatrix.DomainObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Role {

	private String role;
	private List<Competence> competencesBelongingToRole = new ArrayList<Competence>();

	public Role(String role) {
		this.role = role;
	}

	public void addCompetence(Competence competence) {
		if (!competencesBelongingToRole.contains(competence)) {
			this.competencesBelongingToRole.add(competence);
		}
	}

	public List<Competence> getCompetences() {
		return competencesBelongingToRole;
	}

	public String getRole() {
		return role;
	}
	
	
	// Sorts the competences in descending order
	public void sortCompetencesDescendingOrder() {
		Comparator<Competence> sortByTfidfScoreDescending = new Comparator<Competence>() {
		    @Override
		    public int compare(Competence c1, Competence c2) {
		        return c2.getTFIDFScore().compareTo(c1.getTFIDFScore());
		    }
		};
	    Collections.sort(this.getCompetences(), sortByTfidfScoreDescending);
	}
	
}
