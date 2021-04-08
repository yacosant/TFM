#include <splash.h>

#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiAP.h>

//Libraries for OLED Display
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

//OLED pins
#define OLED_SDA 4
#define OLED_SCL 15
#define OLED_RST 16
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels

// Set these to your desired credentials.
const char *ssid = "AP";
const char *password = "yaco123456789";
//definition funtion
void printLcd(char* text, int size=2, bool clear=true, int x=0, int y=0);

// Display
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RST);

WiFiServer server(80);

void setup() {
  // put your setup code here, to run once:
  setup_Serial();
  setup_Display();
  setup_Wifi();
  //printLcd("RUNING");
}


void setup_Serial() {
  Serial.begin(115200);
  Serial.println();
}

void setup_Display() {
  Serial.println("[LCD] Configuring Pins...");
    pinMode(OLED_RST, OUTPUT);
    digitalWrite(OLED_RST, LOW);
    delay(20);
    digitalWrite(OLED_RST, HIGH);
    
    //initialize OLED
    Wire.begin(OLED_SDA, OLED_SCL);
    if(!display.begin(SSD1306_SWITCHCAPVCC, 0x3c, false, false)) { // Address 0x3C for 128x32
      Serial.println(F("SSD1306 allocation failed"));
      for(;;); // Don't proceed, loop forever
    }

  display.clearDisplay();
  display.setTextColor(WHITE);
  display.setTextSize(2);

}

void setup_Wifi() {
  Serial.println("[WIFI] Configuring Wifi Access Point...");
  
  IPAddress myIP = WiFi.softAP(ssid, password);
  Serial.print("[WIFI] AP IP address: ");
  Serial.println(myIP);
  server.begin();
  Serial.println("[WIFI] Wifi Access Point started!");
}


void loop() {
  // put your main code here, to run repeatedly:
  printLcd("LISTO!",2);
  WiFiClient client = server.available();   // listen for incoming clients
  char *msg_log = "";
  char *msg;
  size_t len;
  
  if (client) {
    Serial.println("[WIFI] Client connected!");

    /*Mientras el cliente esté conectado*/
    while (client.connected()) {
      printLcd("CONECTADO!",2);
      len= client.available();
      if (len!=0) {//Msg recibido
        //msg = malloc(sizeof(*char)*len);
        msg = new char[len];
               
        Serial.println("len"+len);
        client.readBytes(msg, len);
        //msg_log = strcat("[TCP][IN] Message: ",  msg);
        //Serial.println(msg_log);
        Serial.println(msg);
        //printLcd(msg_log);
        client.write(msg,len);
        delete[] msg;

      //free(msg);
      }//Fin if mensaje recibido
    }//Fin while cliente conectado
    client.stop(); // close the connection
    Serial.println("[WIFI] Client Disconnected.");
    printLcd("DESCONECTADO :(",2);
  }

}


void printLcd(char* text, int size, bool clear, int x, int y) {
  if(clear)
    display.clearDisplay();
  display.setTextSize(size);
  display.setCursor(x,y);
  display.print(text);
  display.display(); 
}
