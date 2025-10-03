package Utils;

public class Pair {
    public int i;
    public int j;
    public Pair(int i,int j) {
        this.i = i;
        this.j = j;
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
}
