package com.googlecode.jcobs.csv;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
	public void testReadCsvWithHeader_ByFieldName_WithDelimiters_DateString() throws IOException, ParseException {
		CsvReader reader = new CsvReader();
		reader.readFile(getFeriadosFile());

		SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy");

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("01/01/2014"), reader.getDate("data"));
		Assert.assertEquals("Confraternização Internacional", reader.getString("nome"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("12/06/2014"), reader.getDate("data"));
		Assert.assertEquals("Dia dos \"Namorados\"", reader.getString("nome"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("19/06/2014"), reader.getDate("data"));
		Assert.assertEquals("Corpus Christi", reader.getString("nome"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("08/07/2014"), reader.getDate("data"));
		Assert.assertEquals("Semifinais Copa Brasil;Alemanha 7 x 1 Brasil", reader.getString("nome"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("02/11/2014"), reader.getDate("data"));
		Assert.assertEquals("Finados", reader.getString("nome"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("15/11/2014"), reader.getDate("data"));
		Assert.assertEquals("Proclamação da República", reader.getString("nome"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("25/12/2014"), reader.getDate("data"));
		Assert.assertEquals("Natal", reader.getString("nome"));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("31/12/2014"), reader.getDate("data"));
		Assert.assertEquals("Véspera de Ano Novo", reader.getString("nome"));

		Assert.assertFalse(reader.next());
	}

	@Test
	public void testReadCsvWithHeader_ByFieldIndex_WithDelimiters_DateString() throws IOException, ParseException {
		CsvReader reader = new CsvReader();
		reader.readFile(getFeriadosFile());

		SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy");

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("01/01/2014"), reader.getDate(0));
		Assert.assertEquals("Confraternização Internacional", reader.getString(1));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("12/06/2014"), reader.getDate(0));
		Assert.assertEquals("Dia dos \"Namorados\"", reader.getString(1));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("19/06/2014"), reader.getDate(0));
		Assert.assertEquals("Corpus Christi", reader.getString(1));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("08/07/2014"), reader.getDate(0));
		Assert.assertEquals("Semifinais Copa Brasil;Alemanha 7 x 1 Brasil", reader.getString(1));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("02/11/2014"), reader.getDate(0));
		Assert.assertEquals("Finados", reader.getString(1));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("15/11/2014"), reader.getDate(0));
		Assert.assertEquals("Proclamação da República", reader.getString(1));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("25/12/2014"), reader.getDate(0));
		Assert.assertEquals("Natal", reader.getString(1));

		Assert.assertTrue(reader.next());
		Assert.assertEquals(dateFmt.parse("31/12/2014"), reader.getDate(0));
		Assert.assertEquals("Véspera de Ano Novo", reader.getString(1));

		Assert.assertFalse(reader.next());
	}

	@Test
	public void testRecordCount() throws IOException {
		CsvReader reader = new CsvReader();
		reader.readFile(getColaboradoresFile());
		Assert.assertEquals(4, reader.recordCount());
	}

	@Test
	public void testException_FieldNotFound() throws IOException {
		CsvReader reader = new CsvReader();
		reader.readFile(getColaboradoresFile());
		try {
			reader.getString("date");
			Assert.fail("A CsvFormatError must have been thrown.");
		} catch (CsvFormatError ex) {
			Assert.assertEquals("Field not found: 'DATE'.", ex.getMessage());
		}
	}
	
	@Test
	public void testException_FieldIndexNotFound() throws IOException {
		CsvReader reader = new CsvReader();
		reader.readFile(getColaboradoresFile());
		try {
			reader.getString(4);
			Assert.fail("An IllegalArgumentException must have been thrown.");
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals("Invalid field index: 4. Max is 3.", ex.getMessage());
		}
	}

	private File getColaboradoresFile() {
		return new File(getClass().getResource("colaboradores.csv").getPath());
	}

	private File getFeriadosFile() {
		return new File(getClass().getResource("feriados.csv").getPath());
	}

}
