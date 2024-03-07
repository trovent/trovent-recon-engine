#!/usr/bin/env python3 

import subprocess as sp
import sys,json

target=sys.argv[1]
wordlist="/usr/share/wordlists/Discovery/DNS/subdomains-top1million-5000.txt"
results={"results":[]}
res=[]

output=sp.getoutput("gobuster dns -d %s -w %s 2>/dev/null | grep Found | awk -F: '{print $2}'"%(target,wordlist)).splitlines()

for line in output:
    res.append(line.lower().strip()) 

for s in set(res):
    results["results"].append(s)

print(json.dumps(results))
