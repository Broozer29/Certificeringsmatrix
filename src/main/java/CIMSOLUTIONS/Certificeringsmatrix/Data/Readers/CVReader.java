package CIMSOLUTIONS.Certificeringsmatrix.Data.Readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.CVStorage;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

public class CVReader {

	private static CVReader instance = new CVReader();

	private CVReader() {

	}

	public static CVReader getInstance() {
		return instance;
	}

	public void readDocxFile(String fileName) throws IOException {
		CVStorage cvStorage = CVStorage.getInstance();
		Document CV = new Document(fileName);

		String filePath = "resources/CV/" + fileName;
		try (FileInputStream fis = new FileInputStream(new File(filePath))) {
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph para : paragraphs) {
				
				
				String text = para.getText();
                String[] sentences = text.split("(?<=[.!?])\\s+");
                for (String sentence : sentences) {
                	String cleanedSentence = sentence.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
                    String[] words = cleanedSentence.split("\\s+|/");
                    
                    //Indents between sentences still exist? I don't know yet if this is a problem
                    CV.addSentenceToDocument(cleanedSentence);
                    
                    for (String word : words) {
                        String cleanedWord = word.replaceAll("[;:\\-()]", "");
                        if (!cleanedWord.isEmpty()) {
                            CV.addWord(cleanedWord);
                        }
                    }
                }
				
				
//				List<XWPFRun> runs = para.getRuns();
//				for (XWPFRun run : runs) {
//					
//					
//					String[] words = run.text().split("\\s+|/");
//					for (String word : words) {
//						String cleanedWord = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
//						if (!cleanedWord.isEmpty()) {
//							CV.addWord(cleanedWord);
//						}
//					}
//				}
				
				
			}
			cvStorage.addCVDocument(CV);
			document.close();
		}

	}
}
