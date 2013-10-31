package net.clonecomputers.lab.util;

import java.io.*;

public class FileBackedPipe {
	private final File backing;

	public FileBackedPipe() throws IOException {
		backing = File.createTempFile(this.toString(), "pipe");
		backing.deleteOnExit();
	}

	public PrintStream getOutputStream() throws IOException {
		try {
			return new PrintStream(new WakeUpOutputStream(new FileOutputStream(backing)));
		} catch (FileNotFoundException e) {
			throw new IOException("Backing file not found: must have been deleted",e);
		}
	}

	public InputStream getInputStream() throws IOException {
		try {
			return new IgnoreEOFInputStream(new FileInputStream(backing));
		} catch (FileNotFoundException e) {
			throw new IOException("Backing file not found: must have been deleted",e);
		}
	}
	
	private class WakeUpOutputStream extends FilterOutputStream {

		public WakeUpOutputStream(OutputStream out) {
			super(out);
		}
		
		@Override public void write(int b) throws IOException {
			super.write(b);
			wakeUp();
		}
		
		@Override public void write(byte[] b) throws IOException {
			super.write(b);
			wakeUp();
		}
		
		@Override public void write(byte[] b, int off, int len) throws IOException {
			super.write(b, off, len);
			wakeUp();
		}
		
		private void wakeUp(){
			synchronized(FileBackedPipe.this){
				FileBackedPipe.this.notifyAll();
			}
			Thread.yield();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
	}

	private class IgnoreEOFInputStream extends FilterInputStream {

		public IgnoreEOFInputStream(InputStream in) {
			super(in);
		}

		@Override public int read() {
			try {
				while(in.available() == 0) sleep();
				return in.read();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override public int read(byte[] b) {
			try{
				while(in.available() == 0) sleep();
				return in.read(b);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override public int read(byte[] b, int off, int len) {
			try{
				while(in.available() == 0) sleep();
				return in.read(b, off, len);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void sleep() throws InterruptedException {
			synchronized(FileBackedPipe.this){
				FileBackedPipe.this.wait();
			}
		}
	}
}
