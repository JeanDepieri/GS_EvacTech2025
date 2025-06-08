package aplicacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;

class Sensor {
	String coordenada;
	int hashCode;
	double inclinacao;
	int umidade;

	public Sensor(String coordenada, int hashCode) {
		this.coordenada = coordenada;
		this.hashCode = hashCode;
		this.inclinacao = 0.0;
		this.umidade = 0;
	}

	@Override
	public String toString() {
		return "Coordenada: " + coordenada + ", Hash: " + hashCode + ", Inclinação: " + inclinacao + " mm/mês, Umidade: " + umidade + "%";
	}
}

public class RotaSegura {

	public static Scanner le = new Scanner(System.in);
	public static Sensor[] sensores = new Sensor[20];
	public static int n = 0;

	public static int gerarHash(String coordenadas) {
	    coordenadas = coordenadas.replace("’", "'")
	                             .replace("”", "\"")
	                             .replace("“", "\"")
	                             .replace(" ", "");

	    if (coordenadas.length() < 22) {
	        System.out.println("[ERRO] Coordenada muito curta: " + coordenadas);
	        return 0; 
	    }

	    char indice6 = coordenadas.charAt(6);
	    char indice7 = coordenadas.charAt(7);
	    char indice16 = coordenadas.charAt(16);
	    char indice17 = coordenadas.charAt(17);
	    char indice20 = coordenadas.charAt(20);
	    char indice21 = coordenadas.charAt(21);

	    String hash = "" + indice6 + indice7 + indice16 + indice17 + indice20 + indice21;

	    try {
	        return Integer.parseInt(hash);
	    } catch (NumberFormatException e) {
	        System.out.println("[ERRO] Hash gerado não é numérico: " + hash);
	        return 0;
	    }
	}

	public static int geraVetor() {
		String caminhoDoArquivo = "src/aplicacao/CoordenadasCotas.txt";
		try {
			File arquivo = new File(caminhoDoArquivo);
			Scanner leArq = new Scanner(arquivo);
			n = 0;
			while (leArq.hasNextLine() && n < sensores.length) {
				String linha = leArq.nextLine();
				int hash = gerarHash(linha);
				sensores[n] = new Sensor(linha, hash);
				n++;
			}
			leArq.close();
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado: " + e.getMessage());
		}
		return n;
	}

	public static void lerMedidas(Sensor[] sensores, int n) {
		String caminho = "src/aplicacao/medidas.txt";
		try {
			File arquivo = new File(caminho);
			Scanner leitor = new Scanner(arquivo);
			int i = 0;
			while (leitor.hasNextLine() && i < n) {
				String linha = leitor.nextLine();
				String[] partes = linha.split(";");
				if (partes.length == 2) {
					sensores[i].inclinacao = Double.parseDouble(partes[0].replace(",", "."));
					sensores[i].umidade = Integer.parseInt(partes[1]);
				}
				i++;
			}
			leitor.close();
		} catch (Exception e) {
			System.out.println("Erro ao ler medidas: " + e.getMessage());
		}
	}

	public static void ordenarPorHash(Sensor[] sensores, int n) {
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (sensores[j].hashCode > sensores[j + 1].hashCode) {
					Sensor temp = sensores[j];
					sensores[j] = sensores[j + 1];
					sensores[j + 1] = temp;
				}
			}
		}
	}

	public static int buscaBinaria(Sensor[] sensores, int n, int hashBuscado) {
		int inicio = 0, fim = n - 1;
		while (inicio <= fim) {
			int meio = (inicio + fim) / 2;
			if (sensores[meio].hashCode == hashBuscado)
				return meio;
			else if (hashBuscado < sensores[meio].hashCode)
				fim = meio - 1;
			else
				inicio = meio + 1;
		}
		return -1;
	}

	public static void main(String[] args) {

		geraVetor();
		lerMedidas(sensores, n);
		ordenarPorHash(sensores, n);

		int opcao;
		do {
			System.out.println("0 - Sair");
			System.out.println("1 - Apresentacao dos pontos de monitoramento");
			System.out.println("2 - Inserir novo ponto de monitoramento");
			System.out.println("3 - Atualizar medidas");
			System.out.println("4 - Buscar ponto pelo hash");
			System.out.println("5 - Mostrar pontos em risco (inclinação > 10, umidade > 30%)");
			opcao = le.nextInt();
			le.nextLine();

			switch (opcao) {
			case 0:
				System.out.println("Fechando o sistema...");
				break;
			case 1:
				for (int i = 0; i < n; i++) System.out.println(sensores[i]);
				break;
			case 2:
				if (n >= sensores.length) {
					System.out.println("Limite de sensores atingido.");
					break;
				}
				System.out.print("Digite a coordenada (ex: 23o46'09'S45o41'15'W47m): ");
				String novaCoord = le.nextLine();
				int novoHash = gerarHash(novaCoord);
				System.out.print("Digite a inclinacao (mm/mês): ");
				String incStr = le.next().replace(",", ".");
				double novaInc = Double.parseDouble(incStr);
				System.out.print("Digite a umidade (%): ");
				int novaUmi = le.nextInt();
				Sensor novo = new Sensor(novaCoord, novoHash);
				novo.inclinacao = novaInc;
				novo.umidade = novaUmi;
				sensores[n] = novo;
				n++;
				ordenarPorHash(sensores, n);
				break;
			case 3:
				lerMedidas(sensores, n);
				break;
			case 4:
				System.out.print("Digite o hash a buscar: ");
				int h = le.nextInt();
				int pos = buscaBinaria(sensores, n, h);
				if (pos >= 0) System.out.println(sensores[pos]);
				else System.out.println("Sensor nao encontrado.");
				break;
			case 5:
				for (int i = 0; i < n; i++) {
					if (sensores[i].inclinacao > 10 && sensores[i].umidade > 30)
						System.out.println(sensores[i]);
				}
				break;
			default:
				System.out.println("Opcao invalida.");
			}
		} while (opcao != 0);
		le.close();
	}
} 
