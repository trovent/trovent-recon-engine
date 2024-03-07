#!/usr/bin/env python3

import sys,os,re,json

domain=sys.argv[1]
run=os.system("amass enum -ip -json - -d " + domain)

names={}
ips={}

for line in run:
    js=json.loads(line)
    names.add(js["name"])
    for addr in js["addresses"]:
        ips.add(addr["ip"])

print(names)
print(ips)
