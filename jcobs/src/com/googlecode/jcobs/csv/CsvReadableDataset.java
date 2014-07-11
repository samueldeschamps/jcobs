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

public class CsvReadableDataset {

	public static final String DEFAULT_SEPARATOR = ";";
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	protected String separator = DEFAULT_SEPARATOR;
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

	private void loadRecord(String line) {
		// TODO Tratar registro com campos excedentes
		String[] fieldValues = line.split(separator);
		String[] record = new String[fields.size()];
		for (int i = 0; i < fieldValues.length; ++i) {
			record[i] = fieldValues[i];
		}
		records.add(record);
	}

	private void loadHeader(String line) {
		String[] fieldNames = line.split(separator);
		for (String name : fieldNames) {
			fields.put(name.toUpperCase(), fields.size());
		}
	}

	protected int getFieldIndex(String fieldName) {
		fieldName = fieldName.toUpperCase();
		Integer fieldIndex = fields.get(fieldName);
		if (fieldIndex == null) {
			throw new RuntimeException("Field not found: '" + fieldName + "'.");
		}
		return fieldIndex;
	}

	public String getString(String fieldName) {
		return getAsType(fieldName, FieldType.STRING);
	}

	public Integer getInteger(String fieldName) {
		return getAsType(fieldName, FieldType.INTEGER);
	}

	public Long getLong(String fieldName) {
		return getAsType(fieldName, FieldType.LONG);
	}

	public Date getDate(String fieldName) {
		return getAsType(fieldName, FieldType.LOCALDATE);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAsType(String fieldName, FieldType fieldType) {
		int fieldIndex = getFieldIndex(fieldName);
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

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setDateFormat(String format) {
		dateFormat = new SimpleDateFormat(format);
	}

	@SuppressWarnings("rawtypes")
	protected Comparable convertValue(String value, FieldType type) {
		switch (type) {
		case STRING:
			return value;
		case INTEGER:
			return Integer.valueOf(value);
		case LONG:
			return Long.valueOf(value);
		case LOCALDATE:
			try {
				return dateFormat.parse(value);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		default:
			throw new RuntimeException("Invalid field type: '" + type + "'.");
		}
	}

}
