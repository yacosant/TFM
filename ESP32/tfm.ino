#include <splash.h>

//incluye mis utilidades
#include "Utils.h"

#include <WiFi.h>

#include <WiFiClient.h>
#include <WiFiAP.h>

//Libraries for LoRa
#include <SPI.h>
#include <LoRa.h>

//Json
#include <ArduinoJson.h>

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

#define jsonFlag 1

#define BAND 866E6

// Set these to your desired credentials.
char *ssid = "AP2";
const char *password = "yaco123456789";
String qr="";
String userName = "";

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

Mensaje msgObj;

void setup() {
  // put your setup code here, to run once:
  setup_Serial();
  setup_Display();
  setup_Lora();
  setup_Wifi();
  //printLcd("RUNING");
  drawQR();
  printLcd(ssid,1,false,SCREEN_WIDTH/2,0);
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
        
        Serial.println("[TCP][IN] Mensaje:");
        Serial.print(msg);
       if(jsonFlag)
        deserializeObject(String(msg));
        if(msgObj.getOp() == 1){// msg configuracion
          Serial.println("MSG de configuracion");
        }
         else if(msgObj.getOp() == 0){ //msg normal
          Serial.println("MSG de msg");
         }
         else if(msgObj.getOp() == 2){//ack - no usado
          Serial.println("MSG de ACK");
         }
         else if(msgObj.getOp() == 3){//Hello
          Serial.println("MSG de Hello");
          userName=msgObj.getOrigen();
         }
         else if(msgObj.getOp() == 4){//Bye
          Serial.println("MSG de Bye");
         }
         
        /* 
        printLcd("[TCP] Recibido: ",1,true,0,30);
        printLcd(msg,1,false,0,40);
        */
        
        if(msgObj.getOp() != 1){
          //lcdMsg = "[Enviando] "+msgObj.getDestino()+" :";
          printLcd("[Enviando] ",1,true,0,30);
          printLcd(msg,1,false,0,40);
          //Send LoRa packet to receiver
          LoRa.beginPacket();
          LoRa.print(msg);
          LoRa.endPacket();
        }
        
        //client.write(msg,len);//echo
        delete[] msg;

      }//Fin if mensaje recibido
      if (userName!="" && lenLora!=0) {//Msg recibido por LORA
        Serial.println("[LORA][IN] Mensaje:");

        while (LoRa.available()) {
          msgLoraStr = LoRa.readString();
          Serial.print(msgLoraStr.c_str());
        }
        
        //Deserializar y si es para mi, lo pinto y reenvio al dispositivo
        if(jsonFlag)
          deserializeObject(msgLoraStr);
          if(msgObj.getDestino() == userName || msgObj.getOp()==3 || msgObj.getOp()==4){
            Serial.println("Es para ti este mensaje");
            //---------------------
            rssi = LoRa.packetRssi();
            Serial.print(" with RSSI ");    
            Serial.println(rssi);
            msgLoraStr=msgLoraStr+"\n";
            msgLora = const_cast<char*>(msgLoraStr.c_str());
            //lcdMsg = "[Recibido] De "+msgObj.getOrigen()+" : ";
            printLcd("[Recibido] ",1,true,0,30);
            printLcd(msgLora,1,false,0,40);
    
            client.print(msgLora);
        }
      }

      
    }//Fin while cliente conectado
    client.stop(); // close the connection
    Serial.println("[WIFI] Client Disconnected.");
    printLcd("DESCONECTADO",2);
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
  String s= ssid;
  String p= password;
  qr="WIFI:S:"+s+";T:WPA;P:"+p+";;";
  
  qrcode_initText(&qrcode, qrcodeData, QRcode_Version, QRcode_ECC, qr.c_str());
  uint16_t x0 = 0;
  uint16_t y0 = 0;

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

}

void deserializeObject(String json){
    //json = "{\"op\":0,\"o\":\"yaco\",\"d\":\"destino1\",\"msg\":\"mensajePrueba\"}";
    StaticJsonDocument<300> doc;
    DeserializationError error = deserializeJson(doc, json);
    if (error) { return; }
    msgObj.parse(doc);  
}
