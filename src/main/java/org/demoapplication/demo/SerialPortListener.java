package org.demoapplication.demo;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.scene.control.ListView;


public class SerialPortListener {

    private SerialPort serialPort;
    private ListView<String> logListView;

    public SerialPortListener(ListView<String> logListView) {
        this.logListView = logListView;
    }

    public void startListening(String portName) {
        // COM portu seç ve aç
        serialPort = SerialPort.getCommPort(portName); // Örneğin "COM3"
        serialPort.setBaudRate(9600); // Mikrodenetleyicide kullanılan baud hızı
        serialPort.setNumDataBits(8);
        serialPort.setParity(SerialPort.NO_PARITY);
        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);

        if (serialPort.openPort()) {
            System.out.println("Connected: " + portName);
            logToUI("Connected: " + portName);
        } else {
            System.out.println("Connection Failed: " + portName);
            logToUI("Connection Failed: " + portName);
            return;
        }

        // Veri geldiğinde çalışacak listener
        serialPort.addDataListener(new com.fazecast.jSerialComm.SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(com.fazecast.jSerialComm.SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                byte[] buffer = new byte[serialPort.bytesAvailable()];
                int bytesRead = serialPort.readBytes(buffer, buffer.length);

                if (bytesRead > 0) {
                    String data = new String(buffer); // Gelen veriyi stringe çevir
                    System.out.println("Gelen Veri: " + buffer);
                    logToUI("Gelen Veri: " + buffer);
                }
            }
        });
    }

    public void stopListening() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            logToUI("Connection Closed.");
        }
    }

    private void logToUI(String message) {
        // JavaFX thread'inde log ekle
        Platform.runLater(() -> logListView.getItems().add(message));
    }
}
