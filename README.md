# GarticGameTCP
Game gartic building in JAVA using Threads and comunnication of TCP

## Objetivo
O objetivo deste trabalho é realizar um jogo de Gartic. O jogo tem o propósito de um player (cliente) desenhar e o outro player (cliente) visualizar o que está sendo desenhado e então, tentar adivinhar o que está sendo desenhado em sua tela. A interação ocorre via servidor, o cliente que está desenhando envia o seu desenho e em quais coordenadas, enquanto o servidor manda as coordenadas para os clientes e pinta elas. Se o cliente que palpita acerta o desenho, ele se torna o ‘desenhista’, e o antigo ‘desenhista’ se torna o ‘palpitador’.

# Projeto

## Interface
A interface visual do lado do servidor, é uma interface simples onde o usuário apenas informa a porta que ele utilizará para realizar a comunicação do jogo e inicia o serviço do servidor.

![Image 1](/image/image1.png)


Já a interface visual do cliente, contém várias opções para o usuário e consequentemente cada opção troca uma mensagem diferente entre clientes e servidor.
Existem duas telas diferentes para os clientes. A primeira é para o cliente desenhista, isto é, o cliente que irá realizar o desenho para seu adversário tentar acertar, a interface desenhista pode ser vista abaixo.

![Image 2](/image/image2.png)

A segunda interface, ilustrada abaixo, é para o cliente palpitador. Ele só terá a opção de inserir no campo de texto o que acha que é o desenho.

![Image 3](/image/image3.png)
