package com.googlecode.jcobs.csv;

import java.util.Collections;
import java.util.Comparator;

public class CsvSortableReader extends CsvReader {

	public void sort(String fieldName, CsvFieldType fieldType, boolean asc) {
		int fieldIndex = getFieldIndex(fieldName);
		sort(fieldIndex, fieldType, asc);
	}

	public void sort(int fieldIndex, CsvFieldType fieldType, boolean asc) {
		Collections.sort(records, new RecordComparator(fieldIndex, fieldType, asc));
	}

	private class RecordComparator implements Comparator<String[]> {

		private int fieldIndex;
		private CsvFieldType fieldType;
		private boolean asc;

		public RecordComparator(int fieldIndex, CsvFieldType fieldType, boolean asc) {
			this.fieldIndex = fieldIndex;
			this.fieldType = fieldType;
			this.asc = asc;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public int compare(String[] o1, String[] o2) {
			// Nulls come first
			String v1 = o1[fieldIndex];
			String v2 = o2[fieldIndex];
			int res;
			if (v1 == null) {
				if (v2 == null) {
					res = 0;
				} else {
					res = -1;
				}
			} else {
				if (v2 == null) {
					res = 1;
				} else {
					Comparable r1 = convertValue(v1, fieldType);
					Comparable r2 = convertValue(v2, fieldType);
					res = r1.compareTo(r2);
				}
			}
			return asc ? res : -res;
		}

	}

}
