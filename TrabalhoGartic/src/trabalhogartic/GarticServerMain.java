package trabalhogartic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GarticServerMain extends Thread {

    private List<ServerConnection> clientes;
    private ServerSocket server;
    protected int x = 0, y = 0, t = 20, qtdPintada=0;
    protected String tipo = "", color = "";
    protected List<Integer> listX = new ArrayList<Integer>();
    protected List<Integer> listY = new ArrayList<Integer>();
    protected boolean evento = false;

    public GarticServerMain(int porta) throws IOException {
        this.server = new ServerSocket(porta);
        System.out.println(this.getClass().getSimpleName() + " rodando na porta: " + server.getLocalPort());
        this.clientes = new ArrayList<>();
    }

    @Override
    public void run() {
        Socket socket;
        while (true) {
            try {
                socket = this.server.accept();
                ServerConnection cliente = new ServerConnection(socket);
                novoCliente(cliente);
                System.out.println(socket.getRemoteSocketAddress().toString());
                (new GarticServerHandler(cliente, this)).start();
            } catch (IOException ex) {
                System.out.println("Erro 4: " + ex.getMessage());
            }
        }
    }

    public synchronized void novoCliente(ServerConnection cliente) throws IOException {
        if (clientes.isEmpty()){
            cliente.designer = true;
        }
        clientes.add(cliente);
    }

    public synchronized void removerCliente(ServerConnection cliente) {
        clientes.remove(cliente);
        try {
            cliente.getInput().close();
        } catch (IOException ex) {
            System.out.println("Erro servermain1:"+ex.getMessage());
        }
        cliente.getOutput().close();
        try {
            cliente.getSocket().close();
        } catch (IOException ex) {
            System.out.println("Erro servermain2:"+ex.getMessage());
        }
    }

    public List getClientes() {
        return clientes;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.server.close();
    }


}
