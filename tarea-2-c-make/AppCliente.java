



/**
 *
 * @author felipe
 */
/* ChatClient.java */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
 
public class AppCliente implements Runnable {
    Socket server;
    private BufferedReader in = null;
    
    public String nick="";
    public String controllogin="";
    public String controlagregar="";
    public String controlcontactos="";
    public String controlarchivos="";
    public String controlarchivosr="";
    public String respuesta="";
    public  int port = 7000; /* port to connect to */
    private static String host = "localhost"; /* host to connect to */
    public String IP;
 
    private static String minick;
 
    
    /* Se crea el constructor, creando un buffer de lectura*/
    public AppCliente() throws UnknownHostException, IOException{
        this.IP = InetAddress.getLocalHost().getHostAddress();
        this.server = new Socket("localhost", port);
        in = new BufferedReader(new InputStreamReader(
                    server.getInputStream()));
        
    }
    

/* Metodo para enviar mensajes al servidor */
 
  public void  EnviarMensaje(String msg) throws IOException{
      controllogin="";
      controlagregar="";
      respuesta="";
      PrintWriter out = new PrintWriter(this.server.getOutputStream(), true);
      out.println(msg);
  }
  public void EnviarArchivo(String archivo) throws IOException{
      int FILE_SIZE = 6022386;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        Socket socket = new Socket("localhost",13267);
        File myFile = new File (archivo);
        byte [] mybytearray  = new byte [(int)myFile.length()];
        fis = new FileInputStream(myFile);
        bis = new BufferedInputStream(fis);
        bis.read(mybytearray,0,mybytearray.length);
        os = socket.getOutputStream();
        os.write(mybytearray,0,mybytearray.length);
        os.flush();
        socket.close();
        
  }
  
  public void RecibirArchivos() throws IOException{
      int FILE_SIZE = 6022386;
      int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
          sock = new Socket("localhost", 13267);
          System.out.println("Connecting...");

          // receive file
          byte [] mybytearray  = new byte [FILE_SIZE];
          InputStream is = sock.getInputStream();
          fos = new FileOutputStream("retornado.jpg");
          bos = new BufferedOutputStream(fos);
          bytesRead = is.read(mybytearray,0,mybytearray.length);
          current = bytesRead;

          do {
             bytesRead =
                is.read(mybytearray, current, (mybytearray.length-current));
             if(bytesRead >= 0) current += bytesRead;
          } while(bytesRead > -1);

          bos.write(mybytearray, 0 , current);
          bos.flush();
          System.out.println("Files "
              + " downloaded (" + current + " bytes read)");
        }
        finally {
          if (fos != null) fos.close();
          if (bos != null) bos.close();
          if (sock != null) sock.close();
        }
  }
  
  
  /* metodo run que lee respuestas desde el servidor*/
  @Override
  public void run() {
        
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                    if(!msg.equals("")){
                        this.respuesta=msg;
                    }
                    
             
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}



 
