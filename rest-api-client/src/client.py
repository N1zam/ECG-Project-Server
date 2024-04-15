import requests, os, keyboard, json
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
    
    
if __name__ == "__main__":
    os.system("cls") if (os.name == "nt") else os.system("clear");
    
    user = os.getenv("USER");
    password = os.getenv("PASSWORD");
    url_api = os.getenv("URL");

    if(user == None or password == None):
        load_dotenv()
        user = os.getenv("USER");
        password = os.getenv("PASSWORD");
        url_api = os.getenv("URL");
        
    data = clientRestAPI(f"{url_api}/sensor/sensorid/1", user, password)
    
    if (data.getdata()):
        for (item) in (data.getdata()):
            print("ID\t\t : ", item["id"]);
            print("Sensor ID\t : ", item["sensorid"]);
            print("Value\t\t : ", item["value"]);
            print("Create Date\t : ", item["createDate"]);
            print("Update Date\t : ", item["updateDate"])
            print();
    else:
        print("Failed to fetch data from API server")