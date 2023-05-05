package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Role;
/*- This class is responsible for loading all Roles that have to be present in the resulting certificeringsmatrix
 * 
 */
public class RoleLoader {

	private static RoleLoader instance = new RoleLoader();
	private List<Role> roles = new ArrayList<Role>();

	private RoleLoader() {

	}

	public static RoleLoader getInstance() {
		return instance;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void loadAndReadRoles() {
		roles = new ArrayList<Role>();
		String filePath = "resources/Roles/Roles.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split("\n");
				for (String word : words) {
					String cleanedWord = word.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
					Role newRole = new Role(cleanedWord);
					roles.add(newRole);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
