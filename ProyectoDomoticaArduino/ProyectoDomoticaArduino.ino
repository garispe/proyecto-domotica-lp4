#include <SoftwareSerial.h>

SoftwareSerial BT(10, 11); // Receptor, Tramsmisor.

void setup(){

  pinMode(13, OUTPUT); // Se utiliza para pruebas.

  /*
  
  Serial se utiliza para debug.
  El Pin 9 para configuracion.
  
  */

  pinMode(9, OUTPUT); // Se utiliza para configuracion del modulo a traves de comandos AT.
  digitalWrite(9, HIGH);

  Serial.begin(9600);
  Serial.println("Ingrese comando AT:");

  BT.begin(9600); // Se utiliza esta velocidad para recibir bien los caracteres enviados desde el telefono.
}

void loop() {

  if (BT.available()) {

    char dato = BT.read();
    Serial.println(dato);
    delay(5);

    switch(dato){

    case '0':
      digitalWrite(13, LOW);
      break; 

    case '1':
      digitalWrite(13, HIGH);
      break;

    default:
      Serial.println("Comando incorrecto");
      break;
    }
  }
}




