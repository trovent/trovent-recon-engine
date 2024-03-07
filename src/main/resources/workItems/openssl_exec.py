#!/usr/bin/env python3

import ssl,socket,hashlib,sys,json

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-t", "--targets", dest="targets", required=True)
parser.add_argument("-p", "--port", dest="port", required=True)

args = parser.parse_args()

results={"results":[]}

for t in args.targets.split(","):
    t=t.strip()
    try:
        s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        s.settimeout(1)
        ctx=ssl.create_default_context()
        wrapper=ctx.wrap_socket(s,server_hostname=t)
        wrapper.connect((t,int(args.port)))
        der_cert=wrapper.getpeercert(True)

        #Thumbprint
        sha1=hashlib.sha1(der_cert).hexdigest()
        sha256=hashlib.sha256(der_cert).hexdigest()
        fingerprint={}
        fingerprint["sha1"]   = sha1
        fingerprint["sha256"] = sha256
        domain={}
        domain["domain"]=t
        domain["fingerprint"]=fingerprint
        results["results"].append(domain)
        wrapper.close()
    except Exception as e:
        print(e)
        response=False

print(json.dumps(results))
