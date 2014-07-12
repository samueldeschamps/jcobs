package com.googlecode.jcobs.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO Support partial fetch (CsvBigFileReader?).
 * TODO Support more field types.
 * 
 * @author Samuel Y. Deschamps
 *
 */
public class CsvReader {

	public static final char DEFAULT_SEPARATOR = ';';
	public static final char DEFAULT_DELIMITER = '"';
	public static final char DEFAULT_DELIMITER_ESCAPE = '"';
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	protected char separator = DEFAULT_SEPARATOR;
	protected char delimiter = DEFAULT_DELIMITER;
	protected char delimiterEscape = DEFAULT_DELIMITER_ESCAPE;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	protected boolean firstLineIsHeader = true;

	protected Map<String, Integer> fields = new LinkedHashMap<>();
	protected int fieldCount = -1;
	protected List<String[]> records = new ArrayList<>();
	protected int currentIndex = -1;

	public CsvReader() {
	}

	public void clear() {
		fields.clear();
		records.clear();
		fieldCount = -1;
		currentIndex = -1;
	}

	public void readFile(File file) throws IOException {
		clear();
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			String line = bufReader.readLine();
			if (line == null) {
				return;
			}
			if (firstLineIsHeader) {
				readHeader(line);
			}
			while ((line = bufReader.readLine()) != null) {
				readRecord(line);
			}
		} finally {
			bufReader.close();
			reader.close();
		}
	}

	private void readHeader(String line) {
		List<String> fieldNames = readFragments(line);
		for (String name : fieldNames) {
			if (name.isEmpty()) {
				throw new CsvFormatError("Empty fieldname!");
			}
			fields.put(name.toUpperCase(), fields.size());
		}
		fieldCount = fields.size();
	}

	private void readRecord(String line) {
		List<String> fieldValues = readFragments(line);
		if (fieldCount == -1) {
			fieldCount = fieldValues.size();
		} else {
			if (fieldValues.size() != fieldCount) {
				throw new CsvFormatError(String.format("Record with %d fragments. Expecting %d.", fieldValues.size(), fieldCount));
			}
		}
		String[] record = new String[fieldCount];
		for (int i = 0; i < fieldCount; ++i) {
			String fragment = fieldValues.get(i);
			record[i] = fragment.isEmpty() ? null : fragment;
		}
		records.add(record);
	}

	private List<String> readFragments(String line) {
		List<String> result = new ArrayList<>();
		StringBuilder value = new StringBuilder();
		boolean insideStr = false;
		for (int i = 0; i < line.length(); ++i) {
			char c = line.charAt(i);
			if (insideStr && c == delimiterEscape && i + 1 < line.length() && line.charAt(i + 1) == delimiter) {
				value.append(delimiter);
				++i;
				continue;
			}
			if (c == delimiter) {
				insideStr = !insideStr;
				continue;
			}
			if (c == separator && !insideStr) {
				result.add(value.toString());
				value = new StringBuilder();
				continue;
			}
			value.append(c);
		}
		result.add(value.toString());
		return result;
	}

	protected int getFieldIndex(String fieldName) {
		assert firstLineIsHeader : "For CSV files without header, values must be read by index.";
		fieldName = fieldName.toUpperCase();
		Integer fieldIndex = fields.get(fieldName);
		if (fieldIndex == null) {
			throw new CsvFormatError("Field not found: '" + fieldName + "'.");
		}
		return fieldIndex;
	}

	public String getString(String fieldName) {
		return getAsType(fieldName, CsvFieldType.STRING);
	}

	public String getString(int fieldIndex) {
		return getAsType(fieldIndex, CsvFieldType.STRING);
	}

	public Integer getInteger(String fieldName) {
		return getAsType(fieldName, CsvFieldType.INTEGER);
	}

	public Integer getInteger(int fieldIndex) {
		return getAsType(fieldIndex, CsvFieldType.INTEGER);
	}

	public Long getLong(String fieldName) {
		return getAsType(fieldName, CsvFieldType.LONG);
	}

	public Long getLong(int fieldIndex) {
		return getAsType(fieldIndex, CsvFieldType.LONG);
	}

	public Date getDate(String fieldName) {
		return getAsType(fieldName, CsvFieldType.UTIL_DATE);
	}

	public Date getDate(int fieldIndex) {
		return getAsType(fieldIndex, CsvFieldType.UTIL_DATE);
	}

	public int fieldCount() {
		return fields.size();
	}

	protected <T> T getAsType(String fieldName, CsvFieldType fieldType) {
		int fieldIndex = getFieldIndex(fieldName);
		return getAsType(fieldIndex, fieldType);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAsType(int fieldIndex, CsvFieldType fieldType) {
		if (fieldIndex >= fieldCount) {
			throw new IllegalArgumentException(String.format("Invalid field index: %d. Max is %s.", fieldIndex, fieldCount - 1));
		}
		String value = records.get(currentIndex)[fieldIndex];
		return (T) convertValue(value, fieldType);
	}

	public boolean isNull(String fieldName) {
		return getString(fieldName) == null;
	}

	public boolean isNull(int fieldIndex) {
		return getString(fieldIndex) == null;
	}

	public boolean next() {
		if (currentIndex < records.size() - 1) {
			++currentIndex;
			return true;
		}
		return false;
	}

	public int recordCount() {
		return records.size();
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	public char getDelimiterEscape() {
		return delimiterEscape;
	}

	public void setDelimiterEscape(char delimiterEscape) {
		this.delimiterEscape = delimiterEscape;
	}

	public void setDateFormat(String format) {
		dateFormat = new SimpleDateFormat(format);
	}

	public boolean isFirstLineHeader() {
		return firstLineIsHeader;
	}

	public void setFirstLineIsHeader(boolean firstLineIsHeader) {
		this.firstLineIsHeader = firstLineIsHeader;
	}

	@SuppressWarnings("rawtypes")
	protected Comparable convertValue(String value, CsvFieldType type) {
		if (value == null) {
			return null;
		}
		switch (type) {
		case STRING:
			return value;
		case INTEGER:
			return Integer.valueOf(value);
		case LONG:
			return Long.valueOf(value);
		case UTIL_DATE:
			try {
				return dateFormat.parse(value);
			} catch (ParseException e) {
				throw new CsvFormatError(e);
			}
		default:
			throw new CsvFormatError("Not supported field type: '" + type + "'.");
		}
	}

}
