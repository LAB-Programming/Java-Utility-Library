package net.clonecomputers.lab.util;

import java.io.*;

public class FileBackedPipe {
	private final File backing;

	public FileBackedPipe() throws IOException {
		backing = File.createTempFile(this.toString(), "pipe");
	}

	public PrintStream getOutputStream() throws IOException {
		try {
			return new PrintStream(backing);
		} catch (FileNotFoundException e) {
			throw new IOException("Backing file not found: must have been deleted",e);
		}
	}

	public BufferedInputStream getInputStream() throws IOException {
		try {
			return new BufferedInputStream(new IgnoreEOFInputStream(new FileInputStream(backing)));
		} catch (FileNotFoundException e) {
			throw new IOException("Backing file not found: must have been deleted",e);
		}
	}

	private class IgnoreEOFInputStream extends FilterInputStream{

		public IgnoreEOFInputStream(InputStream in) {
			super(in);
		}

		@Override public int read() {
			try {
				while(in.available() <= 0) {
					Thread.sleep(100);
					continue;
				}
				return in.read();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override public int read(byte[] b) {
			try{
				while(in.available() <= 0){
					Thread.sleep(100);
					continue;
				}
				return in.read(b);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override public int read(byte[] b, int off, int len) {
			try{
				while(in.available() <= 0){
					Thread.sleep(100);
					continue;
				}
				return in.read(b, off, len);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
