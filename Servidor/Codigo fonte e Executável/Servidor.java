import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Lucas Alves
 */
public class Servidor {
    
     public static void EnviarLista(Socket Cliente) throws IOException  {
         
        File MeuArquivo;
        // Aqui A variavel recebe o nome do diretório dos arquivos
        MeuArquivo = new File("MeuArquivo");
        File[] Lista;
        // Aqui a string Lista recebe o nome de todos os arquivos presentes na pasta "MeuArquivo"
        Lista = MeuArquivo.listFiles();
       String Arq2 = " ";
        int cont = 0;
        for(File fileTmp: Lista){
           cont ++; 
           Arq2 += fileTmp.getName()+"\n";// Aqui a variavel arq2 é incrementada linearmente de cada nome de arquivo
         
        }
        
       System.out.println("Conteudo da pasta;[ "+ cont +" ] Arquivo(s)..\n");
        OutputStream SendList;
         SendList = Cliente.getOutputStream();
          SendList.write(Arq2.getBytes());
           SendList.write(4);
            SendList.flush();
           
       // infoma quem se conectou e solicitou a lista!
         System.out.println("##-- Lista Solicitada por:..."+ Cliente.getInetAddress().getCanonicalHostName()+"--##\n");
         UpLoad(Cliente);// chamo a função de envio
        
  }
    
     public static void UpLoad(Socket Cliente) throws IOException{
         String NomeArq = "";
         InputStream Recive;
         Recive = Cliente.getInputStream();
         int recebido=0;
         char data = 0;
  
  // Recebo o nome do arquivo que o cliente solicitou
     while ((data = (char)Recive.read()) != 4) {
          //System.out.print((char)data);
         recebido=data;
        NomeArq += data;
      }
     OutputStream Tam;
     Tam = Cliente.getOutputStream();
     
            System.out.println("Conexão aceita: " + Cliente.getInetAddress().getHostAddress());
           
            File myFile = new File ("MeuArquivo//"+NomeArq);// o arquivo e buscado no diretório e enviado
            // Lembrando que o arquivo deve esta dentro da pasta "Arquivos".
            long Tamanho = myFile.length();
           
            // fragmenta o arquivo em uma cadeia de bytes e envia para o cliente
              // envia o arquivo (transforma em byte array)
             byte [] mybytearray  = new byte [(int)myFile.length()];
              FileInputStream fis = new FileInputStream(myFile);
               BufferedInputStream bis = new BufferedInputStream(fis);
                 bis.read(mybytearray,0,mybytearray.length);
                  OutputStream os = Cliente.getOutputStream();
            System.out.println("Enviando...");// Enquanto o arquivo estiver sendo enviado a msg vai aparecer
            
             os.write(mybytearray,0,mybytearray.length);
             
             os.flush();// forca o envio de todos os bytes
             Cliente.close();// fecha a conexão
             
          // Msg para controlar Downloads feitos por usuarios
          System.out.println("Cliente: " + Cliente.getInetAddress().getHostAddress() + " Fez Download do arquivo; "+myFile.getName()+" Tamanho: "+Tamanho/1024+" KBs");
                 
      
     }
    
     
	 public static void main (String [] args ) throws IOException {
             
    // cria o nosso socket na porta 5555
  
             ServerSocket servsock = new ServerSocket(5555);
             System.out.println("SERVER On-Line!..........\n Ouvindo a porta(5555)");
             
             while(true){
                  Socket Server = servsock.accept();
                System.out.println("Cliente conectado: " + Server.getInetAddress().getHostAddress());
              EnviarLista(Server);
             }
              
    
    }
 
   
    
}