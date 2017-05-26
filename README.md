### findclass ###
---

#### Abstract ####
Findclass is a utility that can help you find classes in jar files.
---

#### Features ####
* parameters:

	-r: include subfolders.
	-g: match with regex.

* runtime environment:
	
java runtime environment 8

* example:
	
	sh findclass.sh -r /home/repository Preconditions
---

#### Build ####

1. clone source code from github:
	
	git clone https://github.com/sanmixy/findClass.git

2. build with maven:
	
	mvn clean install

3. pack src/main/resource/findclass.sh and target/findclass.jar together, and use it in your command line.
