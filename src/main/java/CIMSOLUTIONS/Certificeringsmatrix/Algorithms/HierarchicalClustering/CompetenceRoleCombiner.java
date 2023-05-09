package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.Sorting.LinkedHashMapSorter;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.CertificeringsMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Role;

public class CompetenceRoleCombiner {

	public CompetenceRoleCombiner() {

	}

	/*- Goes over all competences and roles and get the similarity score from the Word2vec matrix
	 *  Then save the competences that match the role to a LinkedHashMap, then sort the LinkedHashMap in descending order
	 *  Then select the first 5 competences of the sorted LinkedHashMap and add them to the role
	 */
	public CertificeringsMatrix addCompetencesToRoles(List<Competence> competences, List<Role> roles, double similarityThreshold) {
		WordVectorMatrix word2VecMatrix = WordVectorMatrix.getInstance();
		
		for (Role role : roles) {
			LinkedHashMap<Competence, Double> competenceSimilarities = new LinkedHashMap<Competence, Double>();
			for (Competence competence : competences) {
				String[] splittedWords = role.getRole().split("\\s+");
				int wordCount = splittedWords.length;
				if (wordCount > 1) {
					Double score = word2VecMatrix.getSimilarityOfMultiTermWords(role.getRole(),
							competence.getCompetence());
					if (score > similarityThreshold) {
						competenceSimilarities.put(competence, score);
					}
				} else {
					Double similarityScore = word2VecMatrix.getSimilarity(role.getRole(), competence.getCompetence());
					if (similarityScore > similarityThreshold) {
						competenceSimilarities.put(competence, similarityScore);
					}
				}
			}

			LinkedHashMapSorter sorter = LinkedHashMapSorter.getInstance();
			competenceSimilarities = sorter.sortCompetencesDescending(competenceSimilarities);

			int index = 0;
			for (Map.Entry<Competence, Double> entry : competenceSimilarities.entrySet()) {
				if (index > 5) {
					break;
				}
				role.addCompetence(entry.getKey());
				index++;
			}

		}
		return new CertificeringsMatrix(roles);
		
	}

}
