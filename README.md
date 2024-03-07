## BUILD
### BUILD JAR
```
mvn clean package
```
### BUILD SBOM
- for Java dependencies
```
mvn install cyclonedx:makeAggregateBom
```
- for Python dependencies
```
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
cyclonedx-py environment
```
### BUILD DOCKER
```
docker build -t trovent-recon-engine:1.4.2 .
```
## RUN
### RUN in DOCKER
```
docker run -it --rm -v /results/:/opt/trovent/trovent-recon-engine/results trovent-recon-engine:1.4.2 java -jar trovent-recon-engine-1.4.2.jar trovent.io
```
### IF YOU STILL PREFER TO RUN IT LOCALLY:
```
sudo mkdir -p /opt/trovent/trovent-recon-engine/workitems
sudo mount -o bind $(pwd)/src/main/resources/workItems/ /opt/trovent/trovent-recon-engine/workitems/
sudo cp config.yml /opt/trovent/trovent-recon-engine/
source venv/bin/activate
sudo apt-get install nmap
java -jar target/trovent-recon-engine-1.4.2.jar trovent.io
```
## CREDS
Wordlist directory is a subset of SecLists  (https://github.com/danielmiessler/SecLists/tree/master/Discovery)
