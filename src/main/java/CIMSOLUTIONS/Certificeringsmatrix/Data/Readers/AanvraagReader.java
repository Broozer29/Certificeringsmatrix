package CIMSOLUTIONS.Certificeringsmatrix.Data.Readers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.AanvraagStorage;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

public class AanvraagReader {

	public AanvraagReader() {

	}

	/*-
	 * Recieves a file name and file path, then reads the file with a custom PDFTextStripper anonymous subclass that filters
	 * the text whilst reading it. It saves sentences and individual words to a new document whilst removing all non-alphabetic characters
	 * and whitespace
	 */
	public void readPFDFile(String filePath, String fileName) {
		AanvraagStorage aanvragenStorage = AanvraagStorage.getInstance();
		File file = new File(filePath + fileName);
		try (PDDocument document = Loader.loadPDF(file)) {
			Document aanvraagDocument = new Document(fileName);
			PDFTextStripper stripper = new PDFTextStripper() {

				// Custom sentence string belonging to PDFTextStripper
				String sentence = "";

				protected void writeString(String text, List<TextPosition> textPositionssentence) {
					sentence += text;
					/*- Whilst reading the PDF, the stripper adds the text to "sentence" until it reaches a ./?/!/; character 
					 * Then clean the sentence of non-alphabetic characters & whitespace at the beginning and end of the sentence
					 * */

					if (text.endsWith(".") || text.endsWith("?") || text.endsWith("!") || text.endsWith(";")) {
						String sentenceText = sentence.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();

						/*- If the sentence isn't empty, add it to the newly created document.
						 * 	Then add the individual words to the document as well.
						 */
						if (!sentenceText.isEmpty()) {
							aanvraagDocument.addSentenceToDocument(sentenceText);

							String[] words = sentenceText.split("\\s+");
							for (String word : words) {
								if (!word.isEmpty()) {
									aanvraagDocument.addWord(word);
								}
							}
						}
						// When a sentence is fully processed, reset sentence and start with the next one
						sentence = "";

					}
				}
			};

			// Instructions for the PFDTextStripper to read all pages of the PDF
			stripper.setSortByPosition(true);
			for (int page = 1; page <= document.getNumberOfPages(); page++) {
				stripper.setStartPage(page);
				stripper.setEndPage(page);
				stripper.getText(document);
			}

			// When finished reading a PDF, add the newly created Document to the storage
			aanvragenStorage.addAanvraag(aanvraagDocument);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
