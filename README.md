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
O protocolo de comunicação usado na troca de mensagens de toda a aplicação foi o protocolo TCP. <br>
Inicialmente quando um novo cliente se conecta, é criado um objeto nomeado no projeto como ServerConnection que representa o cliente em nossa aplicação. Na primeira criação, uma variável do cliente definida como isFirst é atribuída como true, isso ocorre pois o primeiro cliente a se conectar é o primeiro a iniciar desenhando. <br>
Essa variável também realiza o controle de quem está desenhando, o desenhista sempre terá esta variável como true, já o player que está palpitando, terá esta variável como false. <br>
E ao desconectar ‘matamos’ esse objeto criado para aquele cliente. 
### -Desenhista:
Para o desenhista iniciar o desenho ele terá que informar primeiramente o que será desenhado no campo:

![Image 4](/image/image4.png)

Com isso a interface libera o painel de desenho para iniciar o desenho, a partir disso, ele começa a desenhar.

![Image 5](/image/image5.png)

O desenho ocorre toda vez que clicamos na tela e arrastamos ou apenas clicamos.
Ao realizar um clique, o cliente manda a seguinte mensagem para o servidor:

“1 | posição x | posição y | tamanho do pincel | cor | desenho”

-1: padrão atribuído para sempre que a troca de mensagem for para um desenho.<br>
-Posição X: posição da coordenada X que você está clicando naquele momento na tela.<br>
-Posição Y: posição da coordenada Y que você está clicando naquele momento na tela.<br>
-Tamanho do pincel: não colocamos opção de escolha, mantemos um valor padrão de 10.<br>
-Cor: cor que o desenho está sendo realizado naquele momento.<br>
-Desenho: o desenho que está sendo feito.<br>


A mensagem ao chegar no servidor, é guardada em três listas, uma que contém todas posições X já pintadas, outra as Y e outra as cores pintadas. ListaX[i], ListaY[i] e ListaCor[i] pertence a um pixel pintado na interface. <br>
Também guardamos no servidor o desenho que está sendo realizado naquele momento. Exclusivamente neste caso, a mesma mensagem com o mesmo formato é enviada a todos os clientes (palpitadores e desenhistas). E na classe ClientHandller é realizado a atribuição das variáveis e chamadas de métodos para os desenhos serem realizados em todos os clientes.
O desenhista tem a opção de apagar um desenho já realizado, com o botão:

![Image 6](/image/image6.png)

Ao clicar neste botão o cliente envia para o servidor a seguinte mensagem:<br>
“apaga”<br>
O servidor ao identificar a chegada desta mensagem, consulta os últimos elementos presente na lista de posição e monta uma mensagem:<br>
“apaga | posição X | posição Y | cor” <br>

-Apaga: mensagem indicativa que algo será apagado. <br>
-Posição X: posição da coordenada X que será apagada. <br>
-Posição Y: posição da coordenada Y que será apagada. <br>
-Cor: cor que será apagada. <br>

Antes do servidor enviar a mensagem, ele apaga o último elemento de ambas listas e envia a mensagem para todos os clientes. <br>

Ao chegar no ClientHandler, ele atribui a variáveis da interface gráfica e chama método que realiza a exclusão do último pixel desenhado. <br>

### Palpitador:
O cliente classificado como palpitador tem o objetivo de acertar o que está sendo desenhado em sua tela. Ele realiza esse palpite pelo campo: <br>

![Image 7](/image/image7.png)

Ao clicar em enviar o cliente manda uma mensagem para o servidor com o seguinte formato: <br>

“palpite | o que ele acha que é” <br>

-Palpite: para indicar que a mensagem é um palpite do cliente. <br>
-O que ele acha que é: é o que ele acha que está sendo desenhado na tela. <br>
A mensagem ao chegar no servidor é realizada uma verificação, o servidor sabe o que está sendo desenhado então ele verifica se o palpitador está certo ou não.
Caso o servidor identifique que o palpitador errou, ele envia só para o palpitador uma mensagem: <br>

“3” <br>

-3: forma proposta para indicar que o palpite está ERRADO. <br>

Com a chegada da mensagem “3” no ClientHandller, ele chama um método que indica que o cliente errou, e esse método mostra uma mensagem na tela apenas para aquele cliente.

![Image 8](/image/image8.png)
