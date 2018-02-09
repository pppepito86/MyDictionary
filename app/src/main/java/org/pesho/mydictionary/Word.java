package org.pesho.mydictionary;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/*
 * word=
 * 
 * row
 * gender
 * original
 * plural
 * meaning
 * additionalInfo
 * label
 * level
 * correct
 * wrong
 * consecutive correct
 * 
 */
public class Word implements Comparable<Word> {
	
	public enum GENDER {
		DER, DIE, DAS, PL;
		
		public String toString() {
			return name().toLowerCase(Locale.ENGLISH);
		};
	} 
	
	private int row;
	private GENDER gender;
	private String original;
	private String modified;
	private String plural;
	private String meaning;
	private String additionalInfo;
	private int level;
	private int correct;
	private int wrong;
	private int consecutiveCorrect;
	private long totalTime;
	private long lastTestetTime;
	private int correct2;
	private int wrong2;
	private int consecutiveCorrect2;
	private long totalTime2;
	private long lastTestetTime2;
	private long lastTimeLevelIncreased;
	private List<String> labels = new LinkedList<String>();
	
	public Word(String word, String meaning) {
		this.original = getString(word);
		this.modified = getModified(getString(word));
		this.meaning = getString(meaning);
	}
	
	public Word(String gender, String word, String meaning, String label) {
		this(word, meaning);
		setGender(getString(gender));
		setLabel(getString(label));
	}
	
	public Word(int row, String... array) {
		this.row = row;
		setGender(getString(array[0]));
		this.original = getString(array[1]);
		this.modified = getModified(getString(array[1]));
		this.plural = getString(array[2]);
		this.meaning = getString(array[3]);
		if (array.length > 4) {
			this.additionalInfo = getString(array[4]);
		}
		if (array.length > 5) {
			setLabel(getString(array[5]));
		}
		if (array.length > 6) {
			this.level = getInt(array[6]);
		}
		if (array.length > 7) {
			this.correct = getInt(array[7]);
		}
		if (array.length > 8) {
			this.wrong = getInt(array[8]);
		}
		if (array.length > 9) {
			this.consecutiveCorrect = getInt(array[9]);
		}
		if (array.length > 10) {
			this.totalTime = getLong(array[10]);
		}
		if (array.length > 11) {
			this.lastTestetTime = getLong(array[11]);
		}
		if (array.length > 12) {
			this.correct2 = getInt(array[12]);
		}
		if (array.length > 13) {
			this.wrong2 = getInt(array[13]);
		}
		if (array.length > 14) {
			this.consecutiveCorrect2 = getInt(array[14]);
		}
		if (array.length > 15) {
			this.totalTime2 = getLong(array[15]);
		}
		if (array.length > 16) {
			this.lastTestetTime2 = getLong(array[16]);
		}
		if (array.length > 17) {
			this.lastTimeLevelIncreased = getLong(array[17]);
		}
	}
	
	private String getString(String s) {
		if (s == null) {
			return "";
		}
		return s.trim();
	}
	
	private int getInt(String s) {
		if (s == null) {
			return 0;
		}
		s = s.trim();
		if (s.length() == 0) {
			return 0;
		}
		return Integer.valueOf(s);
	}
	
	private long getLong(String s) {
		if (s == null) {
			return 0;
		}
		s = s.trim();
		if (s.length() == 0) {
			return 0;
		}
		return Long.valueOf(s);
	}

	private void setGender(String gender) {
		gender = gender.toLowerCase(Locale.ENGLISH);
		if (gender.equals("der")) {
			this.gender = GENDER.DER;
		} else if (gender.equals("die")) {
			this.gender = GENDER.DIE;
		} else if (gender.equals("das")) {
			this.gender = GENDER.DAS;
		} else if (gender.equals("pl") || gender.equals("pl.") || gender.equals("plural")) {
			this.gender = GENDER.PL;
		}
	}
	
