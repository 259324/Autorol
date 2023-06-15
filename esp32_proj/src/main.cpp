#include <Arduino.h>
#include <WiFi.h>

#define WIFI_SSID "Keddom"
// #define WIFI_SSID "raw_AP"
// #define WIFI_SSID "multimedia_RawDom"
#define WIFI_PASS "123ewqasdcxz"
#define SERVER_PORT 23
#define UP 19
#define STOP 18
#define DOWN 17
#define STEP 21


WiFiServer server(SERVER_PORT);
WiFiClient client;

bool connToWiFi = false;
bool connToClient = false;// whether or not the client was connected previously

int pins[] = { 17,18,19 };

void setup() {

  for (int i = 0;i < 3;i++) {
    pinMode(pins[i], OUTPUT);
    digitalWrite(pins[i], LOW);
  }
  pinMode(STEP, OUTPUT);


  Serial.begin(921600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  WiFi.begin(WIFI_SSID, WIFI_PASS);
  Serial.println("\nStarting");
  pinMode(BUILTIN_LED, OUTPUT);

  // laczenie sie z siecia lokalna
  while (!connToWiFi)
  {
    if (!connToWiFi && WiFi.status() == WL_CONNECTED) {
      Serial.println("Connected");
      Serial.println(WiFi.localIP());
      // 192.168.1.23
      connToWiFi = true;
      digitalWrite(LED_BUILTIN, HIGH);
    }
    else {
      Serial.print(".");
      digitalWrite(LED_BUILTIN, !digitalRead(LED_BUILTIN));
      delay(200);
    }
  }


  // start the server:
  server.begin();



}


void loop() {

  for (int i = 0;i < 200;i++) {
    digitalWrite(STEP, HIGH);
    delayMicroseconds(2000);
    digitalWrite(STEP, LOW);
    delayMicroseconds(2000);

  }

  // while (!connToClient) {
  //   client = server.available();
  //   if (client) {

  //     connToClient = true;
  //     Serial.println("We have a new client");
  //   }
  //   else {
  //     Serial.print('.');
  //     for (int i = 0;i < 3;i++) {
  //       digitalWrite(pins[i], !digitalRead(pins[i]));
  //     }
  //     delay(500);
  //   }
  // }

  // if (client.connected()) {
  //   // clear out the input buffer:
  //   // client.flush();
  //   // client.println("Hello, client!");
  //   // Serial.println("Send to client!");

  //   // odczyt danych
  //   while (client.available() > 0)
  //   {
  //     int c = client.read();
  //     Serial.print(c);
  //     if (c == 2) {
  //       digitalWrite(UP, 1);
  //       digitalWrite(STOP, 0);
  //     }
  //     else if (c == 0) {
  //       digitalWrite(UP, 0);
  //       digitalWrite(STOP, 1);
  //       digitalWrite(DOWN, 0);
  //     }
  //     else if (c == 1) {
  //       digitalWrite(DOWN, 1);
  //       digitalWrite(STOP, 0);
  //     }
  //   }
  // }
  // else {
  //   connToClient = false;
  //   Serial.println("client lost!");
  // }
}

/*
Dokumentacja Wifi.h
https://www.arduino.cc/reference/en/libraries/wifi/
*/


