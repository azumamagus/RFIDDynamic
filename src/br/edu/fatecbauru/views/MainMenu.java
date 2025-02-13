package br.edu.fatecbauru.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import br.edu.fatecbauru.controllers.Cadastro;
import br.edu.fatecbauru.controllers.Campo;
import br.edu.fatecbauru.controllers.RFID;
import br.edu.fatecbauru.controllers.util.InfoUtils;
import br.edu.fatecbauru.controllers.util.Resources;

public class MainMenu {
	/** inicialia a classe Scanner para receber entradas do usu�rio */
	Scanner userInput = new Scanner(System.in);

	/** Definindo n�meros correspodentes as op��es de menu */
	final int MENU_CONECTAR = 1;
	final int MENU_DEFINIR_CADASTRO = 2;
	final int MENU_CADASTRAR = 3;
	final int MENU_LER_DADOS = 4;
	final int MENU_SOBRE = 5;
	final int MENU_SAIR = 6;

	/**
	 * Inicializando cont�udo do menu em um {@code TreeMap} para garantir a
	 * ordem correta (crescente) das op��es
	 */
	Map<Integer, String> menu = new TreeMap<Integer, String>() {
		{
			put(MENU_CONECTAR, "Conectar-se ao RFID");
			put(MENU_DEFINIR_CADASTRO, "Definir cadastro");
			put(MENU_CADASTRAR, "Cadastrar cadastro definido");
			put(MENU_LER_DADOS, "Ler cadastro definido");
			put(MENU_SOBRE, "Sobre");
			put(MENU_SAIR, "Sair");
		}
	};

	/**
	 * Mostra o menu na tela de forma j� formatada em ordem crescente das op��es
	 */
	public void printMenu() {
		Iterator<Map.Entry<Integer, String>> iterator = menu.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<Integer, String> entry = iterator.next();
			System.out.println("(" + entry.getKey() + ") : " + entry.getValue());
		}
	}

	RFID rfid = new RFID();

	/** conecta ao RFID */
	public void conectar() {
		try {
			int porta = rfid.conectar();
			System.out.println("Conectado na porta : " + porta);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	
	/**
	 * Define o cadastro a ser enviado para o RFID
	 */
	public void definirCadastro() {

		if (Cadastro.getInstance().getQtdCampos() > 0) {
			while (true) { //controla a valida��o da op��o
				System.out.println("J� h� campos definidos, deseja redefinir o cadastro? " + Resources.getOpcoesFormatadas());
				String opcao = userInput.next(); userInput.nextLine();
				if (opcao.toUpperCase().charAt(0) == Resources.OPCAO_SIM) {
					Cadastro.getInstance().redefinir();
					System.out.println("Cadastro redefinido.");
					break;
				} else if (opcao.toUpperCase().charAt(0) == Resources.OPCAO_NAO) {
					return;
				}
			}
		}
		
		while (true) { //controla o numero de campos que ser�o cadastros pelo usu�rio
			Resources.clearScreen();
			System.out.println("Digite o nome do campo: ");
			String nomeCampo = userInput.nextLine();
			if (nomeCampo.length() <= 0) {
				System.out.println("Entrada vazia digite novamente.");
				continue;
			}

			Cadastro.getInstance().addCampo(nomeCampo);
			
			System.out.println("Deseja cadastrar um novo campo? " + Resources.getOpcoesFormatadas());
			String opcao = userInput.next(); userInput.nextLine();
			if (opcao.toUpperCase().charAt(0) != Resources.OPCAO_SIM){
				break;
			}

		}
		
		System.out.println("Cadastro definido com �xito");
	}
	
	
	public void cadastrar(){
		Iterator<Campo> iterator = Cadastro.getInstance().getCampos().iterator();

		while (iterator.hasNext()) {
			Campo campo = iterator.next();
			System.out.println("Digite o cont�udo do campo " + campo.getNome() + ":");
			String valor = userInput.next(); userInput.nextLine();
			Cadastro.getInstance().addCampo(campo.getNome(), valor);
		}
		
		
		//gravar cadastro no RFID
		Cadastro.getInstance().persistirDadosRFID(rfid);
			
	}
	
	public void lerDados(){
	   Cadastro.getInstance().lerDadosRFID(rfid);
	}
	
	/** Imprime na tela os nomes dos colaboradores deste projeto */
	public void sobre() {
		System.out.println("\n");
		System.out.println("Autores: " + InfoUtils.getAuthors());
		System.out.println("Cordenadores: " + InfoUtils.getCordinators());
	}

	public MainMenu() {
		int value = 0;

		do {
			System.out.println(".:::. " + InfoUtils.getProjectName() + " vers�o: " + InfoUtils.getVersion() + ".:::.");
			printMenu();
			System.out.println("Digite a op��o: ");
			value = userInput.nextInt();
			userInput.nextLine();
			switch (value) {
			case MENU_CONECTAR:
				conectar();
				break;
			case MENU_DEFINIR_CADASTRO:
			    definirCadastro();
				break;
			case MENU_CADASTRAR:
				cadastrar();
				break;
			case MENU_LER_DADOS:
				lerDados();
				break;
			case MENU_SOBRE:
				break;
			case MENU_SAIR:
				break;
			default:
				break;
			}

		} while (value != MENU_SAIR);

	}
}
