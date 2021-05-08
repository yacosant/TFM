#ifndef UTILS_H
#define UTILS_H
//Json
#include <ArduinoJson.h>

class Mensaje {  //Mover a encima
  private:
    short op;
    String origen;
    String destino;
    String msg;
    
    void print();
    
  public:
    Mensaje();
    //Mensaje(StaticJsonDocument<300> doc);
    void parse( StaticJsonDocument<300> doc);
    short getOp();
    String getOrigen();
    String getDestino();
    String getMsg();
};

#endif
