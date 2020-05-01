import java.util.*;
import java.io.*;
import java.nio.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

public class Servidor {
	

	static final String adminUsername = "admin";           // admin predefinido
	static final String adminPassword = "labc";
	private static Scanner x;
	private static Scanner y;

    public static void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

	public static boolean menuDeLogin() throws IOException, InterruptedException {                      // menu inicial do programa

        clearScreen();

		System.out.println("Menu:");
        System.out.println("1) Login / Autenticação");
        System.out.println("2) Sair");
        System.out.print("> ");

        Scanner stdin = new Scanner(System.in);
        int num = stdin.nextInt();
        boolean estado = false;

        switch (num) {

            case 1: estado = login();
                    break;

            case 2: System.out.println("A encerrar o sistema...");
                    estado = false;
                    break;

            default: System.out.println("Opção inválida!");
                    estado = menuDeLogin();
                    break;
                        
            }
        return estado;
	}

	public static boolean menu() throws IOException, InterruptedException {                             // menu de login

            clearScreen();

            System.out.printf("\n");
            System.out.println("Menu:");
            System.out.println("1) Criar novo tópico");
            System.out.println("2) Gerir tópicos / utilizadores");
            System.out.println("3) Ver estatisticas");
            System.out.println("4) Log out");
            System.out.print("> ");
            boolean estado = true;
            Scanner input = new Scanner(System.in);
            int num = input.nextInt();

            switch(num) {

                case 1: criarNovoTopico();
                        break;
                case 2: gerirTopicosUtilizadores();
                        break;
                case 3: verEstatisticas();
                        break;
                case 4: System.out.println("Finalizando sessão...\n");
                        TimeUnit.SECONDS.sleep(3);
                        estado = menuDeLogin();
                        break;
                default: System.out.println("Opção inválida!");
                        TimeUnit.SECONDS.sleep(3);
                        menu();
                        return estado;
            }
            TimeUnit.SECONDS.sleep(3);
            return estado;
        }

	public static boolean login() throws InterruptedException{                         // verificação de login

            clearScreen();
			Scanner input = new Scanner(System.in);
            boolean estado = false;                         // estado de sessao iniciada ou nao
            Console console = System.console();
            
            // fazer scan do ficheiro adminstrador com os utilizadores adminstradores 
            try{
            x = new Scanner(new File("Admin.txt"));
            }
            catch(Exception e){
                System.out.println("");
            }
            
            File file = new File("Admin.txt");
    		System.out.println("\n\033[1mLogin\033[0m\n");
    		System.out.print("Username: ");
  		  	String tempUsername = input.next();
    		char passwordArray[] = console.readPassword("Password: ");
            String tempPassword = new String(passwordArray);


            // verificar se o username e a password correspondem ao utilizador admin predefinido
            
            if (tempUsername.equals(adminUsername) && tempPassword.equals(adminPassword)){  
            	System.out.println("\n\033[1mLogin efectuado com sucesso!\033[0m");         
                estado = true;  // login bem sucedido
            }

            if(file.exists()){
            if(estado == false){
            while(x.hasNext()){
            String a = x.next();
            String b = x.next();

            // verificar se o username e password correspondem a algum utilizador administrador do ficheiro "Admin.txt"
            if(tempUsername.equals(a) && tempPassword.equals(b)){       
                System.out.println("\n\033[1mLogin efectuado com sucesso!\033[0m");                        
                estado = true;  //login bem sucedido
                }
            }
        }
        }
            if(estado == false){
                System.out.println("\n\033[1mErro ao iniciar sessão.\033[1m");
                estado = login();
                     // mensagem de erro ao nao conseguir fazer login
            }

            return estado;

	}

	public static void criarNovoTopico() throws IOException, InterruptedException {       // metodo de criar novo topico

        File topicosFile = new File("Topicos.txt");
        Scanner input = new Scanner(System.in);

        try {
            if(!topicosFile.exists()){      // se o ficheiro nao existe, criar um novo
                topicosFile.createNewFile();
            }
        } catch(IOException e){
            System.out.println("Ficheiro nao encontrado");
            TimeUnit.SECONDS.sleep(2);
            return;
        }

        FileWriter writer = new FileWriter(topicosFile, true);
        BufferedWriter buf = new BufferedWriter(writer);

        try {
            x = new Scanner(new File("Topicos.txt"));
        } 
        catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                TimeUnit.SECONDS.sleep(2);
                return;
        }

        System.out.print("\n\033[1mNome do tópico: \033[0m");
        String nome = input.next();                             // nome do topico a criar

