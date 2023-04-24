package CIMSOLUTIONS.Certificeringsmatrix.DomainObjects;

import java.util.ArrayList;
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
	
}
