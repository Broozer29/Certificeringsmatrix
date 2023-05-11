package CIMSOLUTIONS.Certificeringsmatrix.Data.Readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

public class CVReader {

	public CVReader() {

	}

	public Document readDocxFile(String fileName) throws IOException {
		Document CV = null;
		String filePath = "resources/CV/" + fileName;
		try (FileInputStream fis = new FileInputStream(new File(filePath))) {
			CV = new Document(fileName);
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph para : paragraphs) {

				String text = para.getText();
				String[] sentences = text.split("(?<=[.!?])\\s+");
				for (String sentence : sentences) {
					String cleanedSentence = sentence.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
					String[] words = cleanedSentence.split("\\s+|/");

					// Indents between sentences still exist? I don't know yet if this is a problem
					CV.addSentenceToDocument(cleanedSentence);

					for (String word : words) {
						String cleanedWord = word.replaceAll("[;:\\-()]", "");
						if (!cleanedWord.isEmpty()) {
							CV.addWord(cleanedWord);
						}
					}
				}

			}
			document.close();
		}
		return CV;

	}
}
