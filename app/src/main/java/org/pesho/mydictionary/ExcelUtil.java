package org.pesho.mydictionary;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;

public class ExcelUtil {

	static TreeMap<String, Word> map = new TreeMap<String, Word>();

	static {
		load();
	}

	public static synchronized void load() {
		File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!new File(directory, "Test.xls").exists()) {
			WritableWorkbook newFile = null;
			try {
				newFile = Workbook.createWorkbook(new File(directory, "Test.xls"));
				newFile.createSheet("german", 0);
				newFile.write();
			} catch (IOException e) {
			} finally {
				if (newFile != null) {
					try {
						newFile.close();
					} catch (Exception e) {
					}
				}
			}

			return;
		}

		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setEncoding("Cp1252");

		Workbook workBook = null;
		map = new TreeMap<String, Word>();
		try {
			// workBook = Workbook.getWorkbook(new
			// File("/storage/sdcard0/bluetooth/Test.xls"), workbookSettings);
			workBook = Workbook.getWorkbook(new File(directory, "Test.xls"), workbookSettings);
			Sheet sheet = workBook.getSheet(0);
			for (int i = 0; i < sheet.getRows(); i++) {
				Cell[] row = sheet.getRow(i);
				String[] rowString = new String[row.length];
				for (int j = 0; j < row.length; j++) {
					rowString[j] = row[j].getContents();
				}
				if (rowString.length > 3) {
					if (rowString[1].trim().equals("") || rowString[3].trim().equals("")) {
						continue;
					}
					Word word = new Word(i, rowString);
					map.put(word.getWord(), word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workBook != null) {
				workBook.close();
			}
		}
	}

	public static Word getWord(String word) {
		return map.get(word);
	}

	public static SortedSet<Word> getWords(String startsWith) throws Exception {
		if (startsWith == null) {
			startsWith = "";
		}
		SortedSet<Word> set = new TreeSet<Word>();

		for (Word word : map.values()) {
			if (word.getModified().startsWith(startsWith)) {
				set.add(word);
			}
		}

		return set;
	}

	public static SortedSet<Word> getWordsByLabel(String label, String startsWith) throws Exception {
		if (label == null || label.trim().length() == 0) {
			return getWords(startsWith);
		}
		if (startsWith == null) {
			startsWith = "";
		}
		String[] labels = label.split(",");
		for (int i = 0; i < labels.length; i++) {
			labels[i] = labels[i].trim();
		}

		SortedSet<Word> set = new TreeSet<Word>();

		for (Word word : map.values()) {
			if (word.getModified().startsWith(startsWith)) {
				for (String l : labels) {
					if (word.getLabels().contains(l)) {
						set.add(word);
						break;
					}
				}
			}
		}

		return set;
	}

	static int counter = 0;

	public static synchronized void add(String word, String meaning, String label) throws Exception {
		if (word == null || word.trim().equals("") || meaning == null || meaning.trim().equals("")) {
			return;
		}
		String[] wordSplit = word.split(" ");
		if (wordSplit.length > 1) {

			boolean isCorrectGender = false;
			for (Word.GENDER gender : Word.GENDER.values()) {
				if (wordSplit[0].equalsIgnoreCase(gender.name())) {
					isCorrectGender = true;
				}
			}

			if (!isCorrectGender) {
				return;
			}

			word = wordSplit[1].trim();
		}

		ExcelEdit edit = new ExcelEdit();
		WritableSheet sheet0 = edit.getSheet();

		int newRowIndex = sheet0.getRows();

		while (newRowIndex > 0) {
			Cell cell = sheet0.getCell(1, newRowIndex - 1);
			if (cell == null || cell.getContents().trim().equals("")) {
				newRowIndex--;
			} else {
				break;
			}
		}

		if (!map.containsKey(word)) {
			sheet0.addCell(new Label(1, newRowIndex, word));
			sheet0.addCell(new Label(3, newRowIndex, meaning));
			sheet0.addCell(new Label(5, newRowIndex, label));

			if (wordSplit.length > 1) {
				sheet0.addCell(new Label(0, newRowIndex, wordSplit[0].trim()));
			}
			if (wordSplit.length > 2) {
				sheet0.addCell(new Label(2, newRowIndex, wordSplit[2].trim()));
			}
		}

		edit.writeAndClose();
	}

	public static void copy() throws Exception {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setEncoding("Cp1252");
		try {
			File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			workbook = Workbook.getWorkbook(new File(directory, "Test.xls"), workbookSettings);
			new File(directory, "TestCopy.xls").delete();
			copy = Workbook.createWorkbook(new File(directory, "TestCopy.xls"), workbook, workbookSettings);
			copy.write();
		} finally {
			if (copy != null) {
				copy.close();
			}
			if (workbook != null) {
				workbook.close();
			}
		}
	}

	public static synchronized void writeTestResult(Word word, boolean result, long time) throws Exception {
		ExcelEdit edit = new ExcelEdit();
		WritableSheet sheet0 = edit.getSheet();

		if (result) {
			sheet0.addCell(new Label(7, word.getRow(), String.valueOf(word.increaseCorrect(time))));
			sheet0.addCell(new Label(10, word.getRow(), String.valueOf(word.getTotalTime())));
		} else {
			sheet0.addCell(new Label(8, word.getRow(), String.valueOf(word.increaseWrong())));
		}
		sheet0.addCell(new Label(9, word.getRow(), String.valueOf(word.getConsecutiveCorrect())));
		edit.writeAndClose();
	}

	public static synchronized void writeWordStats(Word word) throws Exception {
		ExcelEdit edit = null;
		try {
			edit = new ExcelEdit();
			WritableSheet sheet0 = edit.getSheet();

			sheet0.addCell(new Number(6, word.getRow(), word.getLevel()));
			sheet0.addCell(new Number(7, word.getRow(), word.getCorrect()));
			sheet0.addCell(new Number(8, word.getRow(), word.getWrong()));
			sheet0.addCell(new Number(9, word.getRow(), word.getConsecutiveCorrect()));
			sheet0.addCell(new Number(10, word.getRow(), word.getTotalTime()));
			sheet0.addCell(new Number(11, word.getRow(), word.getLastTestetTime()));

			sheet0.addCell(new Number(12, word.getRow(), word.getCorrect2()));
			sheet0.addCell(new Number(13, word.getRow(), word.getWrong2()));
			sheet0.addCell(new Number(14, word.getRow(), word.getConsecutiveCorrect2()));
			sheet0.addCell(new Number(15, word.getRow(), word.getTotalTime2()));
			sheet0.addCell(new Number(16, word.getRow(), word.getLastTestetTime2()));

			sheet0.addCell(new Number(17, word.getRow(), word.getLastTimeLevelIncreased()));

			edit.write();
		} finally {
			if (edit != null) {
				edit.close();
			}
		}
	}

	public static synchronized void removeWord(String wordString) throws Exception {
		Word word = ExcelUtil.getWord(wordString);
		ExcelEdit edit = new ExcelEdit();
		WritableSheet sheet = edit.getSheet();
		sheet.removeRow(word.getRow());
		edit.writeAndClose();
	}

	public static synchronized void edit(String word, String meaning, String label) throws Exception {
		if (word == null || word.trim().equals("") || meaning == null || meaning.trim().equals("")) {
			return;
		}
		String[] wordSplit = word.split(" ");
		if (wordSplit.length > 1) {

			boolean isCorrectGender = false;
			for (Word.GENDER gender : Word.GENDER.values()) {
				if (wordSplit[0].equalsIgnoreCase(gender.name())) {
					isCorrectGender = true;
				}
			}

			if (!isCorrectGender) {
				return;
			}

			word = wordSplit[1].trim();
		}

		ExcelEdit edit = new ExcelEdit();
		WritableSheet sheet = edit.getSheet();

		Word w = map.get(word);
		if (w != null) {
			int rowIndex = w.getRow();
			sheet.addCell(new Label(1, rowIndex, word));
			sheet.addCell(new Label(3, rowIndex, meaning));
			sheet.addCell(new Label(5, rowIndex, label));

			if (wordSplit.length > 1) {
				sheet.addCell(new Label(0, rowIndex, wordSplit[0].trim()));
			}
			if (wordSplit.length > 2) {
				sheet.addCell(new Label(2, rowIndex, wordSplit[2].trim()));
			}
		}

		edit.writeAndClose();
	}

}
