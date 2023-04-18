package CIMSOLUTIONS.Certificeringsmatrix.Data;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.AanvraagLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.CVLoader;

public class DocumentLoadingDriver {
	
	private static DocumentLoadingDriver instance = new DocumentLoadingDriver();
	
	private DocumentLoadingDriver() {
		
	}
	
	public static DocumentLoadingDriver getInstance() {
		return instance;
	}
	
	public void loadDocuments() {
    	AanvraagLoader avLoader = AanvraagLoader.getInstance();
    	avLoader.readAllAanvragen();

		// Load & read all CV's
		CVLoader cvLoader = CVLoader.getInstance();
		cvLoader.readAllCVs();
	}

}
