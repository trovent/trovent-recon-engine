#!/usr/bin/env python3 

import sys

# pip install python-nmap
import nmap

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-t", "--targets", dest="targets", required=True)

args = parser.parse_args()

ports_100="7,9,13,21-23,25-26,37,53,79-81,88,106,110-111,113,119,135,139,143-144,179,199,389,427,443-445,465,513-515,543-544,548,554,587,631,646,873,990,993,995,1025-1029,1110,1433,1720,1723,1755,1900,2000-2001,2049,2121,2717,3000,3128,3306,3389,3986,4899,5000,5009,5051,5060,5101,5190,5357,5432,5631,5666,5800,5900,6000-6001,6646,7070,8000,8008-8009,8080-8081,8443,8888,9100,9999-10000,32768,49152-49157"
ports_50="30,21-23,25,53,80-81,110-111,113,135,139,143,199,443,445,465,548,587,993,995,1025,3306,3389,4444,8080,8888,8443,9443"

target_ports=ports_50

results={"results":[]}

nm=nmap.PortScanner()

targets=set(args.targets.split(","))
nm.scan(" ".join(targets),target_ports)

for host in nm.all_hosts():
    ports_list=[]
    ports=nm[host]["tcp"].keys()
    res={}
    for port in ports:
        if nm[host]["tcp"][port]['state'] == "open":
            ports_list.append(port)
    res["ip"] = host
    res["ports"] = ports_list
    results["results"].append(res)

print(results)
