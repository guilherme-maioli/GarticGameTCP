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

## Troca de mensagens e funcionamento da aplicação:
O protocolo de comunicação usado na troca de mensagens de toda a aplicação foi o protocolo TCP.
Inicialmente quando um novo cliente se conecta, é criado um objeto nomeado no projeto como ServerConnection que representa o cliente em nossa aplicação. Na primeira criação, uma variável do cliente definida como isFirst é atribuída como true, isso ocorre pois o primeiro cliente a se conectar é o primeiro a iniciar desenhando.
Essa variável também realiza o controle de quem está desenhando, o desenhista sempre terá esta variável como true, já o player que está palpitando, terá esta variável como false.
E ao desconectar ‘matamos’ esse objeto criado para aquele cliente.
### -Desenhista:
Para o desenhista iniciar o desenho ele terá que informar primeiramente o que será desenhado no campo:

![Image 4](/image/image4.png)

Com isso a interface libera o painel de desenho para iniciar o desenho, a partir disso, ele começa a desenhar.

![Image 5](/image/image5.png)

O desenho ocorre toda vez que clicamos na tela e arrastamos ou apenas clicamos.
Ao realizar um clique, o cliente manda a seguinte mensagem para o servidor:

“1 | posição x | posição y | tamanho do pincel | cor | desenho”

*-1: padrão atribuído para sempre que a troca de mensagem for para um desenho.*
-Posição X: posição da coordenada X que você está clicando naquele momento na tela.
-Posição Y: posição da coordenada Y que você está clicando naquele momento na tela.
-Tamanho do pincel: não colocamos opção de escolha, mantemos um valor padrão de 10.
-Cor: cor que o desenho está sendo realizado naquele momento.
-Desenho: o desenho que está sendo feito.


