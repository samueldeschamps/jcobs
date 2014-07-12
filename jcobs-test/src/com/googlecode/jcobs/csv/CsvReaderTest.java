package com.googlecode.jcobs.csv;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class CsvReaderTest {
	
	@Test
	public void testReadCsvWithHeader_ByFieldName_IntegerString() throws IOException {
		CsvReader reader = new CsvReader();
		reader.readFile(getColaboradoresFile());

		Assert.assertTrue(reader.next());
		Assert.assertEquals(11_000, reader.getInteger("codigo").intValue());
		Assert.assertEquals("Thais Nepomuceno", reader.getString("nome"));
		Assert.assertEquals("Produção", reader.getString("setor"));
		Assert.assertEquals(null, reader.getInteger("codigogerente"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(11_001, reader.getInteger("CODIGO").intValue());
		Assert.assertEquals("Alana Monteiro Bispo da Silva", reader.getString("NOME"));
		Assert.assertEquals("Produção", reader.getString("SETOR"));
		Assert.assertEquals(11_000, reader.getInteger("CODIGOGERENTE").intValue());
		
		Assert.assertTrue(reader.next());
		Assert.assertEquals(11_002, reader.getInteger("Codigo").intValue());
		Assert.assertEquals("Amanda C. Moreira", reader.getString("Nome"));
		Assert.assertEquals("Produção", reader.getString("Setor"));
		Assert.assertEquals(11_000, reader.getInteger("CodigoGerente").intValue());
		
		Assert.assertTrue(reader.next());
		Assert.assertEquals("Angélica Augusta Linhares do Monte", reader.getString("nome"));
		Assert.assertEquals("Passadoria", reader.getString("setor"));
		Assert.assertEquals(11_003, reader.getInteger("codigo").intValue());
		Assert.assertEquals(11_002, reader.getInteger("codigogerente").intValue());

		Assert.assertFalse(reader.next());
	}
	
	@Test
	public void testReadCsvWithHeader_ByFieldIndex_IntegerString() throws IOException {
		CsvReader reader = new CsvReader();
		reader.readFile(getColaboradoresFile());
		
		Assert.assertTrue(reader.next());
		Assert.assertEquals(11_000, reader.getInteger(0).intValue());
		Assert.assertEquals("Thais Nepomuceno", reader.getString(1));
		Assert.assertEquals("Produção", reader.getString(2));
		Assert.assertEquals(null, reader.getInteger(3));
		
		Assert.assertTrue(reader.next());
		Assert.assertEquals("Alana Monteiro Bispo da Silva", reader.getString(1));
		Assert.assertEquals("Produção", reader.getString(2));
		Assert.assertEquals(11_001, reader.getInteger(0).intValue());
		Assert.assertEquals(11_000, reader.getInteger(3).intValue());
		
		Assert.assertTrue(reader.next());
		Assert.assertEquals(11_000, reader.getInteger(3).intValue());
		Assert.assertEquals(11_002, reader.getInteger(0).intValue());
		Assert.assertEquals("Amanda C. Moreira", reader.getString(1));
		Assert.assertEquals("Produção", reader.getString(2));
		
		Assert.assertTrue(reader.next());
		Assert.assertEquals(11_003, reader.getInteger(0).intValue());
		Assert.assertEquals("Angélica Augusta Linhares do Monte", reader.getString(1));
		Assert.assertEquals("Passadoria", reader.getString(2));
		Assert.assertEquals(11_002, reader.getInteger(3).intValue());
		
		Assert.assertFalse(reader.next());
	}
	
	@Test
	public void testRecordCount() throws IOException {
		CsvReader reader = new CsvReader();
		reader.readFile(getColaboradoresFile());
		Assert.assertEquals(4, reader.recordCount());
	}

	private File getColaboradoresFile() {
		return new File(getClass().getResource("colaboradores.csv").getPath());
	}

}
