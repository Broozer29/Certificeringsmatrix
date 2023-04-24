package CIMSOLUTIONS.Certificeringsmatrix.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.AanvraagLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.CVLoader;

public class DocumentLoadingDriver {

	private static DocumentLoadingDriver instance = new DocumentLoadingDriver();
	private AanvraagLoader avLoader = AanvraagLoader.getInstance();
	private CVLoader cvLoader = CVLoader.getInstance();

	private DocumentLoadingDriver() {

	}

	public static DocumentLoadingDriver getInstance() {
		return instance;
	}

	public void loadDocuments() {
		avLoader = AanvraagLoader.getInstance();
		avLoader.readAllAanvragen();

		// Load & read all CV's
		cvLoader = CVLoader.getInstance();
		cvLoader.readAllCVs();
	}

}
