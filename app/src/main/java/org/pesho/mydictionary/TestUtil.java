package org.pesho.mydictionary;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

public class TestUtil {

	private static List<Word> list = new LinkedList<Word>();
	private static int index;
	private static String label;

	static int[] fibs = new int[15];
	static {
		fibs[0] = 0;
		fibs[1] = 1;
		for (int i = 2; i < fibs.length; i++) {
			fibs[i] = fibs[i-1] + fibs[i-2];
		}
	}
	
	public static boolean isEligible(Word word) {
		int level = word.getLevel();
		if (level > 14) {
			level = 14;
		}
		return System.currentTimeMillis() > word.getLastTimeLevelIncreased() + fibs[level] * 60 * 60 * 1000 &&
				System.currentTimeMillis() > word.getLastTestetTime() + 60 * 1000 &&
				System.currentTimeMillis() > word.getLastTestetTime2() + 60 * 1000;
	}
	
	public static void prepareTest(String label) {
		try {
			TestUtil.label = label;
			SortedSet<Word> words = ExcelUtil.getWordsByLabel(label, "");
			List<Word> list0 = new LinkedList<Word>();
			List<Word> listx1 = new LinkedList<Word>();
			List<Word> listx2 = new LinkedList<Word>();
			List<Word> listx3 = new LinkedList<Word>();
			List<Word> listx4 = new LinkedList<Word>();
			List<Word> listx5 = new LinkedList<Word>();
			List<Word> list1 = new LinkedList<Word>();
			List<Word> list6 = new LinkedList<Word>();
			List<Word> list11 = new LinkedList<Word>();
			for (Word word : words) {
				if (word.getLevel() == 0) {
					list0.add(word);
				} else if (word.getLevel() < 6 && isEligible(word)) {
					list1.add(word);
					if (word.getLevel() == 1) {
						listx1.add(word);
					}
					if (word.getLevel() == 2) {
						listx2.add(word);
					}
					if (word.getLevel() == 3) {
						listx3.add(word);
					}
					if (word.getLevel() == 4) {
						listx4.add(word);
					}
					if (word.getLevel() == 5) {
						listx1.add(word);
					}
				} else if (word.getLevel() < 11 && isEligible(word)) {
					list6.add(word);
				} else if (isEligible(word)){
					list11.add(word);
				}
			}
			Collections.shuffle(list0);
			Collections.shuffle(list1);
			Collections.shuffle(list6);
			Collections.shuffle(list11);
			Collections.shuffle(listx1);
			Collections.shuffle(listx2);
			Collections.shuffle(listx3);
			Collections.shuffle(listx4);
			Collections.shuffle(listx5);
			
			list = new LinkedList<Word>();
			addToList(list0, 7);
			addToList(list1, 5);
			addToList(list6, 2);
			addToList(list11, 1);
			
			if (list.size() < 15) {
				addToList(list0, 15 - list.size());
			}
			if (list.size() < 15) {
				addToList(listx1, 15 - list.size());
			}
			if (list.size() < 15) {
				addToList(listx2, 15 - list.size());
			}
			if (list.size() < 15) {
				addToList(listx3, 15 - list.size());
			}
			if (list.size() < 15) {
				addToList(listx4, 15 - list.size());
			}
			if (list.size() < 15) {
				addToList(listx5, 15 - list.size());
			}
			if (list.size() < 15) {
				addToList(list6, 15 - list.size());
			}
			if (list.size() < 15) {
				addToList(list11, 15 - list.size());
			}
			Collections.shuffle(list);
			
//			Collections.sort(list, new Comparator<Word>() {
//				public int compare(Word word1, Word word2) {
//					if (word1.getLevel() != word2.getLevel()) {
//						return Integer.valueOf(word1.getLevel()).compareTo(word2.getLevel());
//					}
//					return word1.compareTo(word2);
//				};
//				
//			});
//			Collections.shuffle(list);
//			for (int i = 10; i < list.size(); i++) {
//				list.remove(i);
//			}
			index = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addToList(List<Word> l, int count) {
		for (int i = 0; i < l.size() && i < count; i++) {
			list.add(l.remove(0));
		}
	}

	public static boolean isFinished() {
		return list.size() == index;
	}
	
	public static Word getCurrentWord() {
		return list.get(index);
	}
	
	public static int increaseIndex() {
		return ++index;
	}
	
	public static boolean isFirstTest() {
		Word word = getCurrentWord();
		return word.getConsecutiveCorrect() >= word.getConsecutiveCorrect2();
	}
	
	public static String testText() {
		Word word = getCurrentWord();
		if (word.getConsecutiveCorrect() < word.getConsecutiveCorrect2()) {
			return word.getMeaning();
		} else {
			return word.getWord();
		}
	}
	
	public static String checkText() {
		Word word = getCurrentWord();
		if (word.getConsecutiveCorrect() < word.getConsecutiveCorrect2()) {
			String text = word.getWord();
			if (word.getGender() != null) {
				text = word.getGender() + " " + text;
				if (word.getPlural() != null && word.getPlural().length() > 0) {
					text += ", " + word.getPlural();
				}
			}
			return text;
		} else {
			if (word.getGender() != null) {
				String text = word.getGender().toString();
				if (word.getPlural() != null && word.getPlural().length() > 0) {
					text += ", " + word.getPlural();
				}
				text += "\n\n" + word.getMeaning();
				return text;
			} else {
				return word.getMeaning();
			}
		}
	}
	
	public static String additionalText() {
		Word word = getCurrentWord();
		return word.getAdditionalInfo();
	}
	
	public static void updateWord(Word word, boolean isCorrect, long time) {
		if (word.getConsecutiveCorrect() < word.getConsecutiveCorrect2()) {
			if (isCorrect) {
				word.increaseCorrect(time);
			} else {
				word.increaseWrong();
			}
			word.setLastTestetTime(System.currentTimeMillis());
		} else {
			if (isCorrect) {
				word.increaseCorrect2(time);
			} else {
				word.increaseWrong2();
			}
			word.setLastTestetTime2(System.currentTimeMillis());
		}
		if (word.getConsecutiveCorrect() >= 3 && word.getConsecutiveCorrect2() >= 3) {
			word.increaseLevel();
		}
	}
	
	public static String getLabel() {
		return label;
	}
}
