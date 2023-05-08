package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Readers.CVReader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

/*- This class is responsible for loading all CV's and calling the reader to read the contents
 * 
 */
public class CVLoader {

	private List<String> fileNames = new ArrayList<String>();

	public CVLoader() {
		loadCVFileNames();
	}

	// Read all CV's and create documents of them. Then add them to the storagemanager
	public void readAllCVs() {
		CVReader reader = new CVReader();
		List<Document> loadedDocuments = new ArrayList<Document>();

		for (String fileName : fileNames) {
			try {
				Document CV = reader.readDocxFile(fileName);
				if (CV != null) {
					loadedDocuments.add(CV);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		StorageManager storageManager = StorageManager.getInstance();
		storageManager.addDocuments(loadedDocuments);

	}

	// Reads the amount of files in the CV folder
	private void loadCVFileNames() {
		String directoryPath = "resources/CV/";
		File directory = new File(directoryPath);
//		int i = 0;
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					fileNames.add(file.getName());
				}
			} else {
				System.out.println("No files found in the directory.");
			}
		} else {
			System.out.println("The provided path is not a valid directory.");
		}
	}

}
