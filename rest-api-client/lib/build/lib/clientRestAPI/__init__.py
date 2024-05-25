import requests
from requests.auth import HTTPBasicAuth

class clientRestAPI:
    def __init__(this, api_url, user, password):
        this.api_url = api_url;
        this.user = user;
        this.password = password;
        
    def getdata(this):
        return (this._fetch_data_from_api())
    
    def deletedata(this):
        return (this._delete_data_from_api())
    
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
        
    def _delete_data_from_api(this):
        try:
            res = requests.delete(
                url=this.api_url, auth=HTTPBasicAuth(this.user, this.password)
            );
            status_code = res.status_code
            if(status_code == 200):
                content = res.text[:100]
            else:
                content = res.content.decode('utf-8')
            
            data = {
                "status-code": status_code,
                "content": content
            }
            
            return data;
        
        except (requests.exceptions.RequestException, requests.ConnectTimeout) as e:
            print("Error : ", e);
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
            