        while(x.hasNext()){

            String a = x.next();
            String b = x.next();
            String c = x.next();

            if(nome.equals(a)){
                System.out.print("\033[1mO nome já existe!\033[0m");        // verificar se o nome já existe
                TimeUnit.SECONDS.sleep(2);
                criarNovoTopico();
                return;
            }

        }

        System.out.print("Descrição (uma palavra): ");      // descricao
        String descricao = input.next();
        System.out.print("Número máximo de mensagens: ");
        String mensagens = input.next();                    // numero maximo de mensagens

        buf.write(nome);
        buf.write(" ");
        buf.write(descricao);
        buf.write(" ");
        buf.write(mensagens);
        buf.write("\n");
        buf.flush();
        buf.close();

        File newTopico = new File(nome+".txt");     

        try{
            if(!newTopico.exists())         // criar o ficheiro do topico
                newTopico.createNewFile();
        }
        catch(IOException e){
            System.out.println("Erro");
            TimeUnit.SECONDS.sleep(2);
            return;
        }

        System.out.println("\n\033[1mTópico criado com sucesso!\033[0m");

	}


	public static void gerirTopicosUtilizadores() throws IOException, InterruptedException {   // menu de gerir topicos/utilizadores
        clearScreen();
        System.out.println("1) Gerir Tópicos");
        System.out.println("2) Gerir Utilizadores");
        System.out.print("> ");
        Scanner std = new Scanner(System.in);
        int num = std.nextInt();

        switch(num){
        	case 1: gerirTopicos();
        			break;
        	case 2: gerirUtilizadores();
        			break;
        	default: System.out.println("Opção inválida!");
                    TimeUnit.SECONDS.sleep(2);
                    gerirTopicosUtilizadores();
                    return;
        }
	}

    public static void gerirTopicos()throws IOException , InterruptedException {       // menu de editar ou eliminar topico

        clearScreen();

        System.out.println("Quer editar ou eliminar um tópico?");
        System.out.print("> ");
        Scanner input = new Scanner(System.in);
        String opcao = input.next();

        if(opcao.equals("editar"))
            editarTopicos();
        
        if(opcao.equals("eliminar"))
            eliminarTopicos();

        if(!(opcao.equals("editar") || opcao.equals("eliminar"))){
            System.out.println("opcao invalida!");
            gerirTopicos();
        }


    }

	public static void editarTopicos() throws IOException, InterruptedException{         // editar topicos
        clearScreen();
        Scanner input = new Scanner(System.in);
        System.out.println("Qual é o tópico a editar?");
        System.out.print("> ");
        String topico = input.next();

        File file = new File("Topicos.txt");
        FileWriter writer = new FileWriter("Topicos.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        try {
            x = new Scanner(new File("Topicos.txt"));
                } 
            catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                }

        boolean existe = false; // variavel que verifica se o topico existe

        while(x.hasNext()){
            String a = x.next();
            String b = x.next();
            String c = x.next();

            if(a.equals(topico))
                existe = true;  // se for encontrado um topico com o nome igual ao introduzido, existe
        }

        if(existe == false){
            System.out.println("\033[1mO tópico "+ topico + " não existe!\033[0m"); // mensagem caso o topico nao existe
            TimeUnit.SECONDS.sleep(3);
            editarTopicos();
            return;
        }

        try
             {
             
             String line = "", oldtext = "",linha="", newTexto="";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";
                 if(line.contains(topico))      // linha com os parametros do topico selecionado
                    linha = line + "\r\n";
             }
             reader.close();

            String nada = oldtext.replaceAll(oldtext,"");

            System.out.println("\nO que deseja alterar?\n");
            System.out.println("1) Nome");
            System.out.println("2) Descrição");
            System.out.println("3) Número de mensagens máximas");
            String mudar = "", mudado = "";
            int escolha = input.nextInt();

            try {
            x = new Scanner(new File("Topicos.txt"));
                } 
            catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                TimeUnit.SECONDS.sleep(3);
                return;
                }

            while(x.hasNext()){

                String a = x.next();
                String b = x.next();
                String c = x.next();

                if(topico.equals(a)){

                    if(escolha == 1){
                        System.out.print("Mudar nome do tópico " + topico + " para: ");
                        mudar = a;
                        mudado = input.next();
                    }

                    if(escolha == 2){
                        System.out.print("Mudar descrição do tópico " + topico + " para: ");
                        mudar = b;
                        mudado = input.next();
                    }

                    if(escolha == 3){
                        System.out.print("Mudar numero máximo de mensagens do tópico " + topico + " para: ");
                        mudar = c;
                        mudado = input.next();
                    }
                }
            }

            String newLinha = linha.replaceAll(mudar,mudado);       // substituir a linha do topico pelo que se queria alterar


            newTexto = oldtext.replaceAll(linha,newLinha);            
             //To replace a line in a file
             //String newtext = oldtext.replaceAll("This is test string 20000", "blah blah blah");
             PrintWriter pw = new PrintWriter("Topicos.txt");
             writer.write(newTexto);writer.close();     // escrever no ficheiro o que se quer alterar

             System.out.println("\033[1mTópico editado com sucesso!\033[0m");

             if(escolha == 1){      // se a escolha for mudar o nome, vai ser necessario mais estes passos

                File antigo = new File(mudar+".txt");
                File novoNome = new File(mudado+".txt");
                PrintWriter novoWriter = new PrintWriter(mudado+".txt");

                try {
                    if(!novoNome.exists()){     // criar um novo ficheiro com o nome alterado
                        novoNome.createNewFile();
                    }
                } catch(IOException e){
                    System.out.println("Erro");
                }

                try {
                    if(!antigo.exists()){
                        antigo.createNewFile();
                    }
                } catch(IOException e){
                    System.out.println("Erro");
                }

                BufferedReader novoReader = new BufferedReader(new FileReader(antigo));
             
                String antigoTexto = "", antigaLinha = "";
                while((antigaLinha = novoReader.readLine()) != null)
                    {
                        antigoTexto += antigaLinha + "\r\n";
                    }
                    novoReader.close();

                novoWriter.write(antigoTexto);      // escrever tudo o que estava no ficheiro com o nome antigo para o novo ficheiro
                novoWriter.close();
                antigo.delete();                    // apagar o ficheiro com o nome antigo

                File subscrito = new File("Clientes.txt");
                boolean estado = false;
                y = new Scanner(subscrito);

                while(y.hasNext()){

                    String aa = y.next();
                    String bb = y.next();
                    String cc = y.next();
                    String dd = y.next();
                    String ee = y.next();

                    File utilizador = new File(cc+".txt");
                    if(utilizador.exists()){        // substituir tambem o nome antigo pelo nome recente se algum utilizador seguisse esse topico com o nome alterado
                        x = new Scanner(utilizador);
                        String linee = "", oldtextt = "",linhaa="", newTextoo="";
                        BufferedReader readd = new BufferedReader(new FileReader(utilizador));
                        while((linee = readd.readLine()) != null){
                            oldtextt += linee + "\r\n";
                            if(linee.contains(mudar)){
                                linhaa = linee + "\r\n";;
                                estado = true;
                            }
                        }

                reader.close();
                if(estado == true){
                    String newLinhaa = linhaa.replaceAll(mudar,mudado);
                    newTextoo = oldtextt.replaceAll(linhaa,newLinhaa);           
            
                    PrintWriter pwr = new PrintWriter(cc+".txt");
                    FileWriter writee = new FileWriter(cc+".txt"); 
                    writee.write(newTextoo);writee.close();
                }
            }
        }

        }
    }
         
         catch (IOException ioe)
             {
             ioe.printStackTrace();
         }

	}

    public static void eliminarTopicos() throws IOException, InterruptedException {       // eliminar um topico
        clearScreen();
        Scanner input = new Scanner(System.in);
        System.out.println("Qual tópico deseja remover?");
        System.out.print("> ");
        String topico = input.next();

        File file = new File("Topicos.txt");
        FileWriter writer = new FileWriter("Topicos.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        try {
            x = new Scanner(new File("Topicos.txt"));
            } 
        catch(Exception e){
            System.out.println("Ficheiro nao encontrado");
            }

        boolean existe = false;

        while(x.hasNext()){
            String a = x.next();
            String b = x.next();
            String c = x.next();

            if(a.equals(topico))
                existe = true;
        }

        if(existe == false){        // mensagem caso o topico nao exista
            System.out.println("\033[1mO tópico "+ topico + " não existe!\033[0m");
            TimeUnit.SECONDS.sleep(3);
            eliminarTopicos();
            return;
        }
        
        try
            {
             
            String line = "", oldtext = "",linha="", newTexto="";
            while((line = reader.readLine()) != null)
                {
                oldtext += line + "\r\n";
                    if(line.contains(topico))
                        linha = line + "\r\n";
            }
            reader.close();

            newTexto = oldtext.replaceAll(linha,"");   // susbtituir a linha com os parametros do utilizador por nada, eliminando assim o utilizador         
            
            PrintWriter pw = new PrintWriter("Topicos.txt");
            writer.write(newTexto);writer.close();

            File antigo = new File(topico+".txt");
            antigo.delete();

            System.out.println("\033[1mTópico removido com sucesso!\033[0m");
                
        }
         
        catch (IOException ioe)
            {
            ioe.printStackTrace();
        }

    }

	public static void gerirUtilizadores() throws IOException, InterruptedException {  // menu de gerir utilizadores

        clearScreen();
		Scanner stdin1 = new Scanner(System.in);

		System.out.println("1) Criar utilizador");
		System.out.println("2) Validar utilizadores");
		System.out.println("3) Gerir utilizadores existentes");

		int num = stdin1.nextInt();

		switch(num){
        	case 1: criarUtilizador();
        			break;
        	case 2: validarUtilizadores();
        			break;
        	case 3: gerirUsers();
        			break;
        	default: System.out.println("Opção inválida!");
                    TimeUnit.SECONDS.sleep(3);
                    gerirUtilizadores();
                    return;
        }
	}

	public static void criarUtilizador() throws IOException, InterruptedException {

		
		Scanner input = new Scanner(System.in);

        System.out.println("Deseja criar um utilizador admin ou other?");
        String tipo = input.next();

        if(tipo.equals("admin"))
            criarUtilizadorAdmin();
        if(tipo.equals("other"))
            criarUtilizadorOther();
        
        if(!((tipo.equals("admin")) || (tipo.equals("other")))){
            System.out.println("Opção inválida!");
            TimeUnit.SECONDS.sleep(3);
            criarUtilizador();
            return;
        }
    }


    public static void criarUtilizadorAdmin() throws IOException, InterruptedException {

        File adminFile = new File ("Admin.txt");

        try {
            x = new Scanner(new File("Admin.txt"));
                } 
            catch(Exception e){
                System.out.println("couldnt find file");
            }

            try {
                if(!adminFile.exists()){
                    System.out.println("Ficheiro novo criado");
                    adminFile.createNewFile();
                }
            } catch(IOException e){
                System.out.println("Erro");
            }

            Scanner input = new Scanner(System.in);
            FileWriter admWriter = new FileWriter(adminFile, true);
            BufferedWriter admBuf = new BufferedWriter(admWriter);

            System.out.print("Username: ");
            String admUsername = input.next();
            System.out.print("Password:");
            String admPassword = input.next();

            while(x.hasNext()){

            String a = x.next();
            String b = x.next();

            if(admUsername.equals(a)){
                System.out.println("\033[1mO utilizador " + admUsername + " já existe!\033[0m");    // verificar se o nome ja existe
                TimeUnit.SECONDS.sleep(3);
                criarUtilizadorAdmin();
                return;
            }

                
            }

            admBuf.write(admUsername);      // escrever as credencias no ficheiro Admin.txt
            admBuf.write(" ");
            admBuf.write(admPassword);
            admBuf.write("\n");
            admBuf.flush();
            admBuf.close();

            System.out.println("\033[1mUtilizador admin criado com sucesso!\033[0m");

    }

    public static void criarUtilizadorOther() throws IOException, InterruptedException{

        File clientesFile = new File("Clientes.txt");

        try {
                y = new Scanner(new File("Clientes.txt"));
            }
            catch(Exception e){
                System.out.println("Ficheiro não encontrado");
            }

            try {
                if(!clientesFile.exists()){
                    System.out.println("Ficheiro novo criado");
                    clientesFile.createNewFile();
            }
            } catch(IOException e){
                System.out.println("Erro");
            }

            Scanner input = new Scanner(System.in);
            FileWriter clieWriter = new FileWriter(clientesFile, true);
            BufferedWriter clieBuf = new BufferedWriter(clieWriter);

            System.out.print("Nome: ");
            String clieNome = input.next();
            System.out.print("Email: ");
            String clieEmail = input.next();
            System.out.print("Username: ");
            String clieUsername = input.next();
            System.out.print("Password: ");
            String cliePassword = input.next();

            while(y.hasNext()){

                String a = y.next();
                String b = y.next();
                String c = y.next();
                String d = y.next();
                String e = y.next();

                if(clieUsername.equals(a)){
                    System.out.println("O username já existe!");
                    TimeUnit.SECONDS.sleep(3);
                    criarUtilizadorOther();
                }

                
            }

            clieBuf.write(clieNome);
            clieBuf.write(" ");
            clieBuf.write(clieEmail);
            clieBuf.write(" ");
            clieBuf.write(clieUsername);
            clieBuf.write(" ");
            clieBuf.write(cliePassword);
            clieBuf.write(" ");
            clieBuf.write("true");
            clieBuf.write("\n");
            clieBuf.flush();
            clieBuf.close();

            System.out.println("\033[1mUtilizador criado com sucesso!\033[0m");

        }

	public static void validarUtilizadores() throws IOException, InterruptedException{
        clearScreen();
        Scanner input = new Scanner(System.in);
        System.out.println("\nValidar todos os utilizadores ou um utilizador específico?\n");
        System.out.println("1) Um utilizador específico");
        System.out.println("2) Todos os utilizadores");
        System.out.print("> ");

        int num = input.nextInt();

        switch(num){

            case 1: validarUmUtilizador();
                    break;
            case 2: validarTodosUtilizadores();
                    break;
            default: System.out.println("\033[1mOpção inválida!\033[0m");
                    TimeUnit.SECONDS.sleep(2);
                    validarUtilizadores();

                    return;
        }

    }


    public static void validarUmUtilizador() throws IOException, InterruptedException{

        Scanner input = new Scanner(System.in);
        System.out.println("Qual utilizador deseja validar?");
        System.out.print("> ");
        String utilizador = input.next();

        File file = new File("Clientes.txt");
        FileWriter writer = new FileWriter("Clientes.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        boolean existe = false;

        try
             {
             
             String line = "", oldtext = "",linha="", newTexto="";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";
                 if(line.contains(utilizador)){
                    linha = line + "\r\n";
                    existe = true;
                }
             }
             reader.close();

             if(existe == false){       // mensagem caso o utilizador nao exista
                System.out.println("O utilizador não existe!");
                TimeUnit.SECONDS.sleep(2);
                validarUmUtilizador();
                return;
             }

            String newLinha = linha.replaceAll("false","true");     // mudar o false para true, fazendo com q o utilizador esteja validado


            newTexto = oldtext.replaceAll(linha,newLinha);            
            
            PrintWriter pw = new PrintWriter("Clientes.txt");
            writer.write(newTexto);writer.close();              // escrever o texto com a linha alterada no ficheiro
            
            System.out.println("Utilizador validado!");
         }
         
         catch (IOException ioe)
             {
             ioe.printStackTrace();
         }
    }

    public static void validarTodosUtilizadores() {

        try
             {
             File file = new File("Clientes.txt");
             BufferedReader reader = new BufferedReader(new FileReader(file));
             String line = "", oldtext = "";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";          // copiar o texto todo do ficheiro para oldtext
             }
             reader.close();

             String newtext = oldtext.replaceAll("false", "true");      // substituir todos os "false" do texto para "true"
            
            
            
             FileWriter writer = new FileWriter("Clientes.txt");
             writer.write(newtext);writer.close();              // substituir no ficheiro

             System.out.println("Utilizadores validados!");
         }
         catch (IOException ioe)
             {
             ioe.printStackTrace();
         }
    }

	public static void gerirUsers() throws IOException, InterruptedException {     // menu de gerir utilizadores

        listarUtilizadoresCliente();
        listarUtilizadoresAdmin();
        Scanner input = new Scanner(System.in);
        System.out.println("\n");
        System.out.println("1) Editar utilzadores");
        System.out.println("2) Remover utilizadores");
        int num = input.nextInt();

        switch(num){

            case 1: editarUtilizadores();
                    break;
            case 2: removerUtilizadores();
                    break;
            default: System.out.println("Opção inválida!");
                    TimeUnit.SECONDS.sleep(2);
                    gerirUsers();
                    break;
        }
	}

    public static void listarUtilizadoresCliente() throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        File file = new File("Clientes.txt");
        FileWriter writer = new FileWriter("Clientes.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int contador = 0;

        try {
            x = new Scanner(new File("Clientes.txt"));
                } 
            catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                TimeUnit.SECONDS.sleep(2);
                return;
                }

        System.out.println("\nClientes:\n");

        while(x.hasNext()){

            String a = x.next();
            String b = x.next();
            String c = x.next();
            String d = x.next();
            String e = x.next();

            contador++;
            System.out.println("> " + contador + ": " + c); // listar os nomes dos utilizadores
        }

        System.out.println("Existem " + contador + " utilizadores clientes");
    }

    public static void listarUtilizadoresAdmin() throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        File file = new File("Admin.txt");
        FileWriter writer = new FileWriter("Admin.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int contador = 0;

        try {
            x = new Scanner(new File("Admin.txt"));
                } 
            catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                TimeUnit.SECONDS.sleep(2);
                return;
                }

        System.out.println("\nAdmins:\n");
        while(x.hasNext()){

            String a = x.next();
            String b = x.next();

            contador++;
            System.out.println("> " + contador + ": " + a);     // listar os utilizadores admins
        }

        System.out.println("Existem " + contador + " utilizadores admins");
    }

    public static void editarUtilizadores() throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        System.out.println("Qual utilizador deseja editar?");
        System.out.print("> ");
        String utilizador = input.next();
        boolean existe = false;
        File file = new File("Clientes.txt");
        FileWriter writer = new FileWriter("Clientes.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        try
             {
             
             String line = "", oldtext = "",linha="", newTexto="";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";
                 if(line.contains(utilizador)){
                    linha = line + "\r\n";
                    existe = true;
                }
             }
             reader.close();

             if(existe == false){       // mensagem caso o utilizador nao existe
                System.out.println("O utilizador não existe!");
                TimeUnit.SECONDS.sleep(2);
                editarUtilizadores();
                return;
             }

            String nada = oldtext.replaceAll(oldtext,"");

            System.out.println("O que deseja alterar?\n");  
            System.out.println("1) Nome");
            System.out.println("2) Contacto / Email");
            System.out.println("3) Username");
            System.out.println("4) Password");
            String mudar = "", mudado = "";
            int escolha = input.nextInt();

            try {
            x = new Scanner(new File("Clientes.txt"));
                } 
            catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                }

            while(x.hasNext()){

                String a = x.next();
                String b = x.next();
                String c = x.next();
                String d = x.next();
                String e = x.next();

                if(utilizador.equals(c)){      // mensagem ao encontrar a linha do utilizador com o mesmo nome introduzido

                    if(escolha == 1){
                        System.out.print("Mudar nome próprio do utilizador " + utilizador + " para: ");
                        mudar = a;
                        mudado = input.next();
                    }

                    if(escolha == 2){
                        System.out.print("Mudar email do utilizador " + utilizador + " para: ");
                        mudar = b;
                        mudado = input.next();
                    }

                    if(escolha == 3){
                        System.out.print("Mudar username do utilizador " + c + " para: ");
                        mudar = c;
                        mudado = input.next();
                    }

                    if(escolha == 4){
                        System.out.print("Mudar password do utilizador " + c + " para: ");
                        mudar = d;
                        mudado = input.next();
                    }
                }
            }

            String newLinha = linha.replaceAll(mudar,mudado);       // mudar o que se quis alterar na linha do utilizador escolhido


            newTexto = oldtext.replaceAll(linha,newLinha);         // alterar a linha antiga pela alterada    
             
            PrintWriter pw = new PrintWriter("Clientes.txt");
            writer.write(newTexto);writer.close();                 // escrever no ficheiro

            System.out.println("Utilizador editado com sucesso!");
                
         }
         
         catch (IOException ioe)
             {
             ioe.printStackTrace();
         }


    }

    public static void removerUtilizadores() throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        System.out.println("Deseja remover um utilizador admin ou cliente?");
        System.out.print("> ");
        String escolha = input.next();

        if(escolha.equals("admin"))
            removerUtilizadoresAdmin();
        else if(escolha.equals("cliente"))
            removerUtilizadoresCliente();
        else {
            System.out.println("Opção inválida!");
            removerUtilizadoresCliente();
            return;
        }
    }

    public static void removerUtilizadoresCliente() throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        System.out.println("Qual utilizador deseja remover?");
        System.out.print("> ");
        String utilizador = input.next();
        boolean existe = false;
        File file = new File("Clientes.txt");
        FileWriter writer = new FileWriter("Clientes.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        try
             {
             
             String line = "", oldtext = "",linha="", newTexto="";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";
                 if(line.contains(utilizador)){
                    linha = line + "\r\n";
                    existe = true;
                }
             }
             reader.close();

             if(existe == false){       // codigo caso o utilizador escrito nao exista
                System.out.println("O utilizador não existe!");
                TimeUnit.SECONDS.sleep(2);
                removerUtilizadoresCliente();
                return;
             }

            newTexto = oldtext.replaceAll(linha,"");            // apagar a linha
             
             PrintWriter pw = new PrintWriter("Clientes.txt");
             writer.write(newTexto);writer.close();             // escrever no ficheiro

             System.out.println("Utilizador removido com sucesso!");
                
         }
         
         catch (IOException ioe)
             {
             ioe.printStackTrace();
         }

    }

    public static void removerUtilizadoresAdmin() throws IOException, InterruptedException {        // programa igual ao anteriror, mas remove os admins em vez dos clientes

        Scanner input = new Scanner(System.in);
        System.out.println("Qual utilizador deseja remover?");
        System.out.print("> ");
        String utilizador = input.next();
        boolean existe = false;
        File file = new File("Admin.txt");
        FileWriter writer = new FileWriter("Admin.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        try
             {
             
             String line = "", oldtext = "",linha="", newTexto="";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";
                 if(line.contains(utilizador)){
                    linha = line + "\r\n";
                    existe = true;
                }
             }
             reader.close();

             if(existe == false){   
                System.out.println("O utilizador não existe!");
                TimeUnit.SECONDS.sleep(2);
                removerUtilizadoresAdmin();
                return;
             }

            newTexto = oldtext.replaceAll(linha,"");            
             
             PrintWriter pw = new PrintWriter("Admin.txt");
             writer.write(newTexto);writer.close();

             System.out.println("Utilizador removido com sucesso!");
                
         }
         
         catch (IOException ioe)
             {
             ioe.printStackTrace();
         }

    }

	public static void verEstatisticas() throws IOException, InterruptedException {        // menu de estatisticas

        Scanner input = new Scanner(System.in);

        System.out.println("\nEstatísticas\n");
        System.out.println("1) Quantidade de tópicos ativos");
        System.out.println("2) Tópicos mais utilizados");
        System.out.println("3) Quantidade de mensagens de um determinado tópico");
        System.out.println("4) Número de utilizadores diferentes que interagem num tópico");
        System.out.println("5) Analisar os tópicos subscritos por um determinado utilizador");

        int num = input.nextInt();

        switch(num){
            case 1: topicosAtivos();
                    break;
            case 2: topicosMaisUtilizados();
                    break;
            case 3: mensagensDeUmTopico();
                    break;
            case 4: utilizadoresAtivos();
                    break;
            case 5: topicosSubscritos();
                    break;
        }
	}

    public static void topicosAtivos() throws IOException, InterruptedException  {

        Scanner input = new Scanner(System.in);
        File file = new File("Topicos.txt");
        FileWriter writer = new FileWriter("Topicos.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int contador = 0;

        try {
            x = new Scanner(new File("Topicos.txt"));
                } 
            catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                TimeUnit.SECONDS.sleep(2);
                return;
                }

        while(x.hasNext()){

            String a = x.next();
            String b = x.next();
            String c = x.next();

            contador++;         // percorrer o ficheiro e contar quantas linhas (ou seja, topicos) existem
        }

        System.out.println("Existem " + contador + " tópicos");
    }

    public static void topicosMaisUtilizados() throws IOException, InterruptedException  {  // metodo para visualizar os topicos mais utilizados

                int contador = 0;
                int maximo = 0;
                String nomes[] = new String[1000];
                int mensagens[] = new int[1000];
                int i = 0, j = 0;
                int temp = 0; 
                String tempo="";


                try {
                    x = new Scanner(new File("Topicos.txt"));
                } 
                catch(Exception e){
                    System.out.println("Ficheiro nao encontrado");
                    TimeUnit.SECONDS.sleep(2);
                    return;
                }

                while(x.hasNext()){

                String a = x.next();
                String b = x.next();
                String c = x.next();

                String line = "";

                File topico = new File(a+".txt");
                Scanner top = new Scanner(new File(a+".txt"));

                try {
                top = new Scanner(new File(a+".txt"));
                } 
                catch(Exception e){
                    System.out.println("Ficheiro nao encontrado");
                    TimeUnit.SECONDS.sleep(2);
                    return;
                }

                try {
                    y = new Scanner(new File(a+".txt"));
                }
                catch(Exception e){
                    System.out.println("Ficheiro nao encontrado");
                    TimeUnit.SECONDS.sleep(2);
                    return;
                }


                BufferedReader read = new BufferedReader(new FileReader(topico)); 

                while((line = read.readLine()) != null)
                {
                    contador++;     // contar quantas mensagens o topico tem
                }

                mensagens[i] = contador;        // atribuir a um array o numero de mensagens e a outro o nome dos topicos
                nomes[i] = a;
                contador = 0;
                i++;
            }
            for(i = 0; i < 100; i++){
                for(j = i+1; j < 100; j++){
                    if(mensagens[i] < mensagens[j]){        // ordenar os topicos com mais mensagens para os que têm menos
                        temp = mensagens[i];
                        mensagens[i] = mensagens[j];
                        mensagens[j]=temp;

                        tempo = nomes[i];
                        nomes[i] = nomes[j];
                        nomes[j] = tempo;
                }
            }
        }
            System.out.println("Tópicos mais ativos: ");

            for(j = 0;j<3;j++){
                System.out.println(nomes[j] + " com " + mensagens[j] + " mensagens");
            }

    }

    public static void mensagensDeUmTopico() throws IOException, InterruptedException { // metodo para ver mensagens de um determinado topico

        Scanner input = new Scanner(System.in);
        int contador = 0;
        System.out.println("Qual tópico quer ver?");
        String topico = input.next();
        boolean existe = false;

        File ficheiro = new File("Topicos.txt");

        try{
            x = new Scanner(new File("Topicos.txt"));
        }
        catch(Exception e){
            System.out.println("Ficheiro nao encontrado");
            TimeUnit.SECONDS.sleep(2);
            return;
        }

        while(x.hasNext()){

            String a = x.next();
            String b = x.next();
            String c = x.next();

            if(topico.equals(a))
                existe = true;      // verificar se existe
        }

        if(existe == false){        // se nao existir, executa isto
            System.out.println("O tópico não existe!");
            TimeUnit.SECONDS.sleep(2);
            mensagensDeUmTopico();
            return;
        }

        File file = new File(topico+".txt");

        BufferedReader reader = new BufferedReader(new FileReader(file));       // ler o ficheiro do topico escolhido
        
        try
             {
             
             String line = "";
             while((line = reader.readLine()) != null)
                {
                contador++;     // contar quantas linhas (mensagens) tem
                 
            }
            reader.close();

            System.out.println("O tópico " + topico + " tem " + contador + " mensagens");
                
        }
         
        catch (IOException ioe)
            {
            ioe.printStackTrace();
        }

    }

    public static void utilizadoresAtivos() throws IOException, InterruptedException {

        String nomes[] = new String[100];
        Scanner input = new Scanner(System.in);
        System.out.println("Qual tópico quer ver?");
        System.out.print("> ");
        String topico = input.next();
        File file = new File(topico+".txt");
        int i = 0;
        int contador = 0;
        boolean existe = false;
        boolean utilizador = false;

        File ficheiro = new File("Topicos.txt");

        try{
            x = new Scanner(new File("Topicos.txt"));
        }
        catch(Exception e){
            System.out.println("Ficheiro nao encontrado");
            TimeUnit.SECONDS.sleep(2);
            return;
        }

        while(x.hasNext()){

            String a = x.next();
            String b = x.next();
            String c = x.next();

            if(topico.equals(a))
                existe = true;  // verificar se o topico existe
        }

        if(existe == false){        // executar isto caso o topico escrito nao exista
            System.out.println("O tópico não existe!");
            TimeUnit.SECONDS.sleep(2);
            utilizadoresAtivos();
            return;
        }

        for(i=0;i<100;i++){
            nomes[i] = "nada";
        }

        try {
                x = new Scanner(new File(topico+".txt"));
            } 
            catch(Exception e){
                System.out.println("Erro");
                TimeUnit.SECONDS.sleep(2);
                return;
            }

        BufferedReader read = new BufferedReader(new FileReader(file)); 

        String line = "";

        while(x.hasNext()){

            String a = x.next();
            x.nextLine();

            for(i=0; i<100;i++){        // comparar se os nomes de utilizador que aparecem, mensagem a mensagem, sao repetidos. Contar a quantidade de nomes diferentes existentes
                if(nomes[i].equals(a))
                    utilizador = true;
                }
            if(utilizador == false){
                nomes[contador] = a;
                contador++;
            }
        }
        
        System.out.println("Existem " + contador + " utilizadores no tópico " + topico);

    }


    public static void topicosSubscritos() throws IOException, InterruptedException {   // metodo para ver quais topicos um utilizador subscreve

        Scanner input = new Scanner(System.in);
        System.out.println("Qual utilizador pretende ver?");
        System.out.print("> ");
        String utilizador = input.next();
        File file = new File(utilizador+".txt");
        BufferedReader read = new BufferedReader(new FileReader(file));
        BufferedReader reader = new BufferedReader(new FileReader("Clientes.txt"));
        boolean existe = false;
        int contador = 1;

        String line = "";
        while((line = reader.readLine()) != null)
            {
                if(line.contains(utilizador))   // verificar se o utilizador existe
                    existe = true;
            }
        if(existe == false){
            System.out.println("O utilizador não existe!");     // se o utilizador nao existir, executa isto
            TimeUnit.SECONDS.sleep(2);
            topicosSubscritos();
            return;
        }

        try {
            x = new Scanner(new File(utilizador+".txt"));
        }
        catch(Exception e){
            System.out.print("Nao foi encontrado o utilizador ou o utilizador não subscreve nenhum tópico");
            TimeUnit.SECONDS.sleep(2);
            return;
        }

        while(x.hasNext()){

            String a = x.next();
            String b = x.next();

            System.out.println(contador + ": " + a);    // listar os topicos que um utilizador subscreve
            contador++;
        }
    }


	public static void main(String[] args) throws IOException, InterruptedException {

        boolean estado = menuDeLogin();

        while(estado == true){      // ciclo que funciona enquanto o login nao estiver terminado
            estado = menu();
            }
        }
    }
