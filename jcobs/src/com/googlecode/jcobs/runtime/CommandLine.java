package com.googlecode.jcobs.runtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>TODO Missing some automated tests!</h1>
 * 
 * A helper class for executing command lines on the OS. {@code CommandLine}
 * supports add parameters, get the return code, read std/error outputs and
 * write internal commands to the process input.
 * 
 * Also available at https://github.com/staroski.
 * 
 * @author Ricardo Staroski
 * @author Samuel Deschamps
 */
public class CommandLine {

	private final String[] command;
	private final StringBuilder errorString;
	private final StringBuilder outputString;
	private final List<String> inputs = new ArrayList<>();

	/**
	 * Creates a new command line
	 * 
	 * @param exec
	 *            The executable file path
	 * @param params
	 *            The executable arguments (optional)
	 */
	public CommandLine(String exec, String... params) {
		int args = 1 + params.length;
		String[] strings = new String[args];
		command = strings;
		command[0] = exec;
		for (int i = 1; i < args; i++) {
			command[i] = params[i - 1];
		}
		errorString = new StringBuilder();
		outputString = new StringBuilder();
	}

	public void addInput(String inputCommand) {
		inputs.add(inputCommand);
	}

	/**
	 * Executes this command line on the current Operational System
	 * 
	 * @return The process exit code
	 * @throws IOException
	 *             If the executable file is not found, or an I/O error occurs
	 */
	public int execute() throws IOException {
		try {
			Process process = Runtime.getRuntime().exec(command);

			InputStream errors = process.getErrorStream();
			InputStream output = process.getInputStream();
			OutputStream input = process.getOutputStream();

			Thread erroReader = new Thread(new StreamReader(errors, errorString));
			Thread ouputReader = new Thread(new StreamReader(output, outputString));

			erroReader.start();
			ouputReader.start();

			// "Type" the input commands (if not empty) to the executing process
			if (!inputs.isEmpty()) {
				Thread inputWriter = new Thread(new StreamWriter(input, inputs));
				inputWriter.start();
			}

			// Waits for the process to end
			return process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * @return Whatever was written to the process standard output, as a String.
	 */
	public String getOutput() {
		return outputString.toString();
	}

	/**
	 * @return Whatever was written to the process error output, as a String.
	 */
	public String getError() {
		return errorString.toString();
	}

	/**
	 * Used to read the process output and error streams
	 */
	private class StreamReader implements Runnable {

		private final InputStream stream;
		private final StringBuilder text;

		StreamReader(InputStream stream, StringBuilder text) {
			this.stream = stream;
			this.text = text;
		}

		@Override
		public void run() {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			try {
				byte[] buffer = new byte[4096];
				for (int read = -1; (read = stream.read(buffer)) != -1; bytes.write(buffer, 0, read)) {
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				text.append(new String(bytes.toByteArray()));
			}
		}
	}

	/**
	 * Used to write the inputs to the process input stream
	 */
	private class StreamWriter implements Runnable {

		private final OutputStream stream;
		private final List<String> commands;

		StreamWriter(OutputStream stream, List<String> commands) {
			this.stream = stream;
			this.commands = commands;
		}

		@Override
		public void run() {
			try {
				String firstCommand = commands.get(0) + "\n";
				stream.write(firstCommand.getBytes());
				commands.remove(0);

				executeNextCommands();

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		protected void executeNextCommands() throws IOException {
			if (commands.isEmpty()) {
				stream.close();
				return;
			}
			Thread threadToExecuteNexts = new Thread(new StreamWriter(stream, commands));
			threadToExecuteNexts.start();
		}
	}
}
