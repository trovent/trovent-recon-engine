#!/usr/bin/env python3

# pip install https://github.com/PaulSec/crt.sh
import sys,json,os

sys.path.append(os.path.join(os.path.dirname(__file__), '/', 'lib'))

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-t", "--target", dest="target", required=True)

args = parser.parse_args()

from lib.crtsh import crtshAPI

domains=[]
try:
    res=crtshAPI().search(args.target)
    results_set={"results":[]}
    for r in res:
        common_name=r["common_name"]
        domains.append(common_name)
    for d in set(domains):
        # skip domains with wildcard
        if d is not None:
            if d.find("*"): 
                results_set["results"].append(d)
    print(json.dumps(results_set))
except Exception as e:
    print(e)

