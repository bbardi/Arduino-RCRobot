#include <SoftwareSerial.h>
#include <AFMotor.h>

SoftwareSerial SoftSerial(A4,A5); // RX | TX pins.  Can be reassigned to other pins if needed
AF_DCMotor motor1(1);
AF_DCMotor motor2(2);
AF_DCMotor motor3(3);
AF_DCMotor motor4(4);

 
const long BAUDRATE = 9600;    // This is the default communication baud rate of the HC-05 module
//===============================================================================
//  Initialization
//===============================================================================
void setup() 
{
    SoftSerial.begin(BAUDRATE);  // Init soft serial object
    motor1.setSpeed(254);
    motor1.run(RELEASE);
    motor2.setSpeed(254);
    motor2.run(RELEASE);
    motor3.setSpeed(254);
    motor3.run(RELEASE);
    motor4.setSpeed(254);
    motor4.run(RELEASE);
    Serial.begin(9600);
}
//===============================================================================
//  Main
//=============================================================================== 
void loop()
{
     // Read from the Bluetooth module and send to the Arduino Serial Monitor
    if (SoftSerial.available())
    {
      char a = SoftSerial.read();
      Serial.println(a);
      if(a == 'u'){
        motor1.run(FORWARD);
        motor2.run(FORWARD);
        motor3.run(FORWARD);
        motor4.run(FORWARD);
        delay(400);
        motor1.run(RELEASE);
        motor2.run(RELEASE);
        motor3.run(RELEASE);
        motor4.run(RELEASE);
      }
      if(a == 'd'){
        motor1.run(BACKWARD);
        motor2.run(BACKWARD);
        motor3.run(BACKWARD);
        motor4.run(BACKWARD);
        delay(400);
        motor1.run(RELEASE);
        motor2.run(RELEASE);
        motor3.run(RELEASE);
        motor4.run(RELEASE);
      }
      if(a == 'r'){
        motor1.run(FORWARD);
        motor2.run(FORWARD);
        motor3.run(BACKWARD);
        motor4.run(BACKWARD);
        delay(200);
        motor1.run(RELEASE);
        motor2.run(RELEASE);
        motor3.run(RELEASE);
        motor4.run(RELEASE);
      }
      if(a == 'l'){
        motor1.run(BACKWARD);
        motor2.run(BACKWARD);
        motor3.run(FORWARD);
        motor4.run(FORWARD);
        delay(200);
        motor1.run(RELEASE);
        motor2.run(RELEASE);
        motor3.run(RELEASE);
        motor4.run(RELEASE);
      }
    }
}
