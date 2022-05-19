# BankSimulator

✔ the client can have accounts in different banks

✔ when a client is added to the bank, a new account is opened in this bank

✔ crud operations for managing banks and clients

✔ each account has its own currency

✔ the bank's clients can transfer funds to the accounts of other clients of the same bank without commission

✔ the bank's clients can transfer funds to the accounts of clients of other banks with a fixed commission

✔ there are 2 types of clients: individuals and legal, banks charge different fees for different types of customers

✔ the application allows you to withdraw the client's accounts and the number of funds to them

✔ the application allows you to withdraw all transactions carried out by the client for a specified period

✔ the data is stored in the Postgresql database

# Installation instruction:

1) *Install Git:

   Go to https://git-scm.com/book/ru/v2/%D0%92%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-%D0%A3%D1%81%D1%82%D0%B0%D0%BD%D0%BE%D0%B2%D0%BA%D0%B0-Git select your operating system and follow the instructions
   For example to install git to linux use next commands:
     sudo dnf install git-all
     sudo apt install git

  Verify that git is installed correctly by checking the version:
     git --version

2) *Chose directory on your computer and clone the repository with bank-simulator from github into it:
     git clone https://github.com/NadezhdaTarasova/BankSimulator

3) *Install Docker Engine

   Go to https://docs.docker.com/engine/install / select your operating system and follow the instructions
   For example to install docker to ubuntu use next commands:
     sudo apt-get update 
     sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

   Verify that docker is installed correctly by checking the version:
     docker -v

4) *Install docker-compose 

   Go to https://docs.docker.com/compose/install/ select your operating system and follow the instructions
   For example to install docker-compose to linux use next commands:
     sudo apt-get update
     sudo apt-get install docker-compose-plugin

   Verify that docker-compose is installed correctly by checking the version:
     docker -v 

5) *Go to your directory:
     cd {path to your directory}

6) *Go to BankSimulator/docker directory:
     cd BankSimulator/docker 

7) *Input docker-compose build command:
     docker-compose build

8) *Input docker-compose up command
     docker-compose up


