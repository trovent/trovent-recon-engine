#!/usr/bin/env python3

import sys,json,os
import subprocess

print(sys.argv[1])
j=json.loads(sys.argv[1])
print(j)

def latexBegin(f):
    f.write("""\section{Results}""")

# urls found through google dorks
def generateUrls(urls,f):
    conc="""\subsection{Public Admin or Login Pages}"""+"\n"
    conc+="""\\begin{itemize}"""+"\n"
    for url in urls:
        conc+="\item{%s}\n"%url
    conc+="""\end{itemize}"""
    conc+="""\\newpage"""
    f.write(conc)

def generateCensysPortsLink(ips,f):
    #censys="https://search.censys.io/hosts/217.160.0.10"
    conc="""\subsection{Centys Ports}"""+"\n"
    conc+="""\\begin{itemize}"""+"\n"
    for ip in ips:
        link="https://search.censys.io/hosts/%s"%(ip.strip())
        conc+="\item{\\url{%s}}\n"%link
    conc+="""\end{itemize}"""
    conc+="""\\newpage"""
    f.write(conc)

def generateCensysCertsLink(hashvalues,f):
    #censys_crt=https://search.censys.io/certificates/5dbf8f6b21d46301185bdd8b5d11d8f88c2c175fa6f14e236c9973f8af91d81c
    conc="""\subsection{Censys Certificates}"""+"\n"
    conc+="""\\begin{itemize}"""+"\n"
    for v in hashvalues:
        link="https://search.censys.io/certificates/?q=%s"%(v.strip())
        conc+="\item{\\url{%s}}\n"%link
    conc+="""\end{itemize}"""
    conc+="""\\newpage"""
    f.write(conc)

def generateRapiddnsLink(domain,f):
    #rapiddns="https://rapiddns.io/s/trovent.io"
    conc="""\subsection{Rapiddns}"""+"\n"
    conc+="""\\begin{itemize}"""+"\n"
    for d in domain:
        link="https://rapiddns.io/s/%s"%(d.strip())
        conc+="\item{\\url{%s}}\n"%link
    conc+="""\end{itemize}"""
    conc+="""\\newpage"""
    f.write(conc)

def generateCrtshLink(domain,f):
    #crtsh="https://crt.sh/?q=trovent.io"
    conc="""\subsection{Crtsh}"""+"\n"
    conc+="""\\begin{itemize}"""+"\n"
    for d in domain:
        link="https://crt.sh/?q=%s"%(d.strip())
        conc+="\item{\\url{%s}}\n"%link
    conc+="""\end{itemize}"""
    conc+="""\\newpage"""
    f.write(conc)

def generateEmailList(emails,f):
    conc="""\subsection{Emails}"""+"\n"
    conc+="""\\begin{itemize}"""+"\n"
    for email in emails:
        conc+="\item{%s}\n"%email
    conc+="""\end{itemize}"""
    conc+="""\\newpage"""
    f.write(conc)

def generatePorts(ports,f):
    conc="""\subsection{Ports}"""+"\n"
    conc+="""\\begin{itemize}"""+"\n"
    for ip,port in ports.items():
        conc+="\item{%s}\n"%(ip)
        conc+="""\\begin{itemize}"""+"\n"
        for p in list(port):
            conc+="\item{%s}\n"%p
        conc+="""\\end{itemize}"""
    conc+="""\end{itemize}"""
    conc+="""\\newpage"""
    f.write(conc)


def gen_report():
    subprocess.Popen("cp -r -t /opt/trovent/trovent-recon-engine/results /opt/trovent/trovent-recon-engine/workitems/reporter.d", stdout=subprocess.PIPE, universal_newlines=True, shell=True)
    process=subprocess.Popen("cd /opt/trovent/trovent-recon-engine/results/reporter.d && pdflatex maindocument.tex", stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True,shell=True)
    stdout, stderr = process.communicate()
    stdout, stderr

def main(json):
    target="/opt/trovent/trovent-recon-engine/workitems/reporter.d/results.tex"
    # create new file
    file=open(target,"w")
    file.close()
    # append to file
    file=open(target,"a")
    latexBegin(file)
    if ("ips" in json) and (json["ips"]):
        generateCensysPortsLink(json["ips"],file)
    if ("sha256list" in json) and (json["sha256list"]):
        generateCensysCertsLink(json["sha256list"],file)
    if ("sha1list" in json) and (json["sha1list"]):
        generateCensysCertsLink(json["sha1list"],file)
    if ("domains" in json) and (json["domains"]):
        generateRapiddnsLink(json["domains"],file)
    if ("emails" in json) and (json["emails"]):
        generateEmailList(json["emails"],file)
    if ("ports" in json) and (json["ports"]):
        generatePorts(json["ports"],file)
    if ("urls" in json) and (json["urls"]):
        generateUrls(json["urls"],file)
    file.close()
   
if __name__ == "__main__":
    main(j) 
    gen_report()