	private void setLabel(String label) {
		StringTokenizer st = new StringTokenizer(label, ",");
		while (st.hasMoreTokens()) {
			String nextToken = st.nextToken();
			nextToken = nextToken.trim();
			if (nextToken.length() > 0) {
				labels.add(nextToken);
			}
		}
	}

	private String getModified(String word) {
		word = word.toLowerCase(Locale.ENGLISH);
		word = word.replace("�", "a");
		word = word.replace("�", "o");
		word = word.replace("�", "u");
		word = word.replace("�", "ss");
		return word;
	}
	
	public String getModified() {
		return modified;
	}
	
	public String getWord() {
		return original;
	}
	
	public String getMeaning() {
		return meaning;
	}
	
	@Override
	public int hashCode() {
		return modified.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Word)) {
			return false;
		}

		Word other = (Word) o;
		return modified.equals(other.modified);
	}
	
	@Override
	public int compareTo(Word another) {
		if (modified.equals(another.modified)) {
			return original.compareTo(another.original);
		}
		return modified.compareTo(another.modified);
	}
	
	@Override
	public String toString() {
		return original;
	}

	public int getRow() {
		return row;
	}

	public GENDER getGender() {
		return gender;
	}

	public String getOriginal() {
		return original;
	}

	public String getPlural() {
		return plural;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public int getLevel() {
		return level;
	}

	public int getCorrect() {
		return correct;
	}

	public int getWrong() {
		return wrong;
	}

	public int getConsecutiveCorrect() {
		return consecutiveCorrect;
	}
	
	public long getTotalTime() {
		return totalTime;
	}

	public int getCorrect2() {
		return correct2;
	}
	
	public int getWrong2() {
		return wrong2;
	}
	
	public int getConsecutiveCorrect2() {
		return consecutiveCorrect2;
	}
	
	public long getTotalTime2() {
		return totalTime2;
	}

	public List<String> getLabels() {
		return labels;
	}
	
	public String getLabelsAsString() {
		if (labels.size() == 0) {
			return "";
		}
		
		String result = labels.get(0);
		for (int i = 1; i < labels.size(); i++) {
			result += ", " + labels.get(i);
		}
		
		return result;
	}
	
	public int increaseLevel() {
		consecutiveCorrect = 0;
		consecutiveCorrect2 = 0;
		lastTimeLevelIncreased = System.currentTimeMillis();
		return ++level;
	}
	
	public int increaseCorrect(long time) {
		correct++;
		totalTime += time;
		if (consecutiveCorrect > 0) {
			consecutiveCorrect++;
		} else {
			consecutiveCorrect = 1;
		}
		return correct;
	}
	
	public int increaseWrong() {
		wrong++;
		if (consecutiveCorrect > 0) {
			consecutiveCorrect = -1;
		} else {
			consecutiveCorrect--;
		}
		return wrong;
	}

	public int increaseCorrect2(long time) {
		correct2++;
		totalTime2 += time;
		if (consecutiveCorrect2 > 0) {
			consecutiveCorrect2++;
		} else {
			consecutiveCorrect2 = 1;
		}
		return correct2;
	}
	
	public int increaseWrong2() {
		wrong2++;
		if (consecutiveCorrect2 > 0) {
			consecutiveCorrect2 = -1;
		} else {
			consecutiveCorrect2--;
		}
		return wrong2;
	}
	
	public long getLastTestetTime() {
		return lastTestetTime;
	}

	public void setLastTestetTime(long lastTestetTime) {
		this.lastTestetTime = lastTestetTime;
	}

	public long getLastTestetTime2() {
		return lastTestetTime2;
	}

	public void setLastTestetTime2(long lastTestetTime2) {
		this.lastTestetTime2 = lastTestetTime2;
	}

	public long getLastTimeLevelIncreased() {
		return lastTimeLevelIncreased;
	}
	
	public void setLastTimeLevelIncreased(long lastTimeLevelIncreased) {
		this.lastTimeLevelIncreased = lastTimeLevelIncreased;
	}

}
