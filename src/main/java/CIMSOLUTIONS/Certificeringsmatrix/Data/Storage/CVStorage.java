package CIMSOLUTIONS.Certificeringsmatrix.Data.Storage;

import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

/*- This class holds all Documents of the "CV" type
 * 
 */
public class CVStorage {

	private static CVStorage instance = new CVStorage();
	private List<Document> CVDocuments = new ArrayList<Document>();
	
	private CVStorage() {
		
	}
	
	public static CVStorage getInstance() {
		return instance;
	}
	
	public void addCVDocument(Document cv) {
		this.CVDocuments.add(cv);
	}
	
	public List<Document> getAllDocuments(){
		return this.CVDocuments;
	}
}
