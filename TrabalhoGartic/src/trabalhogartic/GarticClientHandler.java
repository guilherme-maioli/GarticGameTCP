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

    public Color setColor(String col) {
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
        String aux;
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
                //caller.apagaBolinha();
                StringTokenizer strt
                            = new StringTokenizer(message, DELIMITADOR);
                // mensagem de quem está desenhando
                if(message.substring(0, 1).equals("1")) {
                    caller.tipo = strt.nextToken();
                    caller.x = Integer.parseInt(strt.nextToken());
                    caller.y = Integer.parseInt(strt.nextToken());
                    caller.t = Integer.parseInt(strt.nextToken());
                    strColor = strt.nextToken();
                    caller.cor = this.setColor(strColor);
                    caller.pintaBolinha();
                    
                // mensagem de quem está desenhando (apagando desenho)
                } /*else if (message.substring(0, 1).equals("2")) {
                    caller.tipo = strt.nextToken();
                    caller.desenho = strt.nextToken();
                    caller.x = Integer.parseInt(strt.nextToken());
                    caller.y = Integer.parseInt(strt.nextToken());
                    strColor = strt.nextToken();
                    caller.cor = java.awt.Color.getColor(strColor);
                    caller.palpite = strt.nextToken();
                    caller.apagaBolinha();
                // mensagem de quem está palpitando
                } else {
                    caller.tipo = strt.nextToken();
                    caller.desenho = strt.nextToken();
                    caller.x = Integer.parseInt(strt.nextToken());
                    caller.y = Integer.parseInt(strt.nextToken());
                    strColor = strt.nextToken();
                    caller.cor = java.awt.Color.getColor(strColor);
                    caller.palpite = strt.nextToken();
                }*/
            } catch (Exception ex) {
                System.out.println("Erro clienthandler:"+ex.getMessage());
                break;
            }
        }
    }

}
