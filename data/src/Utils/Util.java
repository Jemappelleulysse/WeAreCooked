package Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Util {


    public static ArrayList<Pair> pathFinding(Pair posDepart, Pair posArrive,int[][] board, int[][] seen) {
        Queue<ArrayList<Pair>> queue = new LinkedList<>();
        ArrayList<Pair> initialPath = new ArrayList<>();
        initialPath.add(posDepart);
        queue.add(initialPath);

        while (!queue.isEmpty()) {
            ArrayList<Pair> currentPath = queue.poll();
            Pair current = currentPath.get(currentPath.size() - 1);

            // Vérification des limites et obstacles
            if (current.i < 0 || current.i >= board.length || current.j < 0 || current.j >= board[0].length) {
                continue;
            }
            if (!(Pair.getTableau(current,board) == -1 || Pair.getTableau(current,board) == 0) || seen[current.i][current.j] == 1) {
                continue;
            }

            // Marquer comme vu
            seen[current.i][current.j] = 1;

            // Si on est arrivé
            if (current.equals(posArrive)) {
                for (Pair p : currentPath) {
                    //System.out.print(p);
                }
                return currentPath;
            }

            // Ajouter les voisins à explorer
            for (Pair neighbor : getNeighbors(current)) {
                ArrayList<Pair> newPath = new ArrayList<>(currentPath);
                newPath.add(neighbor);
                queue.add(newPath);
            }
        }

        return null; // Aucun chemin trouvé
    }


    private static ArrayList<Pair> getNeighbors(Pair p) {
        ArrayList<Pair> neighbors = new ArrayList<>();
        neighbors.add(new Pair(p.i - 1, p.j)); // haut
        neighbors.add(new Pair(p.i + 1, p.j)); // bas
        neighbors.add(new Pair(p.i, p.j - 1)); // gauche
        neighbors.add(new Pair(p.i, p.j + 1)); // droite
        return neighbors;
    }
}
