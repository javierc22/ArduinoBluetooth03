#include <SoftwareSerial.h>

SoftwareSerial bluetooth(2, 3); // RX | TX 

#define led1 8
#define led2 9
#define led3 10

String comando;

void setup() {
    Serial.begin(9600);
    bluetooth.begin(9600);
  
    pinMode(led1, OUTPUT);
    pinMode(led2, OUTPUT);
    pinMode(led3, OUTPUT);
}

void loop() {
    comando = "";

    if (bluetooth.available()) {
        while (bluetooth.available()) {
            char character = bluetooth.read();
      
            comando += character;
            delay(10);
        }
    
        if (comando.indexOf("led1") >= 0) {
            digitalWrite(led1, !digitalRead(led1));
            // Serial.println("Conectar/Desconectar Led 1");
        }
    
        if (comando.indexOf("led2") >= 0) {
            digitalWrite(led2, !digitalRead(led2));
            // Serial.println("Conectar/Desconectar Led 2");
        }
    
        if (comando.indexOf("led3") >= 0) {
            digitalWrite(led3, !digitalRead(led3));
            // Serial.println("Conectar/Desconectar Led 3");
        }

        bluetooth.println("{");

        if (digitalRead(led1)) { bluetooth.println("l1on");  
        } else { bluetooth.println("l1off"); }

        if (digitalRead(led2)) { bluetooth.println("l2on");  
        } else { bluetooth.println("l2off"); }

        if (digitalRead(led3)) { bluetooth.println("l3on");  
        } else { bluetooth.println("l3off"); }

        bluetooth.println("}");
    }
}
