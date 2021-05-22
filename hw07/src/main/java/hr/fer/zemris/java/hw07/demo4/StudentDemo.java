package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Class {@code StudentDemo} contains ways of filtering, grouping, and printing of 
 * student records from a file representing a database of records.
 * 
 * @author stipe
 *
 */
public class StudentDemo {

	public static void main(String[] args) {
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("studenti.txt"));
		} catch (IOException e) {
			System.out.println("Error while reading file.");
			System.exit(1);
		}
		
		List<StudentRecord> records = convert(lines);
		
		startFilters(records);
		
	}

	
	/**
	 * Performs filter, grouping and other operations using the given records.
	 * 
	 * @param records {@code List} of all student records
	 */
	private static void startFilters(List<StudentRecord> records) {
		long broj = vratibodovaViseOd25(records);
		formatOutput(broj, 1, System.out::println);
		
		
		long broj5 = vratiBrojOdlikasa(records);
		formatOutput(broj5, 2, System.out::println);
		
		
		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		formatOutput(odlikasi, 3, list -> list.forEach(System.out::println));
		
		
		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(records);
		formatOutput(odlikasiSortirano, 4, list -> list.forEach(System.out::println));
		
		
		List<String> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);
		formatOutput(nepolozeniJMBAGovi, 5, list -> list.forEach(System.out::println));
		
		
		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(records);
		formatOutput(mapaPoOcjenama, 6, 
				map -> map.forEach((k,v) -> {
							System.out.println(k + " -> ");							
							v.forEach(System.out::println);
							System.out.println();
						})
		);			
		
		
		Map<Integer, Integer> mapaPoOcjenama2 = vratiBrojStudenataPoOcjenama(records);
		formatOutput(mapaPoOcjenama2, 7, map -> map.forEach((k,v) -> System.out.println(k + " -> " + v)));
		
		
		Map<Boolean, List<StudentRecord>> prolazNeprolaz = razvrstajProlazPad(records);
		formatOutput(prolazNeprolaz, 8, 
				map -> map.forEach((k,v) -> {
							System.out.println(k + " -> ");							
							v.forEach(System.out::println);
							System.out.println();
						})
		);	
		
	}

	/**
	 * Groups students depending on if they passed or failed the subject.
	 * 
	 * @param records {@code List} of all student records
	 * @return {@code Map} where {@code Key} is {@code true} if the student passed, 
	 * and {@code false} if failed, and {@code Value} is a list of students that passed/failed
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream()
			.collect(Collectors.partitioningBy(r -> r.getGrade() != 1));
	}

	/**
	 * Groups students by grade and counts how many have each grade.
	 * 
	 * @param records {@code List} of all student records
	 * @return {@code Map} where {@code Key} is the grade, and {@code Value} 
	 * is the number of students with that grade
	 */
	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream()
			.collect(Collectors.toMap(StudentRecord::getGrade, r -> 1, (oldV,newV) -> oldV + newV));
	}

	/**
	 * Groups students by grade.
	 * 
	 * @param records {@code List} of all student records
	 * @return {@code Map} where {@code Key} is the grade, and {@code Value} 
	 * is the list of students with that grade
	 */
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream()
			.collect(Collectors.groupingBy(StudentRecord::getGrade));
	}

	/**
	 * Creates a list of students that have failed the subject.
	 * 
	 * @param records {@code List} of all student records
	 * @return {@code List} of students that failed the subject
	 */
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream()
			.filter(r -> r.getGrade() == 1)
			.map(r -> r.getJmbag())
			.sorted()
			.collect(Collectors.toList());	
	}

	/**
	 * Creates a sorted list of students that passed the subject with an 'A'.
	 * 
	 * @param records {@code List} of all student records
	 * @return sorted {@code List} of students that had an 'A'
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
			.filter(r -> r.getGrade() == 5)
			.sorted((r1,r2) -> Double.compare(
						(r2.getFirstExamResult() + r2.getSecondExamResult() + r2.getLabResult()),
						(r1.getFirstExamResult() + r1.getSecondExamResult() + r1.getLabResult())))
			.collect(Collectors.toList());
	}

	/**
	 * Creates a list of students that passed the subject with an 'A'.
	 * 
	 * @param records {@code List} of all student records
	 * @return {@code List} of students that had an 'A'
	 */
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
			.filter(r -> r.getGrade() == 5)
			.collect(Collectors.toList());
	}

	/**
	 * Prints out the given object in a way defined by the given consumer.
	 * 
	 * @param result object to be printed
	 * @param i task's numeration
	 * @param consumer object defining the way of printing
	 */
	private static <T> void formatOutput(T result, int i, Consumer<T> consumer) {
		System.out.printf("Zadatak %d%n=========%n", i);
		consumer.accept(result);
		System.out.println();
	}

	/**
	 * Counts how many students have passed the subject with an 'A'.
	 * 
	 * @param records {@code List} of all student records
	 * @return number of students that had an 'A'
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream()
			.filter(r -> r.getGrade() == 5)
			.count();
	}

	/**
	 * Counts how many students have a score greater than 25.
	 * 
	 * @param records {@code List} of all student records
	 * @return number of people with a score greater than 25
	 */
	private static long vratibodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
			.filter(r -> r.getFirstExamResult() + r.getSecondExamResult() + r.getLabResult() > 25)
			.count();
	}

	/**
	 * Converts {@code String} objects from a file to a list of {@code StudentRecord} objects.
	 * 
	 * @param lines String objects representing a line from the file
	 * @return {@code List} of {@code StudentRecord} objects
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> records = new ArrayList<>();
		
		for (String line : lines) {
			if (line.isBlank()) {
				continue;
			}
			String[] data = line.split("\\t");
			try {
				double firstExam = Double.parseDouble(data[3]);
				double secondExam = Double.parseDouble(data[4]);
				double lab = Double.parseDouble(data[5]);
				int grade = Integer.parseInt(data[6]);
				
				if (grade > 5 || grade < 1) {
					throw new RuntimeException("Illegal grade in student records!");
				}
				
				records.add(new StudentRecord(
						data[0], data[1], data[2], firstExam, secondExam, lab, grade));

			} catch (NumberFormatException ex) {
				throw new RuntimeException("Illegal number formats in student records!");
			}
		}
		return records;
	}
	
}
