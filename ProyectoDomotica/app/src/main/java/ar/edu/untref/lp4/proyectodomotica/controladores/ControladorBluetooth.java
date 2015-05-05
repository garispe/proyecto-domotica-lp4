package ar.edu.untref.lp4.proyectodomotica.controladores;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Clase que se encarga de manejar la conexión bluetooth y la transferencia de datos.
 */
public class ControladorBluetooth {

    private static final String TAG = "ControladorBluetooth --->";

    private static final String MAC_MODULO_BLUETOOTH = "98:D3:31:70:3D:01";
    private static final UUID UUID_CONEXION = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private int intentos = 0;

    public ControladorBluetooth() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getBluetoothAdapter(){
        return this.bluetoothAdapter;
    }

    /**
     * Se realiza la conexión con el módulo bluetooth conectado a la placa Arduino.
     * Si el bluetooth del teléfono esta habilitado, se crea el socket para la comunicación con el módulo.
     * Si está deshabilitado, se lo habilita y se reintenta la conexión.
     */
    public void conectar() {

        if (bluetoothAdapter != null) {

            if (bluetoothAdapter.isEnabled()) {

                Log.e(TAG, "Bluetooth habilitado. Conectando...");
                intentos = 0;

                bluetoothAdapter.cancelDiscovery();

                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(MAC_MODULO_BLUETOOTH);

                try {

                    socket = device.createRfcommSocketToServiceRecord(UUID_CONEXION);
                    socket.connect();

                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();

                    Log.e(TAG, "Conectado");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {

                intentos++;

                if(intentos <= 1000){

                    bluetoothAdapter.enable();
                    conectar();

                } else {

                    Log.e(TAG, "No se pudo realizar la conexion. " + (intentos - 1) + " intentos realizados");
                }
            }
        }
    }


    /**
     * Se cierra el socket de comunicación con el módulo bluetooth
     * y se deshabilita el bluetooth del teléfono.
     */
    public void desconectar(){

        try {

            if(this.socket != null){
                this.socket.close();
            }

            this.bluetoothAdapter.disable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envía el dato pasado por parámetro, a través del socket, al módulo bluetooh.
     */
    public void enviarDato(String dato) {

        try {
            outputStream.write(dato.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO:
    // Falta probar

    /**
     * Lee un dato, a través del socket, proveniente desde el módulo bluetooh.
     */
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