package com.github.matt.williams.vhack.racer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AccelerometerEventThread extends Thread {
	private Socket socket = null;
    private ControllerCallback mControllerCallback;

	public AccelerometerEventThread(Socket socket, ControllerCallback controllerCallback) {
		this.socket = socket;
	}

	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				String[] values = inputLine.split(", ");
				
				//check we have acceleration and direction
				if (values.length >= 2) {
					float steering = Float.parseFloat(values[0]);
					float speed = Float.parseFloat(values[1]);
					
					mControllerCallback.control(steering, speed);
				}
			}
			out.close();
			in.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}