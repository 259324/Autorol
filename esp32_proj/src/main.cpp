#include <Arduino.h>
#include <WiFi.h>

#define WIFI_SSID "POCO"
// #define WIFI_SSID "Keddom"
// #define WIFI_SSID "raw_AP"
// #define WIFI_SSID "multimedia_RawDom"
#define WIFI_PASS "123ewqasdcxz"
#define SERVER_PORT 23

#define LED_UP 32 
#define LED_STOP 33 
#define LED_DOWN 25 
#define STEP 26
#define SLEEP 27
#define DIR 13

#define LIGHT_SENS2 36
#define LIGHT_SENS1 39

#define STEP_SPEED 4000

WiFiServer server(SERVER_PORT);
WiFiClient client;

bool connToWiFi = false;
bool connToClient = false;// whether or not the client was connected previously

int pins[] = { LED_UP,LED_STOP,LED_DOWN,STEP,SLEEP,DIR,BUILTIN_LED };

int rolling = 0;
int step = 0;
int MAXstep = 1000;

bool kalibRoz = false;
bool kalibZwin = false;
bool trybAuto = false;
int trybAuto_pom = 0;
int b = B00000000;
int trybAuto_delay = 0;



void setup() {

  Serial.begin(921600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  for (int i = 0;i < sizeof(pins) / sizeof(int);i++) {
    pinMode(pins[i], OUTPUT);
    digitalWrite(pins[i], HIGH);
  }
  pinMode(LIGHT_SENS1, INPUT);
  pinMode(LIGHT_SENS2, INPUT);

  WiFi.begin(WIFI_SSID, WIFI_PASS);
  Serial.println("\nStarting");

  // laczenie sie z siecia lokalna
  while (!connToWiFi)
  {
    if (!connToWiFi && WiFi.status() == WL_CONNECTED) {
      Serial.println("Connected");
      Serial.println(WiFi.localIP());
      // 192.168.1.23
      connToWiFi = true;
      digitalWrite(BUILTIN_LED, HIGH);
    }
    else {
      Serial.print(".");
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
    Serial.print("Step ");
    Serial.println(step);

  }
}

void rollUP() {
  digitalWrite(DIR, HIGH);
  if (kalibRoz || kalibZwin) {
    digitalWrite(SLEEP, LOW);
    digitalWrite(STEP, HIGH);
    delayMicroseconds(STEP_SPEED);
    digitalWrite(STEP, LOW);
    delayMicroseconds(STEP_SPEED);
  }
  else if (step > 0) {
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
    rolling = 0;
    trybAuto_delay = 1000;

  }
}

void rollDOWN() {
  digitalWrite(DIR, LOW);
  if (kalibRoz || kalibZwin) {
    digitalWrite(SLEEP, LOW);
    step++;
    digitalWrite(STEP, HIGH);
    delayMicroseconds(STEP_SPEED);
    digitalWrite(STEP, LOW);
    delayMicroseconds(STEP_SPEED);
  }
  else if (step < MAXstep) {
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
    trybAuto_delay = 1000;
    rolling = 0;
  }
}


void loop() {

  if (trybAuto) {

    double L1 = analogRead(LIGHT_SENS1);
    double L2 = analogRead(LIGHT_SENS2);


    if (L1 - L2 > 200 || (L1 > 300 && L2 > 300)) {
      b = (b << 1) | 1;
    }
    else {
      b = b << 1;
    }

    // do gory
    // if (L1 > L2) {
    if (b == B11111111) {
      Serial.println("1");
      if (trybAuto_pom != 2) {
        Serial.println("Auto UP");
        trybAuto_delay = 0;
        trybAuto_pom = 2;
        rolling = 2;
      }

    }//na dol
    else if (b == B00000000) {
      Serial.println("0");
      if (trybAuto_pom != 1) {
        Serial.println("Auto DOWN");
        trybAuto_delay = 0;
        trybAuto_pom = 1;
        rolling = 1;
      }

    }

    // Serial.print("L1 ");
    // Serial.print(double(L1 / 1000), 4);
    // Serial.print("\tL2 ");
    // Serial.println(double(L2 / 1000), 4);

    // delay(200);
  }

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
      Serial.println("\nWe have a new client");
      client.print(String(MAXstep) + "\n");
      client.print(String(step) + "\n");
      client.flush();

    }
    else {
      Serial.print('.');



      for (int i = 0;i < 3;i++) {
        digitalWrite(pins[i], HIGH);
      }
      delay(500);
      for (int i = 0;i < 3;i++) {
        digitalWrite(pins[i], LOW);
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
      Serial.println(c);
      Serial.flush();
      switch (c)
      {
      case 2:
        digitalWrite(LED_UP, 1);
        digitalWrite(LED_STOP, 0);
        break;

      case 0:
        sendPos();
        digitalWrite(LED_UP, 0);
        digitalWrite(LED_STOP, 1);
        digitalWrite(LED_DOWN, 0);
        break;

      case 1:
        digitalWrite(LED_DOWN, 1);
        digitalWrite(LED_STOP, 0);
        break;

      case 4:
        if (kalibRoz) {
          MAXstep = step;
          client.print(String(-MAXstep) + "\n");
          client.flush();
          Serial.print("kalib Roz zakonczona MAXstep = ");
          Serial.println(MAXstep);
          kalibRoz = false;
        }
        else {
          Serial.println("kalib Roz start");
          kalibRoz = true;
        }
        c = 0;
        break;

      case 3:
        if (kalibZwin) {
          step = 0;
          Serial.print("kalib Zwin zakonczona step = ");
          Serial.println(step);
          kalibZwin = false;
        }
        else {
          Serial.println("kalib Zwin start");
          kalibZwin = true;
        }
        c = 0;
        break;

      case 5:
        trybAuto = !trybAuto;
        trybAuto_pom = 0;
        c = 0;
        break;

      default:
        c = 0;
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


