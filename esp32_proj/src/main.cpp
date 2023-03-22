#include <Arduino.h>
#include <WiFi.h>

#define WIFI_SSID "Keddom"
#define WIFI_PASS "123ewqasdcxz"
#define SERVER_PORT 23

WiFiServer server(SERVER_PORT);
WiFiClient client;

bool connToWiFi=false;
bool connToClient=false;// whether or not the client was connected previously

void setup() {
  Serial.begin(921600);
    while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  WiFi.begin(WIFI_SSID,WIFI_PASS);
  Serial.println("Starting");
  pinMode(BUILTIN_LED,OUTPUT);

// laczenie sie z siecia lokalna
  while (!connToWiFi)
  {
    if(!connToWiFi && WiFi.status()==WL_CONNECTED ){
      Serial.println("Connected");
      Serial.println(WiFi.localIP());
      // 192.168.1.23
      connToWiFi = true;
      digitalWrite(LED_BUILTIN,HIGH);
    }else{
      Serial.println(".");
      digitalWrite(LED_BUILTIN,!digitalRead(LED_BUILTIN));
      delay(100);  
    }
  }

  // start the server:
  server.begin();

}


void loop() {

/*
Returns the number of bytes available for reading
(that is, the amount of data that has been written to the client
 by the server it is connected to).
*/
  WiFiClient client = server.available();


  if (client) {
    if (!connToClient) {
      // clear out the input buffer:
      client.flush();
      Serial.println("We have a new client");
      client.println("Hello, client!");
      connToClient = true;
    }
    if (client.available() > 0) {
      // read the bytes incoming from the client:
      char thisChar = client.read();
      // echo the bytes back to the client:
      server.write(thisChar);
      // echo the bytes to the server as well:
      Serial.write(thisChar);
    }
  }
  // else{
  //   Serial.println("Waiting for client...");
  //   delay(1500);
  // }


  
}

/*
Dokumentacja Wifi.h
https://www.arduino.cc/reference/en/libraries/wifi/
*/


