
#include "Utils.h"

//---------------------------------

    void Mensaje::parse( StaticJsonDocument<300> doc){
      this->op = doc["op"].as<short>();
      this->origen = doc["o"].as<String>();
      this->destino = doc["d"].as<String>();
      this->msg = doc["msg"].as<String>();
      Mensaje::print();
    }

    void Mensaje::print(){
      Serial.println(this->op);
      Serial.println(this->origen);
      Serial.println(this->destino);
      Serial.println(this->msg);
    }

    Mensaje::Mensaje() { }
    
    /*Mensaje::Mensaje( StaticJsonDocument<300> doc) {
      parse(doc);
      //init();
      print();
    }*/

    short  Mensaje::getOp(){ return this->op;}
    String Mensaje::getOrigen(){ return this->origen;}
    String Mensaje::getDestino(){ return this->destino;}
    String Mensaje::getMsg(){ return this->msg;}
    
    

//----------------------------------
