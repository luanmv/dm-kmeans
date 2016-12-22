/*
    author: Luís Angel Martínez Valdez
    version: v1.0
    e-mail: mtzvaldezluis@gmail.com ; mtzvaldezluis@outlook.com ;
    ingeniería en sistemas computacionales ITCM
    algorithm: sequential kMeans Clustering
*/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

/* constantes */
#define CLUSTERS        10
#define OBJECTS         1000
#define ATTRIBUTES      5
#define TRUE            1
#define FALSE           0

struct Matriz
{
	float atributos[ATTRIBUTES];
};

struct Matriz_Cluster
{
	float clusters[CLUSTERS];
};

struct Centroides
{
	float atributos[ATTRIBUTES];
};

typedef struct Matriz M;
typedef struct Matriz_Cluster MC;
typedef struct Centroides C;

/* funciones */
void 	__obtener__centroides__iniciales(C centroides[]);
void 	__imprimir__Centroides(C centroides[]);
void 	__iniciar__matriz(M matriz[]);
void 	__llenar__orden2(float orden[]);
void 	__llenar__matriz(MC matriz_cluster[], C centroides[], M matriz[]);
float 	__distancia__euclideana(M matriz[], C centroides[], int i, int j);
void 	__imprimir__matriz(MC matriz_cluster[]);
void 	__clustering(MC matriz_cluster[]);
int 	__es__cero(int var);
void 	__calcular__nuevos__centroides(MC matriz_cluster[], M matriz[], C centroides[], float orden1[]);
int 	__parar(float orden1[], float orden2[]);
void 	__cambiar__orden(float orden1[], float orden2[]);


int main(int argc, char *argv[])
{
	C centroides[CLUSTERS];
	M matriz[OBJECTS];
	MC matriz_cluster[OBJECTS];
	float orden1[OBJECTS], orden2[OBJECTS];
	__iniciar__matriz(matriz);
	__obtener__centroides__iniciales(centroides);
	__llenar__orden2(orden2);
	unsigned int i;
	for(i = 0; i < OBJECTS; i++)
	{
		__llenar__matriz(matriz_cluster, centroides, matriz);
		__clustering(matriz_cluster);
		__calcular__nuevos__centroides(matriz_cluster, matriz, centroides, orden1);
		if (__parar(orden1, orden2) == TRUE)
		{
			printf("Iteraciones %d\n", i+1);
			unsigned int k;
			unsigned int j;
			for(j = 0; j < CLUSTERS - 1; j++)
			{
				unsigned int contador = 0;
				for(k = 0; k < OBJECTS; k++)
				{
					if (j == matriz_cluster[k].clusters[CLUSTERS - 1])
					{
						contador += 1;
					}
				}
				printf("Cluster %d: %d\n", j, contador);
			}
			break;
		}
		else
		{
			__cambiar__orden(orden1, orden2);
		}
	}
	return 0;
}

void __obtener__centroides__iniciales(C centroides[])
{
	srand(time(NULL));
	unsigned int i, j;
	for(i = 0; i < CLUSTERS; i++)
	{
		for(j = 0; j < ATTRIBUTES; j++)
		{
			centroides[i].atributos[j] = (rand()%100) + 1;
		}
	}
}

void __imprimir__Centroides(C centroides[])
{
	unsigned int i, j;
	for(i = 0; i < CLUSTERS; i++)
	{
		for(j = 0; j < ATTRIBUTES; j++)
		{
			printf("%f\t", centroides[i].atributos[j]);
		}
		printf("\n");
	}
}

void __iniciar__matriz(M matriz[])
{
	srand(time(NULL));
	unsigned int i, j;
	for(i = 0; i < OBJECTS; i++)
	{
		for(j = 0; j < ATTRIBUTES; j++)
		{
			matriz[i].atributos[j] = (rand()%15) + 1;
		}
	}
}

void __llenar__orden2(float orden[])
{
	unsigned int i;
	for(i = 0; i < OBJECTS; i++)
		orden[i] = 1.0;
}

