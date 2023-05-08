package CIMSOLUTIONS.Certificeringsmatrix.DomainObjects;

/*- This class is a representation of the certificeringsmatrix
 *  It contains all given roles and the roles contain their corresponding competences
 *  
 */
import java.util.ArrayList;
import java.util.List;

public class CertificeringsMatrix {

	private List<Role> allRoles = new ArrayList<Role>();

	public CertificeringsMatrix(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public List<Role> getAllRoles() {
		return this.allRoles;
	}

	// Sorts roles by the IF-TDF score of a competence in descending order
	public void sortCompetencesBelongingToRoles() {
		for (Role role : allRoles) {
			role.sortCompetencesDescendingOrder();
		}
	}

}
