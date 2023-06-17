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
#define SLEEP 27
#define DIR 26
#define LIGHT_SENS1 34
#define LIGHT_SENS2 35
#define MAX_STEP 1000
#define STEP_SPEED 4000

WiFiServer server(SERVER_PORT);
WiFiClient client;

bool connToWiFi = false;
bool connToClient = false;// whether or not the client was connected previously

int pins[] = { 17,18,19 };
int rolling = 0;
int step = 0;

void setup() {

  for (int i = 0;i < 3;i++) {
    pinMode(pins[i], OUTPUT);
    digitalWrite(pins[i], LOW);
  }
  pinMode(STEP, OUTPUT);
  pinMode(SLEEP, OUTPUT);
  pinMode(DIR, OUTPUT);
  pinMode(LIGHT_SENS1, INPUT);
  pinMode(LIGHT_SENS2, INPUT);
  digitalWrite(SLEEP, HIGH);


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

void sendPos() {
  if (step % 10 == 0) {
    client.print(String(step) + "\n");
    client.flush();
  }
}

void rollUP() {
  digitalWrite(DIR, HIGH);
  if (step > 0) {
    digitalWrite(SLEEP, LOW);
    step--;
    digitalWrite(STEP, HIGH);
    delayMicroseconds(STEP_SPEED);
    digitalWrite(STEP, LOW);
    delayMicroseconds(STEP_SPEED);
    sendPos();
  }
  else {
    if (digitalRead(SLEEP) == LOW) {
      digitalWrite(SLEEP, HIGH);
    }
  }
}

void rollDOWN() {
  digitalWrite(DIR, LOW);
  if (step < MAX_STEP) {
    digitalWrite(SLEEP, LOW);
    step++;
    digitalWrite(STEP, HIGH);
    delayMicroseconds(STEP_SPEED);
    digitalWrite(STEP, LOW);
    delayMicroseconds(STEP_SPEED);
    sendPos();
  }
  else {
    if (digitalRead(SLEEP) == LOW) {
      digitalWrite(SLEEP, HIGH);
    }
  }
}


void loop() {

  switch (rolling) {
  case 2:
    rollUP();
    break;

  case 1:
    rollDOWN();
    break;

  case 0:
    if (digitalRead(SLEEP) == LOW) {
      digitalWrite(SLEEP, HIGH);
    }
    break;
  }


  while (!connToClient) {
    client = server.available();
    if (client) {

      connToClient = true;
      Serial.println("We have a new client");
      client.print(String(step) + "\n");
      client.flush();

    }
    else {

      double i = analogRead(LIGHT_SENS1);

      Serial.print(double(i / 1000), 4);
      Serial.print('\t');
      i = analogRead(LIGHT_SENS2);

      Serial.println(double(i / 1000), 4);




      // Serial.print('.');
      for (int i = 0;i < 3;i++) {
        digitalWrite(pins[i], !digitalRead(pins[i]));
      }
      delay(500);
    }
  }

  if (client.connected()) {

    // clear out the input buffer:
    // client.flush();
    // client.println("Hello, client!");
    // Serial.println("Send to client!");

    // odczyt danych
    while (client.available() > 0)
    {
      int c = client.read();
      Serial.print(c);
      switch (c)
      {
      case 2:
        digitalWrite(UP, 1);
        digitalWrite(STOP, 0);
        break;

      case 0:
        sendPos();
        digitalWrite(UP, 0);
        digitalWrite(STOP, 1);
        digitalWrite(DOWN, 0);
        break;

      case 1:
        digitalWrite(DOWN, 1);
        digitalWrite(STOP, 0);
        break;

      default:
        break;
      }
      rolling = c;
      client.print('\n');

    }
  }
  else {
    connToClient = false;
    Serial.println("client lost!");
  }
}



/*
Dokumentacja Wifi.h
https://www.arduino.cc/reference/en/libraries/wifi/
*/


