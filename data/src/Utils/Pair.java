package Utils;

import java.util.ArrayList;

public class Pair {
    public int i;
    public int j;
    public Pair(int i,int j) {
        this.i = i;
        this.j = j;
    }
    public Pair(Pair ij) {
        this.i = ij.i;
        this.j = ij.j;
    }

    public Pair sub(Pair kl) {
        this.i -= kl.i;
        this.j -= kl.j;
        return this;
    }

    public static ArrayList<Pair> coordsToDirections(ArrayList<Pair> path) {
        if (path.size() < 2)  return new ArrayList<>();
        ArrayList<Pair> result = new ArrayList<>();
        for (int i = 1 ; i < path.size() ; i++) {
            result.add(new Pair( new Pair(path.get(i-1)).sub(path.get(i))));
        }
        return result;
    }

    @Override
    public String toString() {
        return "(" + i +"," + j + ")";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return i == pair.i && j == pair.j;
    }

    public static int getTableau(Pair ij,int[][] tableau) {
        return tableau[ij.i][ij.j];
    }
}
