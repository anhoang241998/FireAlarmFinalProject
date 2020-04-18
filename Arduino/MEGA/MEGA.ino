#include <Eeprom24Cxx.h>
#include <ArduinoJson.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
LiquidCrystal_I2C lcd(0x27, 20, 4);

int a, b, c, d;
int A, B, C, D, E;
int a1, a2;
int I, i, y1, y2, y3, y4;
byte s, s1, s2;
byte ar[399];
int arr[10];
byte second, minute, hour, day, wday, month, year;
static Eeprom24C eeprom(32, 0x50);
void setup()
{
    // put your setup code here, to run once:
    Wire.begin();//Khoi tao thu vien Wire
    Serial.begin(9600);

    //setTime(15,24,30,5,9,1,20);
    Serial1.begin(9600);//Khoi tao cong RS485
    Serial2.begin(4800);//Khoi tao cong Uart dan truyen den ESP8266
    // doc thoi gian thuc

    lcd.init();                      // initialize the lcd
    //lcd.init();
    // Print a message to the LCD.
    lcd.backlight();
}

void loop()
{
    DynamicJsonBuffer jsonBuffer(200);
    JsonObject&root = jsonBuffer.createObject();
    arr[0] = 0;
    // put your main code here, to run repeatedly:
    // doc thoi gian thuc

    Wire.beginTransmission(0x68);//Bat dau viec truyen tai DS3231
    Wire.write(byte(0x00));//Thu thap tu cac thanh ghi
    Wire.endTransmission();// Ket thuc viec truyen du lieu
    Wire.requestFrom(0x68, 7); //Yeu cau truyen 7 byte tu cac thanh ghi
    while (Serial1.available() > 0)
    {
        while (Wire.available())
        {
            second = bcd2dec(Wire.read() & 0x7f);

            minute = bcd2dec(Wire.read());

            hour = bcd2dec(Wire.read() & 0x3f);

            wday = bcd2dec(Wire.read());

            day = bcd2dec(Wire.read());

            month = bcd2dec(Wire.read());

            year = bcd2dec(Wire.read());
            //year += 2000;

            delay(10);


            lcd.setCursor(3, 0);
            lcd.print("STATE");
            lcd.setCursor(0, 1);
            lcd.print("  ");
            lcd.setCursor(0, 1);
            lcd.print(hour);
            lcd.setCursor(2, 1);
            lcd.print(":");
            lcd.setCursor(3, 1);
            lcd.print("  ");
            lcd.setCursor(3, 1);
            lcd.print(minute);
            lcd.setCursor(5, 1);
            lcd.print(":");
            lcd.setCursor(6, 1);
            lcd.print("  ");
            lcd.setCursor(6, 1);
            lcd.print(second);
            lcd.setCursor(9, 1);
            lcd.print("  ");
            lcd.setCursor(9, 1);
            lcd.print(day);
            lcd.setCursor(11, 1);
            lcd.print("/");
            lcd.setCursor(13, 1);
            lcd.print("  ");
            lcd.setCursor(13, 1);
            lcd.print(month);
            lcd.setCursor(14, 1);
            lcd.print("/");
            lcd.setCursor(16, 1);
            lcd.print("    ");
            lcd.setCursor(16, 1);
            lcd.print(year + 2000);
            lcd.setCursor(2, 2);
            lcd.print("FI");
            lcd.setCursor(6, 2);
            lcd.print("WB");
            lcd.setCursor(11, 2);
            lcd.print("FA");
            //Serial.print(second);
            //Serial.println();
            delay(10);
        }


        // doc du lieu vao chuoi ar


        for (I = 1; I <= 8; I++)
        {
            A = Serial1.read();
            if (A == 255)
            {
             break;
            }  
        }     
                for (i = 0; i <= 399; i++)
                {
                    ar[i] = Serial1.read();//Doc du lieu tu cac Detector vao chuoi ar
                    delay(10);
                }
            //}
        //}     lcd.setCursor(8, 2);
        lcd.print("  ");
        lcd.setCursor(8, 2);
        lcd.print(0);
        //Dat so lan WB FI ve 0
        C = 0;
        D = 0;
        E = 0;
        lcd.setCursor(8, 2);
        lcd.print("  ");
        lcd.setCursor(8, 2);
        lcd.print(C);
        lcd.setCursor(4, 2);
        lcd.print("  ");
        lcd.setCursor(4, 2);
        lcd.print(D);
        lcd.setCursor(13, 2);
        lcd.print("  ");
        lcd.setCursor(13, 2);
        lcd.print(E);
        delay(100);
    }
    // Doc trang thai DETECTOR
    for (y1 = 0; y1 <= 396; y1 = y1 + 4)
    {
        s = ar[y1];
        s1 = ar[y1 + 1];
        s2 = ar[y1 + 2];
        //Xac nhan FI vao arr[0]
        if (s == 251 && (s1 >= 0 && s1 < 250) && (s2 >= 0 && s2 < 250))
        {
            arr[0] = 1;
        }
    }
    b = arr[0];
    //Goi root json 254 ve ESP8266
    if (b == 0)
    {
        root["State"] = 254;
        root["floorAddress"] = 254;
        root["roomAddress"] = 254;
        root.printTo(Serial2);
    }
    // Doc trang thai DETECTOR
    for (y1 = 0; y1 <= 396; y1 = y1 + 4)
    {
        s = ar[y1];
        s1 = ar[y1 + 1];
        s2 = ar[y1 + 2];
        if (s == 250 && (s1 >= 0 && s1 < 250) && (s2 >= 0 && s2 < 250))
        {
            lcd.setCursor(9, 0);
            lcd.print("  ");
            lcd.setCursor(9, 0);
            lcd.print("NO");//TrangThai
            lcd.setCursor(12, 0);
            lcd.print("  ");
            //lcd.setCursor(12,0);
            //lcd.print(ar[y1+1]);//lau
            lcd.setCursor(14, 0);
            lcd.print("   ");
            //lcd.setCursor(14,0);
            //lcd.print(ar[y1+2]);//phong
            lcd.setCursor(5, 3);
            lcd.print("     ");
            lcd.setCursor(5, 3);
            lcd.print(y1);

            delay(100);

        }

        if (s == 251 && (s1 >= 0 && s1 < 250) && (s2 >= 0 && s2 < 250))
        {
            // goi du lieu FI  ra ESP8266

            root["State"] = ar[y1];
            root["floorAddress"] = ar[y1 + 1];
            root["roomAddress"] = ar[y1 + 1];
            root.printTo(Serial2);

            lcd.setCursor(9, 0);
            lcd.print("  ");
            lcd.setCursor(9, 0);
            lcd.print("FI");//TrangThai
            lcd.setCursor(12, 0);
            lcd.print("  ");
            lcd.setCursor(12, 0);
            lcd.print(ar[y1 + 1]); //lau
            lcd.setCursor(14, 0);
            lcd.print("   ");
            lcd.setCursor(14, 0);
            lcd.print(ar[y1 + 2]); //phong
            lcd.setCursor(5, 3);
            lcd.print("     ");
            lcd.setCursor(5, 3);
            lcd.print(y1);
            lcd.setCursor(4, 2);
            lcd.print("  ");
            lcd.setCursor(4, 2);
            lcd.print("Y");
            delay(1000);

        }
        //du lieu FA
        if (s == 252 && (s1 >= 0 && s1 < 250) && (s2 >= 0 && s2 < 250))
        {
            lcd.setCursor(9, 0);
            lcd.print("  ");
            lcd.setCursor(9, 0);
            lcd.print("FA");//TrangThai
            lcd.setCursor(12, 0);
            lcd.print("  ");
            lcd.setCursor(12, 0);
            lcd.print(ar[y1 + 1]); //lau
            lcd.setCursor(14, 0);
            lcd.print("   ");
            lcd.setCursor(14, 0);
            lcd.print(ar[y1 + 2]); //phong
            lcd.setCursor(5, 3);
            lcd.print("     ");
            lcd.setCursor(5, 3);
            lcd.print(y1);
            lcd.setCursor(13, 2);
            lcd.print("  ");
            lcd.setCursor(13, 2);
            lcd.print("Y");
            delay(100);

        }
        // goi du lieu WB  ra ESP8266
        if (s == 253 && (s1 >= 0 && s1 < 250) && (s2 >= 0 && s2 < 250))
        {

            //root["State"] = ar[y1];
            //root["floorAddress"] = ar[y1+1];
            //root["roomAddress"] = ar[y1+1];

            lcd.setCursor(9, 0);
            lcd.print("  ");
            lcd.setCursor(9, 0);
            lcd.print("WB");//TrangThai
            lcd.setCursor(12, 0);
            lcd.print("  ");
            lcd.setCursor(12, 0);
            lcd.print(ar[y1 + 1]); //lau
            lcd.setCursor(14, 0);
            lcd.print("   ");
            lcd.setCursor(14, 0);
            lcd.print(ar[y1 + 2]); //phong
            lcd.setCursor(5, 3);
            lcd.print("     ");
            lcd.setCursor(5, 3);
            lcd.print(y1);
            lcd.setCursor(8, 2);
            lcd.print("  ");
            lcd.setCursor(8, 2);
            lcd.print("Y");
            delay(100);

        }

    }

}
// Ham cai dat thoi gian cho DS3231
void setTime(byte hr, byte min , byte sec, byte wd, byte day, byte mth, byte yr)
{
    Wire.beginTransmission(0x68);
    Wire.write(byte(0x00));
    Wire.write(dec2bcd(sec));
    Wire.write(dec2bcd(min));
    Wire.write(dec2bcd(hr));
    Wire.write(dec2bcd(wd));
    Wire.write(dec2bcd(day));
    Wire.write(dec2bcd(mth));
    Wire.write(dec2bcd(yr));
    Wire.endTransmission();
}
// Ham doi so thap phan ra so nhi phan
byte dec2bcd(int num)
{
    return ((num / 10 * 16) + (num % 10));
}
// Ham doi so nhi phan ra so thap phan
int bcd2dec(byte num)
{
    return ((num / 16 * 10) + (num % 16));
}
