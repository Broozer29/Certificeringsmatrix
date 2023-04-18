package CIMSOLUTIONS.Certificeringsmatrix.Data.Storage;

import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Document;

public class StorageManager {

	private static StorageManager instance = new StorageManager();
	private AanvraagStorage aanvraagStorage;
	private CVStorage cvStorage;

	private List<Document> allDocuments = new ArrayList<Document>();

	private StorageManager() {
		aanvraagStorage = AanvraagStorage.getInstance();
		cvStorage = CVStorage.getInstance();
	}

	public static StorageManager getInstance() {
		return instance;
	}

	// When a storage has loaded additional files, this function needs to be called
	// again to synchronize the manager with the storages
	public void refreshStorageManager() {
		allDocuments = new ArrayList<Document>();
		aanvraagStorage = AanvraagStorage.getInstance();
		cvStorage = CVStorage.getInstance();

		for (Document docu : CVStorage.getInstance().getAllDocuments()) {
			allDocuments.add(docu);
		}
		for (Document docu : AanvraagStorage.getInstance().getAllAanvragen()) {
			allDocuments.add(docu);
		}
	}
	
	public List<Document> getAllDocuments(){
		return allDocuments;
	}
}
