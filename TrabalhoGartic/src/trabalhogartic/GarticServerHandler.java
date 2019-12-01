package trabalhogartic;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class GarticServerHandler extends Thread {

    private ServerConnection cliente;
    private GarticServerMain caller;
    private static final String DELIMITADOR = "|";

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
        
        StringTokenizer strt = new StringTokenizer(message, DELIMITADOR);
        caller.tipo = strt.nextToken();
        //if((caller.tipo.equals("1"))&&(cliente.designer)){
        if(caller.tipo.equals("1")){
            caller.x = Integer.parseInt(strt.nextToken());
            caller.y = Integer.parseInt(strt.nextToken());
            caller.t = Integer.parseInt(strt.nextToken());
            caller.color = strt.nextToken();
            caller.listX.add(caller.qtdPintada, caller.x);
            caller.listY.add(caller.qtdPintada, caller.y);
            caller.qtdPintada++;
            
        }
        List<ServerConnection> clientes = this.caller.getClientes();
        for (ServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                cli.getOutput().println(message);
                cli.getOutput().flush();
            }
        }
        
    }

    @Override
    public void run() {

        //if lista tiver só um
        if(this.caller.getClientes().size() == 1)
        
        this.cliente.getOutput().write("true");
        
        
        String message;
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
                messageDispatcher(message);
                System.out.println(message);
            } catch (Exception ex) {
                System.out.println("Erro serverhandler:"+ex.getMessage());
                break;
            }
        }
        encerrar();
    }
}
