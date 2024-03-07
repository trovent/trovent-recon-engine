#!/usr/bin/env python3 

import json

from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument("-j", "--json", dest="input", required=True)

args = parser.parse_args()

data = json.loads(args.input)
