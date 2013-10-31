package net.clonecomputers.lab.util;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;

import javax.swing.*;
import javax.swing.text.*;

@SuppressWarnings("serial")
public class JConsole extends JPanel{
	private JTextPane textArea;
	private JTextField inputField;
	private File in;
	private File out;
	private File err;
	private PrintStream inOutput;

	public static void main(String[] args) throws IOException{
		final JConsole guiConsole = new JConsole();
		//System.setIn(guiConsole.getIn());
		//System.setOut(guiConsole.getOut());
		//System.setErr(guiConsole.getErr());
		JFrame consoleWindow = new JFrame("Console");
		consoleWindow.pack();
		consoleWindow.setSize(800, 600);
		consoleWindow.getContentPane().add(guiConsole);
		consoleWindow.setResizable(false);
		consoleWindow.setVisible(true);
		consoleWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		guiConsole.getOut().println("Hello world!");
		guiConsole.getErr().println("Goodbye world?");
	}
	
	public JConsole() throws IOException{
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				@Override public void run(){
					JConsole.this.initGUI();
				}
			});
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		in = File.createTempFile("JConsole.in", null);
		out = File.createTempFile("JConsole.out", null);
		err = File.createTempFile("JConsole.err", null);
		in.deleteOnExit();
		out.deleteOnExit();
		err.deleteOnExit();
		inOutput = new PrintStream(in);
		new Thread(new StreamWatcher(wrap(in), textArea, new Color(0,192,0))).start();
		new Thread(new StreamWatcher(wrap(out), textArea, Color.BLACK)).start();
		new Thread(new StreamWatcher(wrap(err), textArea, Color.RED)).start();
	}
	
	public BufferedInputStream wrap(File f) throws FileNotFoundException{
		return new BufferedInputStream(new IgnoreEOFInputStream(new FileInputStream(f)));
	}

	public void initGUI() {
		textArea = new JTextPane();
		textArea.setEditable(false);
		inputField = new JTextField(80);
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(textArea),BorderLayout.CENTER);
		this.add(inputField,BorderLayout.SOUTH);
		inputField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\n' || e.getKeyChar() == '\r'){
					inputLine();
				}
			}
		});
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

	private class StreamWatcher implements Runnable {
		private InputStream stream;
		private Style style;
		private StyledDocument document;

		private StreamWatcher(InputStream stream, JTextPane output, Color color) {
			this.stream = stream;
			style = output.addStyle(color.toString(), null);
			StyleConstants.setForeground(style,color);
			document = output.getStyledDocument();
		}

		@Override
		public void run() {
			byte[] buff = new byte[100];
			int numChars;
			try {
				while(true){
					numChars = stream.read(buff);
					String text = new String(buff, 0, numChars);
					document.insertString(document.getLength(), text, style);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public InputStream getIn(){
		try {
			return wrap(in);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public PrintStream getOut(){
		try {
			return new PrintStream(out);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public PrintStream getErr(){
		try {
			return new PrintStream(err);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	protected void inputLine() {
		String input = inputField.getText();
		inputField.setText("");
		inOutput.println(input);
	}
}
