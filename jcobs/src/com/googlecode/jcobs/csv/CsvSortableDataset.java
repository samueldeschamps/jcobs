package com.googlecode.jcobs.csv;

import java.util.Collections;
import java.util.Comparator;

public class CsvSortableDataset extends CsvReadableDataset {

	public void sort(String fieldName, FieldType fieldType, boolean asc) {
		int fieldIndex = getFieldIndex(fieldName);
		Collections.sort(records, new RecordComparator(fieldIndex, fieldType, asc));
	}

	private class RecordComparator implements Comparator<String[]> {

		private int fieldIndex;
		private FieldType fieldType;
		private boolean asc;

		public RecordComparator(int fieldIndex, FieldType fieldType, boolean asc) {
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
