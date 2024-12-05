package org.demoapplication.demo;

import com.fazecast.jSerialComm.SerialPort;

public class TestSerialPort {
    public static void main(String[] args) {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available COM Ports:");
        for (SerialPort port : ports) {
            System.out.println(port.getSystemPortName());
        }
    }
}
