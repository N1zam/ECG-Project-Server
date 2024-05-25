import requests, os, keyboard, json
from clientRestAPI import clientRestAPI, File
from time import sleep
from requests.auth import HTTPBasicAuth
from dotenv import load_dotenv, dotenv_values

def main():    
    user = os.getenv("USER");
    password = os.getenv("PASSWORD");
    url_api = os.getenv("URL");

    if(user == None or password == None):
        load_dotenv();
        user = os.getenv("USER");
        password = os.getenv("PASSWORD");
        url_api = os.getenv("URL");
    
    filesensor = "./result/Sensor.txt";
    filelogtime = "./result/logtime.txt";
    data = clientRestAPI(f"{url_api}/sensor/sensorid/4", user, password);
    
    if (data.getdata()):
        value = [];
        for (item) in (data.getdata()):
            value.append(item["value"]);
            print("ID\t\t : ", item["id"]);
            print("Sensor ID\t : ", item["sensorid"]);
            print("Value\t\t : ", item["value"]);
            print("Create Date\t : ", item["createDate"]);
            print("Update Date\t : ", item["updateDate"]);
            print();
                        
        File(filesensor).save(value);

    else:
        print("Failed to fetch data from API server");
        

def test():
    filename = "./result/Sensor.txt";
    data = File(filename).read();
    for i in data:
        print(i);
    
if (__name__ == "__main__"):
    os.system("cls") if (os.name == "nt") else os.system("clear");
    # main();
    # test();
    
    while(True):
        if(keyboard.is_pressed('q')): break;
        main();
        sleep(1);
        