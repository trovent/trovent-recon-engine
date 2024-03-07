#!/usr/bin/env python3

import sys,json,os

sys.path.append(os.path.join(os.path.dirname(__file__), '/', 'lib'))

from lib.DNSDumpsterAPI import DNSDumpsterAPI

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-t", "--target", dest="target", required=True)

args = parser.parse_args()

res=DNSDumpsterAPI().search(args.target)
records=res['dns_records']

dns=records['dns']
mx=records['mx']
txt=records['txt']
host=records['host']

results_set=[]    
results={"results":[]}

for item in host:
    results_set.append(item["domain"])

for res in set(results_set):
    results["results"].append(res) 

print(json.dumps(results))
