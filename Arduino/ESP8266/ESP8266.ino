// Khai Báo Thư Viện
#include <SoftwareSerial.h>
SoftwareSerial mySerial(4, 5); //D2, D1 = SRX, STX
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <Wire.h>
#include <ArduinoJson.h>


#if defined(ESP8266)
#include <pgmspace.h>
#else
#include <avr/pgmspace.h>
#endif

/*  Set the default condition for wifi
    HOST is the link in realtime database tab of firebase (NOTE: not the Firestore database)
    AUTH is the code in the Secret Database tab
    WIFI_SSID is the name of wifi that you want the nodemcu esp8266 to connect
    WIFI_PASSWORD is the password of wifi that you want the nodemcu esp8266 to connec
*/
#define FIREBASE_HOST "fire-alarm-version-13.firebaseio.com"
#define FIREBASE_AUTH "94Y9qBj4ddwUBWbwJkgyAMTbwq34EM6HQsddP5a6"
#define WIFI_SSID "Nokia 8.1"
#define WIFI_PASSWORD "12345678"
int a;
void setup()
{
    //Set the baud rate for the serial communication (mySerial is the serial communication with arduino mega)
    Serial.begin(9600);
    mySerial.begin(4800);

    //Check Serial port
    while (!Serial) continue;

    // connect to wifi.
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("connecting");
    while (WiFi.status() != WL_CONNECTED)
    {
        //Print "." if  the connect is not connected
        Serial.print(".");
        delay(500);
    }

    //Print "Connected" if  the connect is connected
    Serial.println();
    Serial.print("connected: ");
    Serial.println(WiFi.localIP());

    //Set the channel for the firebase communication
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

#define countof(a)(sizeof(a) / sizeof(a[0]))
void loop()
{

    //Declare the ArduiniJson in mega
    const size_t capacity = JSON_OBJECT_SIZE(3) + 30;
    DynamicJsonBuffer jsonBuffer(capacity);

    //Declare the data ArduiniJson in nodemcu ESP8266
    DynamicJsonBuffer json(200);
    JsonObject &root1 = json.createObject();
    JsonObject &room1 = root1.createNestedObject("phòng 234");
    JsonObject &room2 = root1.createNestedObject("phòng 235");

    // Test if parsing succeeds.


    //declare the json for the whole program
    int i;

    int ar1[19];
    int ar2[3];
    int b1;
    int b2;
    int data1; //store the data
    int data2;
    int data3;

    for (a = 0; a < 18; a++) {
        JsonObject &root = jsonBuffer.parseObject(mySerial);
        if (root == JsonObject::invalid()) {
            ar1[a] = 0;
            Serial.print(ar1[a]);
        } else {
            data2 = root["floorAddress"];
            ar1[a] = data2;
        }

        ar2[0] = 0;
        ar2[1] = 0;
        ar2[2] = 0;

        for (i = 0; i <= 9; i++) {

            b1 = ar1[i];
            if (b1 == 1) {
                ar2[0] = 1;
            }
            if (b1 == 2) {
                ar2[1] = 2;
            }
            if (b1 == 254) {
                ar2[2] = 254;
            }

        }

        // Khi không có phòng cháy
        if (ar2[0] == 0 && ar2[1] == 0) {
            Serial.println("Không có phòng cháy");

            //set the default value
            //room 234
            room1["floorAddress"] = 1;
            room1["roomAddress"] = 235;
            room1["alarm"] = 0;
            room1["wire_break"] = 0;
            room1["fault"] = 0;

            //room 235
            room2["floorAddress"] = 2;
            room2["roomAddress"] = 232;
            room2["alarm"] = 0;
            room2["wire_break"] = 0;
            room2["fault"] = 0;

            //upload data
            Firebase.set("list", root1);
            Firebase.set("status", 0);
        }

        // Khi phòng 2 cháy
        if (ar2[0] == 0 && ar2[2] == 0 ) {
            Serial.println("co 1 phong chay");
            Serial.println(ar2[1]);

            //room 234
            room1["floorAddress"] = 1;
            room1["roomAddress"] = 235;
            room1["alarm"] = 0;
            room1["wire_break"] = 0;
            room1["fault"] = 0;

            //room 235
            room2["floorAddress"] = 2;
            room2["roomAddress"] = 232;
            room2["alarm"] = 1;
            room2["wire_break"] = 0;
            room2["fault"] = 0;

            //upload data
            Firebase.set("list", root1);
            Firebase.set("status", 1);

        }

        // Khi phòng 2 cháy
        if (ar2[0] == 0 && ar2[2] == 254 && ar2[1] != 0 ) {
            Serial.println("co 1 phong chay");
            Serial.println(ar2[1]);

            //room 234
            room1["floorAddress"] = 1;
            room1["roomAddress"] = 235;
            room1["alarm"] = 0;
            room1["wire_break"] = 0;
            room1["fault"] = 0;

            //room 235
            room2["floorAddress"] = 2;
            room2["roomAddress"] = 232;
            room2["alarm"] = 1;
            room2["wire_break"] = 0;
            room2["fault"] = 0;

            //upload data
            Firebase.set("list", root1);
            Firebase.set("status", 1);

        }

        // Khi phòng 1 cháy
        if (ar2[1] == 0 && ar2[2] == 0 ) {
            Serial.println("co 1 phong chay");
            Serial.println(ar2[0]);

            //room 234
            room1["floorAddress"] = 1;
            room1["roomAddress"] = 235;
            room1["alarm"] = 1;
            room1["wire_break"] = 0;
            room1["fault"] = 0;

            //room 235
            room2["floorAddress"] = 2;
            room2["roomAddress"] = 232;
            room2["alarm"] = 0;
            room2["wire_break"] = 0;
            room2["fault"] = 0;

            //upload data
            Firebase.set("list", root1);
            Firebase.set("status", 1);
        }

        // Khi phòng 1 cháy
        if (ar2[1] == 0 && ar2[2] == 254 && ar2[0] != 0 ) {
            Serial.println("co 1 phong chay");
            Serial.println(ar2[0]);

            //room 234
            room1["floorAddress"] = 1;
            room1["roomAddress"] = 235;
            room1["alarm"] = 1;
            room1["wire_break"] = 0;
            room1["fault"] = 0;

            //room 235
            room2["floorAddress"] = 2;
            room2["roomAddress"] = 232;
            room2["alarm"] = 0;
            room2["wire_break"] = 0;
            room2["fault"] = 0;

            //upload data
            Firebase.set("list", root1);
            Firebase.set("status", 1);

        }

        // Khi cả 2 phòng cháy
        if ( ar2[0] < ar2[1] && ar2[0] != 0) {

            Serial.println("có 2 phòng cháy");
            Serial.println(ar2[0]);
            Serial.println(ar2[1]);
            Serial.println();

            //room 234
            room1["floorAddress"] = 1;
            room1["roomAddress"] = 235;
            room1["alarm"] = 1;
            room1["wire_break"] = 0;
            room1["fault"] = 0;

            //room 235
            room2["floorAddress"] = 2;
            room2["roomAddress"] = 232;
            room2["alarm"] = 1;
            room2["wire_break"] = 0;
            room2["fault"] = 0;

            //upload data
            Firebase.set("list", root1);
            Firebase.set("status", 1);


        }

        // Khi cả 2 phòng cháy
        if ( ar2[1] < ar2[0] && ar2[1] != 0) {

            Serial.println("có 2 phòng cháy");
            Serial.println(ar2[1]);
            Serial.println(ar2[0]);
            Serial.println();

            //room 234
            room1["floorAddress"] = 1;
            room1["roomAddress"] = 235;
            room1["alarm"] = 1;
            room1["wire_break"] = 0;
            room1["fault"] = 0;

            //room 235
            room2["floorAddress"] = 2;
            room2["roomAddress"] = 232;
            room2["alarm"] = 1;
            room2["wire_break"] = 0;
            room2["fault"] = 0;

            //upload data
            Firebase.set("list", root1);
            Firebase.set("status", 1);
        }
    }
}
