

import java.net.*;
import java.io.*; 



public class ServidorJava
{
    // Se inicializa el puerto, en este caso sera el 8000
  int puerto = 8000;
  
  //Un metodo para retornar mensajes del servidor
  void MensajeServidor(String mensaje)
  {
    System.out.println("Mensaje: " + mensaje);
  }
  
  public static void main(String[] array)
  {
    //Primero se verifica si el archivo existe
    try{
        String basePath = new File("").getAbsolutePath();
        File archivo = new File(basePath+ "/contactos.txt");
        if(archivo.exists()){
            //no hacer nada
            
        }
        else{
            //Si no existe, se crea uno nuevo
            FileWriter archivoContactos ;
            PrintWriter pw ;
            
            archivoContactos = new FileWriter("contactos.txt");
            pw = new PrintWriter(archivoContactos);
            pw.println("Nombre\t\t\tDireccion IP\t\t\tPuerto");
            pw.close();
        }
       
        
      
    }
    catch (IOException e) {
            e.printStackTrace();
    }
    //Se instancia el servidor y luego se inicia 
    ServidorJava instancia = new ServidorJava();
    
    instancia.IniciarServidor();
  }
  
  public boolean IniciarServidor()
  {
    
    MensajeServidor("Servidor Iniciado");
    try
    {
        // Se abre el host en el puerto 8000 esperando que un cliente llegue
      ServerSocket s = new ServerSocket(8000);
      MensajeServidor("Esperando Conexion......");
      while(true)
      {
        Socket cliente = s.accept();
        //Se instancia un cliente, y se inicia en su respectivo thread con la clase start.
        Peticion pCliente = new Peticion(cliente);
        pCliente.start();
      }
    }
    catch (IOException e)
    {
      MensajeServidor("Error en servidor\n" + e.toString()); //Si ocurre algun problema al instanciar el servidor se lanza este mensaje de error
    }
    return true;
  }
}
