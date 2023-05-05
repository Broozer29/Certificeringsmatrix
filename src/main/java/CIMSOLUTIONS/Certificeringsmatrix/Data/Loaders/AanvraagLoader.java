package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Readers.AanvraagReader;

/*- This class is responsible for loading all Aanvragen and calling the reader to read the contents
 * 
 */
public class AanvraagLoader {

	private List<String> fileNames = new ArrayList<String>();

	public AanvraagLoader() {
		loadAanvraagFileNames();
	}

	public void readAllAanvragen() {
		AanvraagReader aanvraagReader = new AanvraagReader();
		String filePath = "resources/Aanvragen/";
		for(String fileName : fileNames) {
			aanvraagReader.readPFDFile(filePath, fileName);
		}

	}

	private void loadAanvraagFileNames() {
		String directoryPath = "resources/Aanvragen/";
        File directory = new File(directoryPath);

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
