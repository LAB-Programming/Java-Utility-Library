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
	private FileBackedPipe in;
	private FileBackedPipe out;
	private FileBackedPipe err;
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
		consoleWindow.setResizable(true);
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
		in = new FileBackedPipe();
		out = new FileBackedPipe();
		err = new FileBackedPipe();
		inOutput = in.getOutputStream();
		new Thread(new StreamWatcher(in.getInputStream(), textArea, new Color(0,192,0))).start();
		new Thread(new StreamWatcher(out.getInputStream(), textArea, Color.BLACK)).start();
		new Thread(new StreamWatcher(err.getInputStream(), textArea, Color.RED)).start();
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
					if(numChars <= 0) continue;
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
			return in.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public PrintStream getOut(){
		try {
			return out.getOutputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public PrintStream getErr(){
		try {
			return err.getOutputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void inputLine() {
		String input = inputField.getText();
		inputField.setText("");
		inOutput.println(input);
	}
}
