# ProjectCritics

## Depedencies

Installation script for ubuntu ! include NodeJS android studio mongodb sublime text and GIT.

Install all the project depedencies

### NODEJS
An efficient and scalable HTTP server
```
sudo apt-get update
sudo apt-get install nodejs -y
```
### ANDROIDSTUDIO
The sdk to develop an android app
```
sudo apt-add-repository ppa:paolorotolo/android-studio
sudo apt-get update
sudo apt-get install android-studio -y
```
### MONGODB
A distriabuted database
```
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
echo "deb http://repo.mongodb.org/apt/ubuntu "$(lsb_release -sc)"/mongodb-org/3.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.0.list
sudo apt-get update
sudo apt-get install -y mongodb-org
sudo apt-get install -y mongodb-org=3.0.4 mongodb-org-server=3.0.4 mongodb-org-shell=3.0.4 mongodb-org-mongos=3.0.4 mongodb-org-tools=3.0.4
```
### SublimeText
A good text editor with code highlight
```
sudo add-apt-repository ppa:webupd8team/sublime-text-2
sudo apt-get update
sudo apt-get install sublime-text -y
```
### GIT
The versioning system
```
sudo apt-get update
sudo apt-get install git -y
```
### NPM
the package manager for NODEJS
```
sudo apt-get install npm -y
```

## Clone the project
```
https://github.com/vanmellebenjamin/ProjectCritics.git
```

## Start the server
```
cd /pathToProject/Backend
sudo npm install
nodejs app.js
```
