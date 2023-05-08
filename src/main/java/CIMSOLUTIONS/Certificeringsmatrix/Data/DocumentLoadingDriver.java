package CIMSOLUTIONS.Certificeringsmatrix.Data;


import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.AanvraagLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.CVLoader;

/*- This class is responsible for calling the load & read methods of the Document loaders & readers
 */
public class DocumentLoadingDriver {

	private static DocumentLoadingDriver instance = new DocumentLoadingDriver();
	private AanvraagLoader avLoader = null;
	private CVLoader cvLoader = null;

	private DocumentLoadingDriver() {

	}

	public static DocumentLoadingDriver getInstance() {
		return instance;
	}

	public void loadDocuments() {
		// Load & read all aanvragen
		avLoader = new AanvraagLoader();
		avLoader.readAllAanvragen();

		// Load & read all CV's
		cvLoader = new CVLoader();
		cvLoader.readAllCVs();
	}
	
}
