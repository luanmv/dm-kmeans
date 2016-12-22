import random
from math import pow
from math import sqrt

clusters 	= 10
objects 	= 100
attributes 	= 5
continuar 	= True

# Funcion para llenar los centroides iniciales
def centroidesIniciales(centroides):
	for i in xrange(clusters):
		centroides.append([0] * attributes)

	for x in xrange(clusters):
		for y in xrange(attributes):
			centroides[x][y] = random.randint(1, 15)


# Funcion para llenar los objetos iniciales
def objetosIniciales(objetos):
	for i in xrange(objects):
		objetos.append([0] * attributes)

	for x in xrange(objects):
		for y in xrange(attributes):
			objetos[x][y] = random.randint(1, 150)

# Funcion para llenar el orden de los cluster iniciales
def ordenInicial(orden1):
	for x in xrange(objects):
		orden1.append([0])
		orden1[x] = 1.0


# Funcion para llenar la matriz de cluster con distancias euclideanas
def llenarMatriz(matrizCluster, centroides, objectos):
	for x in xrange(objects):
		matrizCluster.append([0] * clusters)

	for i in xrange(objects):
		for j in xrange(clusters - 1):
			matrizCluster[i][j] = distanciaEuclideana(objetos, centroides, i, j)


# Funcion para la distancia auclideana
def distanciaEuclideana(objetos, centroides, i, j):
	resultado = 0
	for x in xrange(attributes):
		resultado += pow((centroides[j][x] - objetos[i][x]), 2)
	return sqrt(resultado)

# Funcion para el clustering (elegir en que cluster fue asignado en la corrida)
def clustering(matrizCluster):
	for x in xrange(objects):
		menor = matrizCluster[x][0]
		indice = 0
		for y in xrange(clusters):
			if y < clusters - 1:
				if menor > matrizCluster[x][y]:
					menor = matrizCluster[x][y]
					indice = y
			else:
				matrizCluster[x][y] = indice


# Aqui inicia el algoritmo
centroides = []
objetos = []
orden1 = []
matrizCluster = []
centroidesIniciales(centroides)
objetosIniciales(objetos)
ordenInicial(orden1)
llenarMatriz(matrizCluster, centroides, objetos)
clustering(matrizCluster)
#while continuar:
#	continuar = False
print matrizCluster