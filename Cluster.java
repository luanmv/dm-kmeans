package kMeans;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import weka.core.Instances;

/**
 *
 * @author Luis Angel Martínez Valdez
 * @email mtzvaldezluis@gmail.com
 *
 */
public class Cluster {

	static int k;
	static String[] centroides_cluster;
	static String temporal[];
	static String[] centroides_final;
	static int[] valores;
	
	public Cluster(int k)
	{
		this.k = k;
		centroides_cluster = new String[k];
		centroides_final = new String[k];
	}

	private static void revolver(Instances dataInstances) {
		valores = new int[dataInstances.numInstances()];
		for (int i = 0; i < valores.length; i++) {
			valores[i] = i;
		}

		for (int i = 0; i < valores.length; i++) {
			int posAleatoria = (int) Math.random() * dataInstances.numInstances();
			int temp = valores[i];
			valores[i] = valores[posAleatoria];
			valores[posAleatoria] = temp;
		}
	}

	protected static void calcular_centroides(String[] centroides, Instances dataInstances) {
		for (int i = 0; i < k; i++) {
			centroides[i] = dataInstances.get(i).toString();
			centroides_cluster[i]= centroides[i];
		}
	}
	
	
/*	protected static void calcular_centroides(String[] centroides, Instances dataInstances) {
		revolver(dataInstances);
		for (int i = 0; i < k; i++) {
			temporal = dataInstances.get(valores[i]).toString().split(",");
			String centro = "";
			for (int j = 0; j < temporal.length - 1; j++) {
				if (j < temporal.length - 2) {
					centro += temporal[j] + ",";
				} else {
					centro += temporal[j];
				}
			}
			centroides[i] = centro;
			centroides_cluster[i] = centroides[i];
		}
	} */

	protected static String imprimir_centroides_iniciales() {
		String imprimir = "";
		imprimir += "\nCentroides iniciales:\n";
		for (int i = 0; i < centroides_cluster.length; i++) {
			imprimir += centroides_cluster[i] + "\n";
		}
		return imprimir;
	}

	protected static void llenar_matriz(float matriz[][], Instances dataInstances, String[] centroides) {
		for (int i = 0; i < dataInstances.numInstances(); i++) {
			for (int j = 0; j < k; j++) {
				matriz[i][j] = calcular__nuevos__valores(centroides[j], dataInstances.get(i).toString());
			}
		}
	}

	protected static float calcular__nuevos__valores(String centroide, String attribute) {
		String[] arregloCentroide, arregloAttribute;
		float result = 0;
		arregloCentroide = centroide.split(",");
		arregloAttribute = attribute.split(",");
		for (int i = 0; i < arregloAttribute.length - 1; i++) {
			result += (Math.pow((Float.parseFloat(arregloCentroide[i]) - Float.parseFloat(arregloAttribute[i])), 2));
		}
		return result;
	}

	protected static void clustering(float[][] matriz, float[][] matriz_cluster, Instances dataInstances) {
		for (int i = 0; i < dataInstances.numInstances(); i++) {
			float menor = matriz[i][0];
			float indice = 0;
			for (int j = 0; j < k + 1; j++) {
				if (j < k) {
					if (menor > matriz[i][j]) {
						menor = matriz[i][j];
						indice = j;
					}
					matriz_cluster[i][j] = matriz[i][j];
				} else {
					matriz_cluster[i][j] = indice;
				}
			}
		}
	}

	protected static void imprimir_matriz(Instances dataInstances, float[][] matriz_cluster, PrintWriter print) {

		for (int i = 0; i < dataInstances.numInstances(); i++) {
			for (int j = 0; j < k + 1; j++) {
				System.out.printf("%f\t", matriz_cluster[i][j]);
				print.printf("%f\t", matriz_cluster[i][j]);
			}
			System.out.println();
			print.println();
		}
	}


	protected static void calcular_nuevos_centroides(Instances dataInstances, float[][] matriz_cluster,
			String[] centroides) {
		// System.out.println("Centroides:");
		for (int i = 0; i < k; i++) {
			String centroide = "";
			for (int j = 0; j < dataInstances.numAttributes() - 1; j++) {
				int contador = 0;
				float suma = 0;
				for (int m = 0; m < dataInstances.numInstances(); m++) {
					if (matriz_cluster[m][k] == i) {
						contador++;
						suma += Float.parseFloat(dataInstances.get(m).toString().split(",")[j]);
					}
				}
				centroide += ""+(contador == 0 ? 0 : suma / contador)+",";
			}
			centroides[i] = centroide;
			centroides_final[i] = centroides[i];
		}
	}

	protected static void llenar_valores_cluster(Instances dataInstances, float[] orden1, float[][] matriz_cluster) {
		for (int j = 0; j < dataInstances.numInstances(); j++) {
			orden1[j] = matriz_cluster[j][k];
		}
	}

	protected static void llenar_orden2(float[] orden2) {
		for (int i = 0; i < orden2.length; i++) {
			orden2[i] = 0.0f;
		}
	}

	protected static boolean parar(float[] orden1, float[] orden2) {
		boolean parar = true;
		for (int i = 0; i < orden2.length; i++) {
			if (orden1[i] != orden2[i]) {
				parar = false;
			}
		}
		return parar;
	}

	protected static void cambiar_orden(float[] orden1, float[] orden2) {
		for (int i = 0; i < orden2.length; i++) {
			orden2[i] = orden1[i];
		}
	}

	protected static String datos_por_cluster(Instances dataInstances, float[][] matriz_cluster) {
		String imprimir = "";
		for (int i = 0; i < k; i++) {
			int contador = 0;
			for (int j = 0; j < dataInstances.numInstances(); j++) {
				if (matriz_cluster[j][k] == i) {
					contador++;
				}
			}
			imprimir += "Cluster " + i + ": " + contador + "\n";
		}
		return imprimir;
	}

	protected static String imprimir_centroides_finales(Instances dataInstances) {
		String imprimir = "";
		DecimalFormat df = new DecimalFormat("0.0000");
		imprimir += "\n";
		imprimir += "Centroides Finales\n";
		imprimir += "Atributos\t";
		for (int i = 0; i < k; i++) {
			imprimir += i + "\t\t";
		}
		imprimir += "\n";
		for (int i = 0; i < dataInstances.numAttributes() - 1; i++) {
			imprimir += dataInstances.attribute(i).name()
					+ (dataInstances.attribute(i).name().equals("desvrojo") ? "\t\t" : "\t\t");
			for (String centroides_final1 : centroides_final) {
				imprimir += df.format(Float.parseFloat(centroides_final1.split(",")[i])) + "\t\t";
			}
			imprimir += "\n";
		}
		return imprimir;
	}
}
