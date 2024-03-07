FROM fedora:latest

RUN yum update -y 

RUN yum install java-11-openjdk java-11-openjdk-devel java-11-openjdk-headless -y
RUN yum install texlive-collection-latex texlive-ocgx2 texlive-lastpage texlive-cprotect texlive-microtype texlive-blindtext texlive-csvsimple texlive-multirow texlive-helvetic texlive-collection-fontsrecommended  -y
RUN yum install python3-pip -y
RUN yum install nmap -y
RUN yum install openssl -y 
RUN yum install texlive-xurl -y
RUN yum install gobuster -y

COPY target/trovent-recon-engine-1.4.0.jar /opt/trovent/trovent-recon-engine/trovent-recon-engine-1.4.0.jar
COPY src/main/resources/workItems/ /opt/trovent/trovent-recon-engine/workitems/
COPY requirements.txt /opt/trovent/trovent-recon-engine/requirements.txt
COPY dorkScanner.py /opt/trovent/trovent-recon-engine/dorkScanner.py

# provide wordlists 
RUN mkdir /usr/share/wordlists
COPY wordlists/ /usr/share/wordlists

WORKDIR /opt/trovent/trovent-recon-engine

# install WI dependencies
RUN python3 -m pip install virtualenv

ENV VIRTUAL_ENV=/opt/trovent/trovent-recon-engine
RUN python3 -m venv $VIRTUAL_ENV
ENV PATH="$VIRTUAL_ENV/bin:$PATH"
RUN pip3 install --upgrade pip
RUN pip3 install -r requirements.txt

