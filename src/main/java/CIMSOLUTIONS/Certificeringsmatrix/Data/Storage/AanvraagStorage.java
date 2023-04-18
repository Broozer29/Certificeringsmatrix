package CIMSOLUTIONS.Certificeringsmatrix.Data.Storage;

import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Document;

public class AanvraagStorage {
	
	private static AanvraagStorage instance = new AanvraagStorage();
	private List<Document> aanvraagDocuments = new ArrayList<Document>();
	
	private AanvraagStorage() {
		
	}
	
	public static AanvraagStorage getInstance() {
		return instance;
	}
	
	public void addAanvraag(Document aanvraag) {
		aanvraagDocuments.add(aanvraag);
	}
	
	public List<Document> getAllAanvragen(){
		return this.aanvraagDocuments;
	}
	

}
