/*
 Autor; Lucas Alves da costa
projeto, Cliente socket TCP/FTP
Redes de Computadores 2016
 */
package clientesocket;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucas Alves
 */
public class ClienteSocket {


 
    public static void main(String[] args)throws Exception{
         Menu();// ja começo chamando o menu
    }
    
       //***************** menu básico ***********************//                           
    public static void Menu() throws IOException {
       // inicia a conexão com o servidor raiz   
          System.out.println("Escolha uma opção:\n [1]. Listar Arquivos Compartilhados.\n [2]. Configurar IP do Servidor."
                  + "\n [3]. Sair!\n");
         Scanner Ler;
         Ler = new Scanner(System.in);
         int Opc;
         Socket Cliente;
           Opc = Ler.nextInt();
           if(Opc==2){
               
              String newIP = JOptionPane.showInputDialog("Digite O 'IP' do Servidor!");// Entrada por enterface grafica
              Cliente = new Socket(newIP,5555);
              long T = System.currentTimeMillis();
              System.out.println("Conectado com o novo servidor. . .");
              long tempoEspera;
               do{
                 tempoEspera = System.currentTimeMillis()+ 100000;
                    System.out.println("O Servidor esta ocupado! Aguarde......\n");
               
               } while(System.currentTimeMillis()>tempoEspera);
                receberMsg(Cliente);
           }if(Opc==3){
               System.exit(0);
           }
           else{
            Cliente = new Socket("127.0.0.1",5555);
             System.out.println("Conectado com o servidor. . .");
              receberMsg(Cliente);
           }  
    }
    public static void Download(String Lista,Socket Cliente) throws IOException {
        // Função para receber o arquivo e savar em disco
        
        // Aqui envia  o nome do arquivo a ser baixado! 
           OutputStream Saida;
              Saida = Cliente.getOutputStream();
              Saida.write(Lista.getBytes());
              Saida.write(4);
           Saida.flush();
  /*************************************************************************************/ 
      
       
      int filesize = 10240000;// buffer de 10.000 KB/9.7 MB
     
      long start = System.currentTimeMillis();
      int bytesRead = -1;
      int current = 0;
      long end;
   
            // recebendo o arquivo
            File Salva;
            Salva = new File(Lista);
            InputStream is = Cliente.getInputStream();
            
            
            byte [] mybytearray  = new byte [filesize];
            
               try (FileOutputStream fos = new FileOutputStream(Salva)) {
  // O mesmo nome de arquivo que e solicitado é usado para salvar o arquivo que o servidor retornar
  // incluindo a extensão.
         BufferedOutputStream bos;  
          bos = new BufferedOutputStream(fos);
           bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;
       
        while(bytesRead > -1)  {// enquanto ouver bytes a serem salvos o arquivo sera salvo
            bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
            if(bytesRead >= 0)
                current += bytesRead;
             end = System.currentTimeMillis();
               System.out.println("Recebendo..."+ fos + " "+(end-start));/* enquanto a transferencia sendo feita
            sera exibida esta msg + complemento do tempo*/
              bos.write(mybytearray, 0 , current);
              bos.flush();// força a gravação de todos os bytes do arquivo
            fos.write(mybytearray, 0, current);
            fos.flush();
           
         }
         bos.close();// Fecha o recebimento
      }
           
            // exibe uma msgn grafica de arquivo recebido
              JOptionPane.showMessageDialog(null, "FILE RECEIVED!");
              Menu();
            
    }
      
    

    /**
     *
     * @param Cliente
    
     * @throws java.io.IOException
     
     */
   // @SuppressWarnings("empty-statement")
   
   @SuppressWarnings("empty-statement")
    public static void receberMsg(Socket Cliente) throws IOException {
       
        InputStream Recive;
        Recive = Cliente.getInputStream();
      int recebido = 0; 
      char DATE = 0;
     // Recebe o nome do arquivo, faço um cast para converter o dado recebido em "char" e Enprimo para o USER
      while ((DATE = (char)Recive.read()) != 4) {
        System.out.print((char)DATE);
        recebido =DATE; 
     };
   
          Scanner Ler;
          Ler = new Scanner(System.in);
          String Lista;
          //Estrução básica de como  escolher o arquivo
          JOptionPane.showMessageDialog(null, "Copie o  nome do arquivo a ser baixado![ctrl + c]\n");
          System.out.println("Cole o Nome do  arquivo AQUI: [ctrl + v]:");
          Lista = Ler.nextLine();
          
           // Leio a escolha do User, e passo como parametro da funcão Download
           Download(Lista,Cliente);
        
 }
  
}
    
