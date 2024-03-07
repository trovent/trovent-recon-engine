#!/usr/bin/env python3 
import requests, json, subprocess

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-d", "--domain", dest="domain", required=True)
args = parser.parse_args()

res={"results":[]}

try:
    with subprocess.Popen("/opt/trovent/trovent-recon-engine/dorkScanner.py -q 'intitle:Login inurl:{}' -e Google".format(args.domain), stdout=subprocess.PIPE, shell=True) as p:
      for line in p.stdout:
        if b"http" in line:
            res["results"].append(line.decode("utf-8"))
    with subprocess.Popen("/opt/trovent/trovent-recon-engine/dorkScanner.py -q 'intitle:Login inurl:{}' -e Google".format(args.domain), stdout=subprocess.PIPE, shell=True) as p:
      for line in p.stdout:
        if b"http" in line:
            res["results"].append(line.decode("utf-8"))
except Exception as e:
    print(e)

print(res)
