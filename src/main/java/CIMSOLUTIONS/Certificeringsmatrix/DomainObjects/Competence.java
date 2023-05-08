package CIMSOLUTIONS.Certificeringsmatrix.DomainObjects;

/*- The representation of a "Competentie" within the application
 * 
 */
public class Competence {

	private String competence;
	private Double tfidfScore;

	public Competence(String competence, Double tfidfScore) {
		this.competence = competence;
		this.tfidfScore = tfidfScore;
	}

	public String getCompetence() {
		return competence;
	}
	
	public Double getTFIDFScore() {
		return this.tfidfScore;
	}

}
