import requests, os, keyboard, json
from requests.auth import HTTPBasicAuth
from dotenv import load_dotenv, dotenv_values


def fetch_data_from_api_server(api_url, user, password):    
    try:
        res = requests.get(
            url = api_url,
            auth= HTTPBasicAuth(user, password)
        )
        
        data = res.json()
        return data
    
    except requests.exceptions.RequestException as e:
        print("Error fetching data", e)
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
        
    data = fetch_data_from_api_server(f"{url_api}/sensor/sensorid/1", user, password);
    
    if (data):
        for (item) in (data):
            print("ID\t\t : ", item["id"]);
            print("Sensor ID\t : ", item["sensorid"]);
            print("Value\t\t : ", item["value"]);
            print("Create Date\t : ", item["createDate"]);
            print("Update Date\t : ", item["updateDate"])
            print();
    else:
        print("Failed to fetch data from API server")