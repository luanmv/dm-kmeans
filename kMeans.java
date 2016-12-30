package kMeans;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

/**
 *
 * @author Luis Angel Martínez Valdez
 * @email mtzvaldezluis@gmail.com
 *
 */
public class kMeans {

    private static File file;
    private static Instances dataInstances;
    private static String RUTA_SALIDA = "Results.txt";
    private static float[] orden1, orden2;
    protected static int K;

    public static String kMeansClustering(String RUTA_ENTRADA, int k) throws IOException {
        String imprimir = "";
        K = k;
        Cluster c = new Cluster(K);
        dataInstances = new Instances(new BufferedReader(new FileReader(RUTA_ENTRADA)));
        orden1 = new float[dataInstances.numInstances()];
        orden2 = new float[dataInstances.numInstances()];
        c.llenar_orden2(orden2);
        String centroides[] = new String[k];
        imprimir += "Relation:\n" + dataInstances.relationName();
        c.calcular_centroides(centroides, dataInstances);
        for (int i = 0; i < dataInstances.numInstances(); i++) {
            float matriz[][] = new float[dataInstances.numInstances()][k];
            float matriz_cluster[][] = new float[dataInstances.numInstances()][k + 1];
            c.llenar_matriz(matriz, dataInstances, centroides);
            c.clustering(matriz, matriz_cluster, dataInstances);
            c.calcular_nuevos_centroides(dataInstances, matriz_cluster, centroides);
            c.llenar_valores_cluster(dataInstances, orden1, matriz_cluster);
            if (Cluster.parar(orden1, orden2) == true) {
                imprimir += "\n\nk = " + k + "\n";
                imprimir += "\nNúmero de iteraciones :" + (i + 1) + "\n";
                imprimir += c.imprimir_centroides_iniciales();
                imprimir += c.imprimir_centroides_finales(dataInstances);
                imprimir += "\nTotal de instancias: " + dataInstances.numInstances() + "\n";
                imprimir += c.datos_por_cluster(dataInstances, matriz_cluster);
                try {
                    //	tree_graph();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return imprimir;
            } else {
                c.cambiar_orden(orden1, orden2);
            }
        }
        return imprimir;
    }

    public static void tree_graph() throws Exception {
        Instances data = new Instances(new BufferedReader(new FileReader("RGBPhotos.arff")));
        HierarchicalClusterer cls = new HierarchicalClusterer();
        cls.buildClusterer(data);
        data.setClassIndex(data.numAttributes() - 1);
        final javax.swing.JFrame jf = new javax.swing.JFrame("Árbol resultante");
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null, cls.graph(), new PlaceNode2());
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });

        jf.setVisible(true);
        tv.fitToScreen();
    }
}
