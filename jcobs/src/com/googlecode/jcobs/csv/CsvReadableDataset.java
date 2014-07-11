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
 * TODO Support files without Header.
 * TODO Support partial fetch.
 * TODO Support more field types.
 * TODO recordCount() method.
 * 
 * @author Samuel Y. Deschamps
 *
 */
public class CsvReadableDataset {

	public static final char DEFAULT_SEPARATOR = ';';
	public static final char DEFAULT_DELIMITER = '"';
	public static final char DEFAULT_DELIMITER_ESCAPE = '"';
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	protected char separator = DEFAULT_SEPARATOR;
	protected char delimiter = DEFAULT_DELIMITER;
	protected char delimiterEscape = DEFAULT_DELIMITER_ESCAPE;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

	protected Map<String, Integer> fields = new LinkedHashMap<>();
	protected List<String[]> records = new ArrayList<>();
	protected int currentIndex = -1;

	public CsvReadableDataset() {
	}

	public void clear() {
		fields.clear();
		records.clear();
		currentIndex = -1;
	}

	public void loadFromFile(File file) throws IOException {
		clear();
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			String line = bufReader.readLine();
			if (line == null) {
				return;
			}
			loadHeader(line);
			while ((line = bufReader.readLine()) != null) {
				loadRecord(line);
			}
		} finally {
			bufReader.close();
			reader.close();
		}
	}

	private void loadHeader(String line) {
		List<String> fieldNames = readTokens(line);
		for (String name : fieldNames) {
			if (name.isEmpty()) {
				throw new CsvFormatError("Empty fieldname!");
			}
			fields.put(name.toUpperCase(), fields.size());
		}
	}

	private void loadRecord(String line) {
		List<String> fieldValues = readTokens(line);
		String[] record = new String[fields.size()];
		for (int i = 0; i < fieldValues.size(); ++i) {
			String string = fieldValues.get(i);
			record[i] = string.isEmpty() ? null : string;
		}
		records.add(record);
	}

	private List<String> readTokens(String line) {
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
		fieldName = fieldName.toUpperCase();
		Integer fieldIndex = fields.get(fieldName);
		if (fieldIndex == null) {
			throw new CsvFormatError("Field not found: '" + fieldName + "'.");
		}
		return fieldIndex;
	}

	public String getString(String fieldName) {
		return getAsType(fieldName, FieldType.STRING);
	}

	public String getString(int fieldIndex) {
		return getAsType(fieldIndex, FieldType.STRING);
	}

	public Integer getInteger(String fieldName) {
		return getAsType(fieldName, FieldType.INTEGER);
	}

	public Integer getInteger(int fieldIndex) {
		return getAsType(fieldIndex, FieldType.INTEGER);
	}

	public Long getLong(String fieldName) {
		return getAsType(fieldName, FieldType.LONG);
	}

	public Long getLong(int fieldIndex) {
		return getAsType(fieldIndex, FieldType.LONG);
	}

	public Date getDate(String fieldName) {
		return getAsType(fieldName, FieldType.UTIL_DATE);
	}

	public Date getDate(int fieldIndex) {
		return getAsType(fieldIndex, FieldType.UTIL_DATE);
	}
	
	public int fieldCount() {
		return fields.size();
	}

	protected <T> T getAsType(String fieldName, FieldType fieldType) {
		int fieldIndex = getFieldIndex(fieldName);
		return getAsType(fieldIndex, fieldType);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAsType(int fieldIndex, FieldType fieldType) {
		String value = records.get(currentIndex)[fieldIndex];
		return (T) convertValue(value, fieldType);
	}

	public boolean isNull(String fieldName) {
		return getString(fieldName) == null;
	}

	public boolean next() {
		if (currentIndex < records.size() - 1) {
			++currentIndex;
			return true;
		}
		return false;
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

	@SuppressWarnings("rawtypes")
	protected Comparable convertValue(String value, FieldType type) {
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
