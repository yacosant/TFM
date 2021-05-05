#include <splash.h>

#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiAP.h>

//Libraries for LoRa
#include <SPI.h>
#include <LoRa.h>

//Libraries for OLED Display
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include "qrcode.h"
QRCode qrcode;                  // Create the QR code

//OLED pins
#define OLED_SDA 4
#define OLED_SCL 15
#define OLED_RST 16
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels
//LoRa pins
#define SCK 5
#define MISO 19
#define MOSI 27
#define SS 18
#define RST 14
#define DIO0 26

#define BAND 866E6

// Set these to your desired credentials.
const char *ssid = "AP";
const char *password = "yaco123456789";


const int QRcode_Version = 3;   //  set the version (range 1->40)
const int QRcode_ECC = 0;       //  set the Error Correction level (range 0-3) or symbolic (ECC_LOW, ECC_MEDIUM, ECC_QUARTILE and ECC_HIGH)
#define _QR_doubleSize    //
#define Lcd_X  128
#define Lcd_Y  64
uint8_t qrcodeData[106];

//definition funtion
void printLcd(char* text, int size=2, bool clear=true, int x=0, int y=0);

// Display
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RST);

WiFiServer server(80,1);

void setup() {
  // put your setup code here, to run once:
  setup_Serial();
  setup_Display();
  setup_Lora();
  setup_Wifi();
  //printLcd("RUNING");
  drawQR();
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
  //Serial.print("[WIFI] AP IP address: ");
  //Serial.println(myIP);
  server.begin();
  Serial.println("[WIFI] Wifi Access Point started!");
}

void setup_Lora() {
  //SPI LoRa pins
  SPI.begin(SCK, MISO, MOSI, SS);
  //setup LoRa transceiver module
  LoRa.setPins(SS, RST, DIO0);
  
  if (!LoRa.begin(BAND)) {
    Serial.println("[LORA] Starting failed!");
    while (1);
  }
  Serial.println("[LORA] Initializing OK!");
}

void loop() {
  // put your main code here, to run repeatedly:
  //printLcd("LISTO!",2);
  //printLcd("LISfssfTO!",2);
  //printLcd("",2);
  
  //printLcd((char *)WiFi.gatewayIP().toString().c_str(),1, false, 0, 30);
  WiFiClient client = server.available();   // listen for incoming clients
  char *msg_log = "";
  char *msg;
  size_t len;
  int lenLora;
  String msgLoraStr;
  char *msgLora;
  int rssi;
  
  if (client) {
    Serial.println("[WIFI] Client connected!");
    printLcd("CONECTADO",2);
    /*Mientras el cliente esté conectado*/
    while (client.connected()) {
      
    
      printLcd("CONECTADO",2,false);
      lenLora = LoRa.parsePacket();
      len= client.available();
      
      if (len!=0) {//Msg recibido por WIFI
        msg = new char[len+1];
               
        client.readBytes(msg, len);
        //msg_log = strcat("[TCP][IN] Message: ",  msg);
        //Serial.println(msg_log);
        Serial.println("[TCP][IN] Mensaje:");
        Serial.print(msg);
       
        printLcd("[TCP] Recibido: ",1,true,0,30);
        printLcd(msg,1,false,0,40);
        
        //Send LoRa packet to receiver
        LoRa.beginPacket();
        LoRa.print(msg);
        LoRa.endPacket();
        
        client.write(msg,len);
        delete[] msg;

      }//Fin if mensaje recibido
      if (lenLora!=0) {//Msg recibido por LORA
        Serial.println("[LORA][IN] Mensaje:");

        while (LoRa.available()) {
          msgLoraStr = LoRa.readString();
          Serial.print(msgLoraStr.c_str());
        }

        rssi = LoRa.packetRssi();
        Serial.print(" with RSSI ");    
        Serial.println(rssi);
        msgLoraStr=msgLoraStr+"\n";
        msgLora = const_cast<char*>(msgLoraStr.c_str());

        printLcd("[LORA] Recibido: ",1,true,0,30);
        printLcd(msgLora,1,false,0,40);

        client.print(msgLora);
      }

      
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


void drawQR(){
  display.clearDisplay();
  display.setCursor(0,0);
  qrcode_initText(&qrcode, qrcodeData, QRcode_Version, QRcode_ECC, "https://github.com/");
  Serial.print("in");
Serial.print( qrcode.size);
  uint16_t x0 = 0;
  uint16_t y0 =  0;   //

  uint16_t xx;
  uint16_t yy;
  

  //--------------------------------------------
  //display QRcode
  for (uint8_t y = 0; y < qrcode.size; y++) {
    for (uint8_t x = 0; x < qrcode.size; x++) {  
      xx=x;
      yy=y;
      if (qrcode_getModule(&qrcode, x, y) == 0) {
        
        //display.drawPixel(xx,yy,1);
        
        display.drawPixel(2 * xx,2 * yy,1);
        display.drawPixel(2 * xx + 1, 2 * yy,1);
        display.drawPixel(2 * xx, 2 * yy + 1,1);
        display.drawPixel(2 * xx + 1,2 * yy + 1,1);
      }
      else{
        
         //display.drawPixel(xx,yy,0);

        display.drawPixel(2 * xx,2 * yy,0);
        display.drawPixel(2 * xx + 1, 2 * yy,0);
        display.drawPixel(2 * xx, 2 * yy + 1,0);
        display.drawPixel(2 * xx + 1,2 * yy + 1,0);
      }
    }
  }
  display.display(); 
  Serial.print("out");

}
