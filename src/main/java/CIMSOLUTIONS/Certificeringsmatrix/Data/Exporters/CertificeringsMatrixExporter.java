package CIMSOLUTIONS.Certificeringsmatrix.Data.Exporters;

import java.io.FileWriter;
import java.io.IOException;

import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.CertificeringsMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Role;

public class CertificeringsMatrixExporter {

	public CertificeringsMatrixExporter() {
	}

	// Count is currently hardcoded to 5, probably shouldnt be hardcoded here nor should it
	// be 5. Product owner betrekken
	public void exportMatrix(CertificeringsMatrix matrix) {
		String fileName = "Roles with competences.txt";
		try (FileWriter writer = new FileWriter(fileName)) {
			for (Role role : matrix.getAllRoles()) {
				writer.write("Role: " + role.getRole() + "\n");
				int count = 0;
				for (Competence competence : role.getCompetences()) {
					if (count >= 5) {
						break;
					}
					writer.write("Competence: " + competence.getCompetence() + " with a TF-IDF score of: "
							+ competence.getTFIDFScore() + " \n");
					count++;
				}
				writer.write("\n");
			}
			System.out.println("Exported a matrix");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
