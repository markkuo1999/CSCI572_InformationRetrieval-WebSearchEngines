import csv

unique_cells = set()  
row_count = 0

# 打開CSV文件
with open('./urls_latimes.csv', newline='') as csvfile:
    csvreader = csv.reader(csvfile)
    next(csvreader)  
    
    
    for row in csvreader:
        row_count += 1
        cell_value = row[0]  
        unique_cells.add(cell_value) 

print("Total URLs extracted:", row_count-1)

num_unique_cells = len(unique_cells)
print("# unique URLs extracted:", num_unique_cells-1)

num_matching_cells = 0
prefixes = ["http://www.latimes.com", "https://www.latimes.com", "http://latimes.com", "https://latimes.com"]

for cell in unique_cells:
    for prefix in prefixes:
        if cell.startswith(prefix):
            num_matching_cells += 1
            break  # 如果找到符合的前綴，跳出內部循環

print("# unique URLs within News Site", num_matching_cells)
print("\n")

value_counts = {}

with open('./fetch_latimes.csv', newline='') as csvfile:
    csvreader = csv.reader(csvfile)
    for row in csvreader:
        if len(row) > 1:
            value = row[1]
            value_counts[value] = value_counts.get(value, 0) + 1


for value, count in value_counts.items():
    print(f"{value}: {count}")
  
print("\n")
   
count_less_than_1024 = 0
b = 0
c = 0
d = 0
e = 0
count = 0
with open('./visit_latimes.csv', newline='') as csvfile:
    csvreader = csv.reader(csvfile)
    for row in csvreader:
        try:
            value = int(row[1]) 
            if value < 1024:
                count_less_than_1024 += 1
            elif 1024 <= value < 10240:
                b += 1
            elif 10240 <= value < 102400:
                c += 1
            elif 102400 <= value < 1048576:
                d += 1
            elif value >= 1048576:
                e += 1
        except ValueError:
            pass  

print(f"< 1KB： {count_less_than_1024}")
print(f"1KB ~ <10KB： {b}")
print(f"10KB ~ <100KB： {c}")
print(f"100KB ~ <1MB： {d}")
print(f">= 1MB： {e}")
print("\n")

key_counts = {}


with open('./visit_latimes.csv', newline='') as csvfile:
    csvreader = csv.reader(csvfile)
    
    
    for row in csvreader:
        key = row[3]  
        if key in key_counts:
            key_counts[key] += 1
        else:
            key_counts[key] = 1

for key, count in key_counts.items():
    print(f"{key}: {count}")