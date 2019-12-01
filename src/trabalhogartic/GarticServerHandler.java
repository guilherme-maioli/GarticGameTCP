package trabalhogartic;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class GarticServerHandler extends Thread {

    private ServerConnection cliente;
    private GarticServerMain caller;
    private static final String DELIMITADOR = "|";
    private int ganhadores = 0;

    public GarticServerHandler(ServerConnection cliente, GarticServerMain caller) throws IOException {
        this.cliente = cliente;
        this.caller = caller;
    }

    @Override
    protected void finalize() throws Throwable {
        encerrar();
    }

    private void encerrar() {
        this.caller.removerCliente(this.cliente);
    }

    public synchronized void messageDispatcher(String message) throws IOException {
        System.out.println("messageDispatcher1:" + message);
        boolean sendAll = false;
        String messageFirst = "";
        
        StringTokenizer strt = new StringTokenizer(message, DELIMITADOR);
        caller.tipo = strt.nextToken();
        
        // se o cliente for o que desenha
        //if (cliente.isFirst) {
            
            //System.out.println("cliente desenha");
            // desenhar
            if(caller.tipo.equals("1")){
                messageFirst = message;
                caller.x = Integer.parseInt(strt.nextToken());
                caller.y = Integer.parseInt(strt.nextToken());
                caller.t = Integer.parseInt(strt.nextToken());
                caller.color = strt.nextToken();
                caller.desenho = strt.nextToken();

                caller.listX.add(caller.qtdPintada, caller.x);
                caller.listY.add(caller.qtdPintada, caller.y);
                caller.listColor.add(caller.qtdPintada, caller.color);

                caller.qtdPintada++;
                message = messageFirst;
                sendAll = true;
            // apaga desenho
            }else if(caller.tipo.equals("apaga")){
                messageFirst = "apaga";
                messageFirst = messageFirst + "|" +
                        caller.listX.get(caller.listX.size() - 1) + "|"+
                        caller.listY.get(caller.listY.size() - 1) + "|"+
                        caller.listColor.get(caller.listColor.size() - 1);

                caller.listX.remove(caller.listX.size()-1);
                caller.listY.remove(caller.listY.size()-1);
                caller.listColor.remove(caller.listColor.size()-1);
                caller.qtdPintada--;

                message = messageFirst;       
                sendAll = true;
            }
            
        // se o cliente for o que palpita
        //} else {
            
            // ao novo cliente entrar e a tela já estiver desenhada
            if(caller.tipo.equals("pedido")){
                messageFirst = "pedido";
                for(int x = 0; x<caller.listX.size(); x++){
                
                    messageFirst = messageFirst + "|" + 
                                  caller.listX.get(x) + "|" + 
                                  caller.listY.get(x) + "|"+
                                  caller.listColor.get(x); 
                }
                message = messageFirst;
                sendAll = false;
            // cliente acertou o desenho
            } else if(caller.tipo.equals("ganhou")){
                messageFirst = "ganhou";
                               
                for (int i = 0; i < caller.listX.size(); i++) {
                    messageFirst = messageFirst + "|" +
                                    caller.listX.get(i) + "|" +
                                    caller.listY.get(i);
                }
                
                caller.listX.clear();
                caller.listY.clear();
                caller.listColor.clear();
                caller.qtdPintada = 0;
               
                message = messageFirst;
                sendAll = true;
                
            } else if(caller.tipo.equals("palpite")){
                messageFirst = "";
                
                // acerto
                String palpite = strt.nextToken();
                if (caller.desenho.equals(palpite)) {
                    messageFirst = "2";
                    
                    // troca as variaveis do cliente
                    List<ServerConnection> clientes = caller.getClientes();
                    for (int i = 0; i < clientes.size(); i++) {
                        if(clientes.get(i).isFirst){
                            clientes.get(i).setIsFirst(false);
                        }else {
                            clientes.get(i).setIsFirst(true);
                        }
                    }
                    
                // erro
                } else {
                    messageFirst = "3";
                }
                
                message = messageFirst;
                sendAll = false;
            }
            
        //}
        
        System.out.println("messageDispatcher2:" + message);
        if (sendAll) {
            List<ServerConnection> clientes = this.caller.getClientes();
            for (ServerConnection cli : clientes) {
                if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                    cli.getOutput().println(message);
                    cli.getOutput().flush();
                }
            }
        } else {
            if (cliente.getSocket() != null && cliente.getSocket().isConnected() && cliente.getOutput() != null) {
                 cliente.getOutput().println(message);
                 cliente.getOutput().flush();
            } 
        }
        
    }
    
    @Override
    public void run() {
        
        if (cliente.isFirst){
            this.cliente.getOutput().println("desenha");
            caller.listX.clear();
            caller.listY.clear();
            caller.listColor.clear();   
            caller.qtdPintada = 0;
        }    
        
        String message  = "";
        while (true) {
            try {
                if (this.cliente.getSocket().isConnected() && this.cliente.getInput() != null) {
                    message = this.cliente.getInput().readLine();
                } else {
                    break;
                }
                if (message == null || message.equals("")) {
                    break;
                }
                System.out.println("serverhandles:  "+message);
                messageDispatcher(message);
            } catch (Exception ex) {
                System.out.println("Erro serverhandlerrun:"+ex.getMessage());
                break;
            }
        }
        caller.removerCliente(cliente);
        encerrar();
    }
}
