package trabalhogartic;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;


public class GarticClientHandler extends Thread {

    private Socket socket;
    private GarticClient caller;
    private BufferedReader input;
    private static final String DELIMITADOR = "|";
    Color color;

    public GarticClientHandler(Socket socket, GarticClient caller) throws IOException {
        this.socket = socket;
        this.caller = caller;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public Color getColor(String col) {
        switch (col.toLowerCase()) {
            case "blue":
                color = Color.BLUE;
                break;
            case "black":
                color = Color.BLACK;
                break;   
            case "green":
                color = Color.GREEN;
                break;
            case "yellow":
                color = Color.YELLOW;
                break;
        
            case "red":
                color = Color.RED;
                break;
        }
        return color;
    }

    @Override
    public void run() {
        String message;
        String strColor;
        while (true) {
            try {
                if (this.socket.isConnected() && this.input != null) {
                    message = this.input.readLine();
                } else {
                    break;
                }
                
                if (message == null || message.equals("")) {
                    break;
                }
                
                StringTokenizer strt
                            = new StringTokenizer(message, DELIMITADOR);
                
                // mensagem inicial para settar variavel de quem desenha 
                System.out.println("ClienteHandler: " + message);
                
                if(message.substring(0, 1).equals("d")){
                    caller.desenha = true;
                    caller.ajustaPanel();
                    
                // mensagem inicial para setar variavel de quem chuta
                } else if(message.substring(0, 1).equals("c")){
                    caller.desenha = false;
                    caller.ajustaPanel();
                    
                } else if(message.substring(0, 1).equals("p")){
                    caller.tipo = strt.nextToken();
                    while (strt.hasMoreTokens()) {
                        caller.x = Integer.parseInt(strt.nextToken());
                        caller.y = Integer.parseInt(strt.nextToken());
                        strColor = strt.nextToken();
                        caller.cor = this.getColor(strColor);
                        Thread.sleep(1);
                        caller.pintaBolinha();
                    }
                    
                // mensagem de quem está desenhando
                } else if(message.substring(0, 1).equals("1")) {
                    caller.tipo = strt.nextToken();
                    caller.x = Integer.parseInt(strt.nextToken());
                    caller.y = Integer.parseInt(strt.nextToken());
                    caller.t = Integer.parseInt(strt.nextToken());
                    strColor = strt.nextToken();
                    caller.cor = this.getColor(strColor);
                    caller.pintaBolinha();
                    
                // apaga bolinha
                } else if(message.substring(0, 1).equals("a")) {
                    caller.tipo = strt.nextToken();
                    caller.x = Integer.parseInt(strt.nextToken());
                    caller.y = Integer.parseInt(strt.nextToken());
                    strColor = strt.nextToken();
                    System.out.println("X:" + Integer.toString(caller.x));
                    System.out.println("Y:" + Integer.toString(caller.y));
                    caller.apagaBolinha();
                
                // alguem acertou    
                } else if(message.substring(0, 1).equals("g")) {
                    // ganhou mas tem que aguardar todos
                    caller.tipo = strt.nextToken();
                    
                    if (caller.desenha){
                        caller.desenha = false;
                    } else {
                        caller.desenha = true;
                    }
                    
                    while (strt.hasMoreTokens()) {
                        caller.x = Integer.parseInt(strt.nextToken());
                        caller.y = Integer.parseInt(strt.nextToken());
                        caller.apagaBolinha();
                    }
                    caller.ajustaPanel();

                } else if(message.substring(0, 1).equals("2")) {
                    caller.acertou();
                } else if(message.substring(0, 1).equals("3")) {
                    caller.errou();
                }
            } catch (Exception ex) {
                System.out.println("Erro clientehandlerrun:"+ex.getMessage());
                break;
            }
        }
    }

}
