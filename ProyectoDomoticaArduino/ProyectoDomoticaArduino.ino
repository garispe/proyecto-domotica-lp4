#include <SoftwareSerial.h>

SoftwareSerial BT(12, 13); // Receptor, Tramsmisor.
int pin;
int dato;
int configuracionBT = 9;
int relay = 3;

void setup(){

  pinMode(relay, OUTPUT);
  
  //pinMode(configuracionBT, OUTPUT); // Se utiliza para configuracion del modulo a traves de comandos AT.
  //digitalWrite(configuracionBT, HIGH);
  
  Serial.begin(9600);
  
  BT.begin(9600); // Se utiliza esta velocidad para recibir bien los caracteres enviados desde el telefono.
}
  
void loop() {
  
  if (BT.available()) {
  
      procesarDatos();
      prenderLed(pin, dato);
  }
}

void procesarDatos(){

  int decena = BT.read();
  delay(5);
  
  int unidad = BT.read();
  delay(5);
  
  dato = BT.read();
  delay(5);
  
  decena = decena - 48;
  unidad = unidad - 48;
  dato = dato - 48;

  pin = (decena*10) + unidad;  
}


void prenderLed(int pin, int dato){

  switch(dato){
  
    case 0:
    digitalWrite(pin, LOW);
    break;
    
    case 1:
    digitalWrite(pin, HIGH);
    break;
    
    default:
    Serial.println("Comando incorrecto");
    break;
  }
}
