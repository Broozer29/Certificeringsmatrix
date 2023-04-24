package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Readers.CVReader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.CVStorage;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

public class CVLoader {

	private static CVLoader instance = new CVLoader();
	private List<String> fileNames = new ArrayList<String>();

	private CVLoader() {
		loadCVFileNames();
	}

	public static CVLoader getInstance() {
		return instance;
	}

	public void readAllCVs() {
		CVReader reader = CVReader.getInstance();
		for (String fileName : fileNames) {
			try {
				reader.readDocxFile(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
