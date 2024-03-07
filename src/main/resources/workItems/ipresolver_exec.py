#!/usr/bin/env python3

# pip install dnspython3
import dns,json,sys
from dns import resolver

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-d", "--domains", dest="domains", required=True)

args = parser.parse_args()

results={"results":[]}

for t in args.domains.split(","):
    if t.count("*") == 0:
        dns_ip={}
        try:
            ipval_arr=[]
            for ipval in resolver.query(t,"A"):
                ipval_arr.append(ipval.to_text())
        except dns.exception.DNSException as e:
            continue
        dns_ip["domain"]=t
        dns_ip["ips"]=ipval_arr
        results["results"].append(dns_ip)

print(results)
