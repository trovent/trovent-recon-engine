#!/usr/bin/env python3 
import requests, json

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-d", "--domain", dest="domain", required=True)
parser.add_argument("-t", "--token", dest="token", required=True)

args = parser.parse_args()

url="https://api.hunter.io/v2/domain-search?domain="
target=url+args.domain+"&api_key="+args.token
res={"results":[]}
r=requests.get(target)
js=json.loads(r.text)
print(js)
for email in js["data"]["emails"]:
    res["results"].append(email["value"])

print(res)
