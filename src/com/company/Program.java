package com.company;

import javax.swing.JFrame;

public class Program extends JFrame {

	private static final long serialVersionUID = 1L;
	private GUI gof;
	public static int width = 1024;
	public static int height = 768;
	public Program() {
		setTitle("Sound simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gof = new GUI(this);
		gof.initialize(this.getContentPane());
		this.setSize(width, height);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Program();
	}
}
