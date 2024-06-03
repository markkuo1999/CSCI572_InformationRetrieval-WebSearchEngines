from bs4 import BeautifulSoup
from time import sleep
import requests
from random import randint
from html.parser import HTMLParser
import time
from urllib.parse import unquote
import json

USER_AGENT = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"}
file_path = './Google_Result2.json'
with open(file_path, 'r') as f:
    data = json.load(f)

Yahoo_data = data
#for i in range(len(yahoo_urls)):
     #yahoo_urls[i] = page

class SearchEngine:
    @staticmethod
    def search(query, new_page, sleep=False):
        if sleep: # Prevents loading too many pages too soon
            time.sleep(randint(10, 100))
        temp_url = '+'.join(query.split()) #for adding + between words for the query
        url = 'https://search.yahoo.com/search?p=' + temp_url + '&b=' + str(new_page)
        soup = BeautifulSoup(requests.get(url, headers=USER_AGENT).text, "html.parser")
        #print(soup)
        new_results = SearchEngine.scrape_search_result(soup)
        return new_results
    
    @staticmethod
    def scrape_search_result(soup):
        raw_results = soup.find_all("a", attrs = {"class" : "d-ib fz-20 lh-26 td-hu tc va-bot mxw-100p"})
        #print(raw_results)
        results = []
        #implement a check to get only 10 results and also check that URLs must not be duplicated
        for result in raw_results:
            link = result.get('href')
            decoded_url = unquote(link)
            start_index = decoded_url.find("RU=")
            end_index = decoded_url.find("/RK=", start_index)
            if start_index != -1 and end_index != -1:
                content = decoded_url[start_index + len("RU="):end_index]
                
            results.append(content)
            
        print(len(results))
        return results
    @staticmethod
    def check_duplicate(num_success_added_page, data, topic, added_page):
        arr = []
        for i in range(num_success_added_page):
            arr.append(data[topic][i])
            
        arr.append(added_page)
            
        arr_set = set(arr)      
        # 检查数组和集合的长度是否相等
        if len(arr) != len(arr_set):
            return True
        else:
            return False
        
        
num_link = 0
new_page = 1
curr = 0
num_run = 0
num_success_added_page = 0
count=0
#開始100個Query
for topic, urls in data.items():
      for i in range(len(urls)):
          Yahoo_data[topic][i] = ""
      #搜尋Page直到Page數=10或是已經搜尋了3次
      while num_success_added_page < 10 and num_run < 3:
          page = SearchEngine.search(topic, new_page)
          new_page += 1
          for i in range(len(page)):
              if curr > 9:
                  break 
              if SearchEngine.check_duplicate(num_success_added_page, Yahoo_data, topic, page[i]) == False: 
                  Yahoo_data[topic][curr] = page[i]
                  num_success_added_page += 1
                  curr += 1
          num_run += 1
          num_link += len(page)
          
      num_link = 0
      new_page = 1
      curr = 0
      num_success_added_page = 0
      num_run = 0
      print(topic)
      print(len(Yahoo_data[topic]))
      print(Yahoo_data[topic])
      
      

# 指定要写入的文件路径
output_file_path = "output2.json"
# 将数据写入到 JSON 文件中
with open(output_file_path, 'w') as f:
    json.dump(Yahoo_data, f, indent=4)  # indent=4 表示缩进4个空格，使得输出的JSON文件更易读