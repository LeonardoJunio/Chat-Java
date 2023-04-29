<h1 align="center">Chat - Java</h1>

<p align="center">Chat between users, like a group chat, but you can send a private message to 1 or more users</p>

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

* ClienteFrameService -> ClienteService -> ListenerSocket(Servidor) -> ListenerSocket(Cliente (nesse caso pra enviar para os outros usuarios))

### :warning: Errors/Improvements:

* exceções
* Mensagens de erros/aviso
* Listar em Last Group somente os 5 primeiros caracteres de cada nome
* Usar trim nos textos
* Ajustar pra pasta que armazena arquivo ser dentro do projeto, mas sem ter caminho manualmente (System.getProperty ?)
* Criar uma herança com message + chat e arquivo
* Aplicar a funcionalidade para envio de arquivo (teria caminho padrao e pasta com nome de cada usuario pra simular) (?)
* Ajustar para o processamento principal ficar do lado do servidor, assim enviaria uma lista de mensagens+arquivo para ele processar/enviar/salvar
* Historico de conversação salva em texto
* Metodos com 1 funcionalidade?
* Melhora da estrutura de código
* Add project image
* Adjust project to be able to use docker


##

<div align="center">
	<p>Made with :computer: + :heart: by Leonardo Junio</p>
</div>
