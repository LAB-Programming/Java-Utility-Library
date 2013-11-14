package net.clonecomputers.lab.util;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class JavaFileChooserDialog extends JPanel {
	private class FileSystemListModel implements ListModel {
		private FileSystemView view;
		private File dir;
		public FileSystemListModel(FileSystemView view, File dir){
			this.view = view;
			this.dir = dir;
		}
		
		@Override
		public int getSize() {
			return view.getFiles(dir,true).length;
		}

		@Override
		public File getElementAt(int i) {
			return view.getFiles(dir,true)[i];
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			// TODO: Make this do something
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			// TODO: Make this do something
		}

	}
	private JPanel fileSystemPanel;
	private FileSystemView view;
	private LinkedList<JList> fileSystemLists;
	private boolean wasCanceled = false;
	public JavaFileChooserDialog(){
		this(FileSystemView.getFileSystemView());
	}
	public JavaFileChooserDialog(FileSystemView view){
		super();
		fileSystemLists = new LinkedList<JList>();
		this.view = view;
		this.setLayout(new BorderLayout());
		fileSystemPanel = new JPanel();
		fileSystemPanel.setLayout(new BoxLayout(fileSystemPanel,BoxLayout.X_AXIS));
		this.add(new JScrollPane(fileSystemPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),BorderLayout.CENTER);
		fileSystemPanel.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e){
				synchronized(JavaFileChooserDialog.this){
					JavaFileChooserDialog.this.notifyAll();
				}
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wasCanceled = true;
				synchronized(JavaFileChooserDialog.this){
					JavaFileChooserDialog.this.notifyAll();
				}
			}
		});
		buttonPanel.add(cancelButton);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized(JavaFileChooserDialog.this){
					JavaFileChooserDialog.this.notifyAll();
				}
			}
		});
		buttonPanel.add(okButton);
		this.add(buttonPanel,BorderLayout.SOUTH);
		//addList(view.getDefaultDirectory());
	}
	
	private void addList(File dir){
		JList list = new JList(new FileSystemListModel(view, dir));
		list.setCellRenderer(new DefaultListCellRenderer() {
			
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				return super.getListCellRendererComponent(list, ((File)value).getName(), index, isSelected, cellHasFocus);
			}
			
		});
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) return;
				JList list = (JList)e.getSource();
				File f = (File)list.getSelectedValue();
				while(fileSystemLists.peekLast() != list){
					fileSystemLists.pollLast();
					fileSystemPanel.remove(fileSystemLists.size());
				}
				if(f.isDirectory()) addList(f);
				JavaFileChooserDialog.this.validate();
			}
		});
		list.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e){
				synchronized(JavaFileChooserDialog.this){
					JavaFileChooserDialog.this.notifyAll();
				}
			}
		});
		fileSystemLists.add(list);
		fileSystemPanel.add(new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}
	private File getSelectedFile() {
		JList lastList = fileSystemLists.getLast();
		if(lastList.isSelectionEmpty() && fileSystemLists.size() > 1){
			lastList = fileSystemLists.get(fileSystemLists.size()-2);
		}
		if(lastList.isSelectionEmpty()) return null;
		return (File)lastList.getSelectedValue();
	}
	public static File openFile(Container panel){
		return openFile(panel, FileSystemView.getFileSystemView().getDefaultDirectory());
	}
	public static File openFile(Container panel, File startingDirectory){
		JavaFileChooserDialog jfcd = new JavaFileChooserDialog();
		jfcd.addList(startingDirectory);
		panel.add(jfcd);
		panel.validate();
		synchronized(jfcd){
			try {
				jfcd.wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		if(jfcd.wasCanceled) return null;
		return jfcd.getSelectedFile();
	}
	
	public static void main(String[] args){
		final JFrame window = new JFrame();
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.pack();
				window.setSize(new Dimension(500,400));
				window.setVisible(true);
			}
		});
		System.out.println(openFile(window.getContentPane()));
		window.dispose();
	}
}
