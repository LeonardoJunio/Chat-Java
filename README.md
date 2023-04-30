<h1 align="center">Chat - Java</h1>

<p align="center">Chat between users (at localhost, for now), like a group chat, but you can send as private to 1 or more users. Besides messages, files can also be sent (in this case, they are saved in a folder with the users' names). The system stores logs of messages and errors in TXT files. The message flow would be: ClienteFrameService -> ClienteService -> ListenerSocket(Servidor) -> ListenerSocket(Cliente - to send to the others users).</p>

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

### :warning: Errors/Improvements:

* Add project image
* Trying to adapt for use in a real network
* Adjust project to be able to use docker


##

<div align="center">
	<p>Made with :computer: + :heart: by Leonardo Junio</p>
</div>
