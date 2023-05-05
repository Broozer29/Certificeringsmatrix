package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.Sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;

/*- This class is responsible for sorting a given LinkedHashMap
 * 	Credits to Stack Overflow for the implementation of the custom comparator
 */
public class LinkedHashMapSorter {

	private static LinkedHashMapSorter instance = new LinkedHashMapSorter();

	private LinkedHashMapSorter() {

	}

	public static LinkedHashMapSorter getInstance() {
		return instance;
	}

	// Used for sorting LinkedHashMaps of words with scores. Used by multiple components
	public LinkedHashMap<String, Double> sortByValueDescending(LinkedHashMap<String, Double> unsortedMap) {
		List<Map.Entry<String, Double>> list = new ArrayList<>(unsortedMap.entrySet());

		// Create a custom comparator and create the compare method to sort in descending
		// To sort in ascending, simply swap the positions of entry2 and entry1
		list.sort(new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> entry1, Map.Entry<String, Double> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});

		// Create a new LinkedHashMap to store the sorted entries
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	/*- Used for sorting LinkedHashMaps of competences and their scores. Used only for generating the certificeringsmatrix */
	public LinkedHashMap<Competence, Double> sortCompetencesDescending(LinkedHashMap<Competence, Double> unsortedMap) {
		List<Map.Entry<Competence, Double>> list = new ArrayList<>(unsortedMap.entrySet());

		list.sort(new Comparator<Map.Entry<Competence, Double>>() {
			@Override
			public int compare(Map.Entry<Competence, Double> entry1, Map.Entry<Competence, Double> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});

		// Create a new LinkedHashMap to store the sorted entries
		LinkedHashMap<Competence, Double> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<Competence, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;

	}

}
