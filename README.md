<h1 align="center">Chat - Java</h1>

<p align="center">Chat between users (at localhost, for now), like a group chat, but you can send as private to 1 or more users.</p>

<p align="center">Besides messages, files can also be sent (in this case, they are saved in a folder with the users' names).</p>

<p align="center">The system stores logs of messages and errors in TXT files.</p>

<hr> 

### :hammer_and_wrench: Technologies & Concepts:

* Java 17

<div align="center" style="display: inline_block">
	<img src="https://img.shields.io/static/v1?label=Java&message=v17&color=B07219&style=flat"/>
	<img src="https://img.shields.io/static/v1?label=license&message=MIT&color=green&style=flat"/>
</div>

### :gear: Settings:

* Project Servidor - Starting the server
	* Run as Java Application
* Project Cliente - Start one or more clients 
	* Run as Java Application (Cliente)
* Obs: Tests done with eclipse (load as separate projects)
* Obs 2: To edit the frame visually, you need the WindowBuilder plugin (installable from Eclipse Marketplace) and open it as WindowBuilderEditor (bottom tab 'Design')
* Obs 3: The message flow would be: ClienteFrameService -> ClienteService -> ListenerSocket (Servidor) -> ListenerSocket (Cliente - to send to the others users)

### :warning: Errors/Improvements:

* Trying to adapt for use in a real network
* Adjust project to be able to use docker

### :framed_picture: Images:

<div align="center" style="display: inline_block">
<img src="https://user-images.githubusercontent.com/15869620/235369177-a0e3c05e-0293-46e3-a4f1-c2f9170cdf17.png"/>
</div>

##

<div align="center">
	<p>Made with :computer: + :heart: by Leonardo Junio</p>
</div>
