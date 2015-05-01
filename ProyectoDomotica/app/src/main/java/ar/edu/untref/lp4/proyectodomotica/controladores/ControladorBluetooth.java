package ar.edu.untref.lp4.proyectodomotica.controladores;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ControladorBluetooth {

    private static final String MAC_MODULO_BLUETOOTH = "98:D3:31:70:3D:01";
    private static final UUID UUID_CONEXION = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public ControladorBluetooth() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void conectar() {

        if (bluetoothAdapter != null) {

            if (bluetoothAdapter.isEnabled()) {

                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(MAC_MODULO_BLUETOOTH);

                try {

                    socket = device.createRfcommSocketToServiceRecord(UUID_CONEXION);
                    socket.connect();

                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {

                Log.e("ERROR ---->", "Bluetooth deshabilitado.");

                bluetoothAdapter.enable();
                conectar();
            }
        }
    }

    public void enviarDato(String dato) {

        try {
            outputStream.write(dato.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO:
    // Falta probar

    public String leerDato() throws IOException {

        String datoLeido = "No se leyó ningún dato";

        byte[] respuesta = new byte[5];

        for (int i = 0; i < 5; i++) {

            byte[] bytes = new byte[1];
            inputStream.read(bytes);
            respuesta[i] = bytes[0];
        }

        datoLeido = new String(respuesta);

        return datoLeido;
    }
}