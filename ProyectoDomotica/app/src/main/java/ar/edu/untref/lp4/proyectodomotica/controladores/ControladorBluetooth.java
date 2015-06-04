package ar.edu.untref.lp4.proyectodomotica.controladores;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Clase que se encarga de manejar la conexión bluetooth y la transferencia de datos.
 */
public class ControladorBluetooth {

    private static final String TAG = ControladorBluetooth.class.getSimpleName();

    private static final String MAC_MODULO_BLUETOOTH = "98:D3:31:70:3D:01";
    private static final UUID UUID_CONEXION = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private boolean estaConectado = false;

    private static ControladorBluetooth instance = new ControladorBluetooth();

    private int intentos = 0;

    private ControladorBluetooth() {

        Logger.init(TAG);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static ControladorBluetooth getInstance(){

        return instance;

    }

    public BluetoothAdapter getBluetoothAdapter(){
        return this.bluetoothAdapter;
    }

    /**
     * Realiza la conexión con el módulo bluetooth conectado a la placa Arduino.
     * Si el bluetooth del teléfono esta habilitado, se crea el socket para la comunicación con el módulo.
     * Si está deshabilitado, se lo habilita y se reintenta la conexión.
     */
    public boolean conectar() {

        if (bluetoothAdapter != null) {

            if (bluetoothAdapter.isEnabled()) {

                Logger.i("Bluetooth habilitado. Conectando...");
                intentos = 0;

                bluetoothAdapter.cancelDiscovery();

                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(MAC_MODULO_BLUETOOTH);

                try {

                    socket = device.createRfcommSocketToServiceRecord(UUID_CONEXION);
                    socket.connect();

                    estaConectado = true;

                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();

                    Logger.i("Conexion establecida");

                } catch (IOException e) {

                    estaConectado = false;
                    Logger.i("Timeout de Conexion");
                    e.printStackTrace();
                }

            } else {

                intentos++;

                if(intentos <= 500){

                    bluetoothAdapter.enable();
                    conectar();

                } else {

                    estaConectado = false;
                    Logger.i("No se pudo realizar la conexion. " + (intentos - 1) + " intentos realizados");
                }
            }
        }

        return estaConectado;
    }

    public boolean estaConectado(){
        return estaConectado;
    }

    /**
     * Cierra el socket de comunicación con el módulo bluetooth
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