import random
from math import pow
from math import sqrt

clusters 	= 10
objects 	= 10000
attributes 	= 5
continuar 	= True

# Funcion para iniciar las estructuras
def iniciarEstructuras(centroides, objetos, orden1, orden2, matrizCluster):
	for i in xrange(clusters):
		centroides.append([0] * attributes)

	for i in xrange(objects):
		objetos.append([0] * attributes)
		orden1.append([0])
		orden2.append([0])
		matrizCluster.append([0] * clusters)


# Funcion para llenar los centroides iniciales
def centroidesIniciales(centroides):
	for x in xrange(clusters):
		for y in xrange(attributes):
			centroides[x][y] = random.randint(1, 15)


# Funcion para llenar los objetos iniciales
def objetosIniciales(objetos):
	for x in xrange(objects):
		for y in xrange(attributes):
			objetos[x][y] = random.randint(1, 150)

# Funcion para llenar el orden de los cluster iniciales
def ordenInicial(orden1):
	for x in xrange(objects):
		orden1[x] = 1.0


# Funcion para llenar la matriz de cluster con distancias euclideanas
def llenarMatriz(matrizCluster, centroides, objectos):
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

# Funcion para calcular el orden de los clusters
def ordenCluster(orden2, matrizCluster):
	for x in xrange(objects):
		orden2[x] = matrizCluster[x][clusters - 1]

# Funcion para calcular nuevos centroides
def calcularNuevosCentroides(matrizCluster, objetos, centroides):
	for i in xrange(clusters - 1):
		p0 = 0.0
		p1 = 0.0
		p2 = 0.0
		p3 = 0.0
		p4 = 0.0
		contador = 0
		for j in xrange(objects):
			if matrizCluster[j][clusters-1] == i:
				contador+=1
				for k in xrange(attributes):
					if k == 0:
						p0 += objetos[j][k]
					if k == 1:
						p1 += objetos[j][k]
					if k == 2:
						p2 += objetos[j][k]
					if k == 3:
						p3 += objetos[j][k]
					if j == 4:
						p4 += objetos[j][k]
		for l in xrange(attributes):
			if l == 0:
				centroides[i][l] = centroideValue(p0, contador)
			if l == 1:
				centroides[i][l] = centroideValue(p1, contador)
			if l == 2:
				centroides[i][l] = centroideValue(p2, contador)
			if l == 3:
				centroides[i][l] = centroideValue(p3, contador)
			if l == 4:
				centroides[i][l] = centroideValue(p4, contador)


# Funcion para obtener el valor del centroide
def centroideValue(p, contador):
	if contador == 0:
		return 0
	else:
		return p/contador

# Funcion para detener el algoritmo
def pararAlgoritmo(orden1, orden2):
	suma1 = 0.0
	suma2 = 0.0
	for x in xrange(objects):
		suma1 += orden1[x]
		suma2 += orden2[x]
	resultado = abs(suma1 - suma2)
	umbral = suma1 * 0.00000005
	if resultado > umbral:
		return False
	else:
		return True

# Funcion para impresion de resultados
def mostrarResultados(matrizCluster):
	for x in xrange(clusters - 1):
		contador = 0
		for y in xrange(objects):
			if x == matrizCluster[y][clusters - 1]:
				contador += 1
		print('Cluster ' + str(x) + ': ' + str(contador))


# Funcion para cambiar el orden
def swap(orden1, orden2):
	for x in xrange(objects):
		orden2[x] = orden1[x]

# Aqui inicia el algoritmo
centroides = []
objetos = []
orden1 = []
orden2 = []
matrizCluster = []
iniciarEstructuras(centroides, objetos, orden1, orden2, matrizCluster)
centroidesIniciales(centroides)
objetosIniciales(objetos)
ordenInicial(orden1)
i = 1
while continuar:
	llenarMatriz(matrizCluster, centroides, objetos)
	clustering(matrizCluster)
	ordenCluster(orden2, matrizCluster)
	calcularNuevosCentroides(matrizCluster, objetos, centroides)
	continuar = pararAlgoritmo(orden1, orden2)
	if continuar == False:
		print('Iteraciones: ' + str(i))
		mostrarResultados(matrizCluster)
	else:
		i+=1
		swap(orden1, orden2)