void __llenar__matriz(MC matriz_cluster[], C centroides[], M matriz[])
{
	unsigned int i, j;
	for(i = 0; i < OBJECTS; i++)
	{
		for(j = 0; j < CLUSTERS - 1; j++)
		{
			matriz_cluster[i].clusters[j] = __distancia__euclideana(matriz, centroides, i, j);
		}
	}
}

float __distancia__euclideana(M matriz[], C centroides[], int i, int j)
{
	float resultado = 0;
	unsigned int l;
	for(l = 0; l < ATTRIBUTES; l++)
		resultado += pow((centroides[j].atributos[l] - matriz[i].atributos[l]),2);
	return sqrt(resultado);
}

void __imprimir__matriz(MC matriz_cluster[])
{
	unsigned int i, j;
	for(i = 0; i < OBJECTS; i++)
	{
		for(j = 0; j < CLUSTERS; j++)
		{
			printf("%f\t", matriz_cluster[i].clusters[j]);
		}
		printf("\n");
	}
}

void __clustering(MC matriz_cluster[])
{
	unsigned int i, j;
	for(i = 0; i < OBJECTS; i++)
	{
		float menor = matriz_cluster[i].clusters[0];
		unsigned int indice = 0;
		for(j = 0; j < CLUSTERS; j++)
		{
			if (j < CLUSTERS - 1)
			{
				if (menor > matriz_cluster[i].clusters[j])
				{
					menor = matriz_cluster[i].clusters[j];
					indice = j;
				}
			}
			else
				matriz_cluster[i].clusters[j] = indice;
		}
	}
}

void __calcular__nuevos__centroides(MC matriz_cluster[], M matriz[], C centroides[], float orden1[])
{
	unsigned int i, j, k;
	for(i = 0; i < CLUSTERS - 1; i++)
	{
		float p0 = 0.0f, p1 = 0.0f, p2 = 0.0f, p3 = 0.0f, p4 = 0.0f;
		unsigned int contador = 0;
		for(j = 0; j < OBJECTS; j++)
		{
			if (matriz_cluster[j].clusters[CLUSTERS - 1] == i)
			{
				contador+=1;
				for(k = 0; k < ATTRIBUTES; k++)
				{
					switch(k)
					{
						case 0:
							p0 += matriz[j].atributos[k];
						break;
						case 1:
							p1 += matriz[j].atributos[k];
						break;
						case 2:
							p2 += matriz[j].atributos[k];
						break;
						case 3:
							p3 += matriz[j].atributos[k];
						break;
						default:
							p4 += matriz[j].atributos[k];
						break;
					}
				}
			}
			orden1[j] = matriz_cluster[j].clusters[CLUSTERS - 1];
		}
		unsigned int l;
		for(l = 0; l < ATTRIBUTES; l++)
		{
			switch(l)
			{
				case 0: 
					centroides[i].atributos[l] = __es__cero(contador) == TRUE ? 0.0f : p0/contador;
				break;
				case 1:
					centroides[i].atributos[l] = __es__cero(contador) == TRUE ? 0.0f : p1/contador;
				break;
				case 2:
					centroides[i].atributos[l] = __es__cero(contador) == TRUE ? 0.0f : p2/contador;
				break;
				case 3:
					centroides[i].atributos[l] = __es__cero(contador) == TRUE ? 0.0f : p3/contador;
				break;
				default:
					centroides[i].atributos[l] = __es__cero(contador) == TRUE ? 0.0f : p4/contador;
				break;
			}
		}
	}
}

int __es__cero(int var)
{
	return var == 0 ? TRUE : FALSE;
}

int __parar(float orden1[], float orden2[])
{
	unsigned int parar = TRUE;
	double suma1 = 0.0f;
	double suma2 = 0.0f;
	double resultado, umbral;
	unsigned int i;
	for(i = 0; i < OBJECTS; i++)
	{
		suma1 += orden1[i];
		suma2 += orden2[i];
	}
	resultado = fabs(suma1 - suma2);
	umbral = suma1 * 0.00000005;
	if (resultado > umbral)
	{
		parar = FALSE;
	}

	return parar;
}

void __cambiar__orden(float orden1[], float orden2[])
{
	unsigned int i;
	for(i = 0; i < OBJECTS; i++)
	{
		orden2[i] = orden1[i];
	}
}