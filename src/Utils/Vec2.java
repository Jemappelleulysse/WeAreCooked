package Utils;

import java.util.ArrayList;

public class Vec2 {

    private int x, y;


    /// //////////// ///
    /// CONSTRUCTORS ///
    /// //////////// ///

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 vec) {
        if (vec == null) {
            throw new IllegalArgumentException("Copied vector can't be null");
        }

        this.x = vec.x;
        this.y = vec.y;
    }


    /// /////// ///
    /// GETTERS ///
    /// /////// ///

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isNotNull() {
        return x != 0 || y != 0;
    }


    /// /////// ///
    /// METHODS ///
    /// /////// ///
    public Vec2 sub(Vec2 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    public static ArrayList<Vec2> coordsToDirections(ArrayList<Vec2> path) {
        if (path.size() < 2) {
            return new ArrayList<>();
        }

        ArrayList<Vec2> result = new ArrayList<>();
        for (int i = 1 ; i < path.size() ; i++) {
            result.add(new Vec2( new Vec2(path.get(i-1)).sub(path.get(i))));
        }

        return result;
    }

    public static int getTableau(Vec2 vec, int[][] tableau) {
        return tableau[vec.x][vec.y];
    }

    @Override
    public String toString() {
        return "(" + x +"," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2 vec2 = (Vec2) o;
        return x == vec2.x && y == vec2.y;
    }
}
