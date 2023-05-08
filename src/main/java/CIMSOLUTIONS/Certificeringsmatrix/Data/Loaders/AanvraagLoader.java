package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Readers.AanvraagReader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

/*- This class is responsible for loading all Aanvragen and calling the reader to read the contents
 * 
 */
public class AanvraagLoader {

	private List<String> fileNames = new ArrayList<String>();

	public AanvraagLoader() {
		loadAanvraagFileNames();
	}

	//Reads & creates Documents of aanvragen and adds them to the storagemanager
	public void readAllAanvragen() {
		AanvraagReader aanvraagReader = new AanvraagReader();
		String filePath = "resources/Aanvragen/";
		List<Document> aanvraagDocuments = new ArrayList<Document>();
		for(String fileName : fileNames) {
			Document aanvraag = aanvraagReader.readPFDFile(filePath, fileName);
			if(aanvraag != null) {
				aanvraagDocuments.add(aanvraag);
			}
		}
		
		StorageManager storageManager = StorageManager.getInstance();
		storageManager.addDocuments(aanvraagDocuments);

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
