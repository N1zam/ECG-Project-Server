import requests, os, keyboard, json
from time import sleep
from requests.auth import HTTPBasicAuth
from dotenv import load_dotenv, dotenv_values

class clientRestAPI:
    def __init__(this, api_url, user, password):
        this.api_url = api_url;
        this.user = user;
        this.password = password;
        
    def getdata(this):
        return (this._fetch_data_from_api())
    
    def _fetch_data_from_api(this):
        try:
            res = requests.get(
                url=this.api_url, auth=HTTPBasicAuth(this.user, this.password)
            );
            data = res.json();
            return data;
        except (requests.exceptions.RequestException, requests.ConnectTimeout) as e:
            print("Error: ", e);
            return None;
        
class File:
    def __init__(this, nameFile: str):
        this.namefile = nameFile;
        
    def read(this):
        try:
            with open(this.namefile, "r", encoding="utf-8") as file:
                data = [data.strip() for data in file.readlines()]
                file.close();
            return data;

        
        except Exception as e:
            print("Error: ", e);
    
    def save(this, value):
        try:
            with open(this.namefile, "w", encoding="utf-8") as file:
                if(isinstance(value, list)):
                    for i in value:
                        file.write(str(i)+"\n");
                else:
                    file.write(f"{value:.1f}\n");
                    
                file.close();
                
        except Exception as e:
            print("Error: ", e);
            
            

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
        