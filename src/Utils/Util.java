package Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Util {

    public static ArrayList<Vec2> pathFinding(Vec2 posDepart, Vec2 posArrive, int[][] board, int[][] seen) {
        Queue<ArrayList<Vec2>> queue = new LinkedList<>();
        ArrayList<Vec2> initialPath = new ArrayList<>();
        initialPath.add(posDepart);
        queue.add(initialPath);

        while (!queue.isEmpty()) {
            ArrayList<Vec2> currentPath = queue.poll();
            Vec2 current = currentPath.get(currentPath.size() - 1);

            // Vérification des limites et obstacles
            if (current.getX() < 0 || current.getX() >= board.length || current.getY() < 0 || current.getY() >= board[0].length) {
                continue;
            }
            if (!(Vec2.getTableau(current,board) == -1 || Vec2.getTableau(current,board) == 0) || seen[current.getX()][current.getY()] == 1) {
                continue;
            }

            // Marquer comme vu
            seen[current.getX()][current.getY()] = 1;

            // Si on est arrivé
            if (current.equals(posArrive)) {
                return currentPath;
            }

            // Ajouter les voisins à explorer
            for (Vec2 neighbor : getNeighbors(current)) {
                ArrayList<Vec2> newPath = new ArrayList<>(currentPath);
                newPath.add(neighbor);
                queue.add(newPath);
            }
        }

        return null; // Aucun chemin trouvé
    }


    private static ArrayList<Vec2> getNeighbors(Vec2 p) {
        ArrayList<Vec2> neighbors = new ArrayList<>();
        neighbors.add(new Vec2(p.getX() - 1, p.getY())); // haut
        neighbors.add(new Vec2(p.getX() + 1, p.getY())); // bas
        neighbors.add(new Vec2(p.getX(), p.getY() - 1)); // gauche
        neighbors.add(new Vec2(p.getX(), p.getY() + 1)); // droite
        return neighbors;
    }
}
