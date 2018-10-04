import java.util.Objects;

public final class GraphPoint {
    private final Double row;
    private final Double col;
    private final Double val;

    public GraphPoint(Double row, Double col, Double val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }

    public Double getRow() {
        return row;
    }

    public Double getCol() {
        return col;
    }
    
    public Double getVal() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphPoint that = (GraphPoint) o;
        return row == that.row &&
                col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return String.format("row %d, column %d, value %d", row, col, val);
    }

    public GraphPoint plus(Double valDiff) {
        return new GraphPoint(row, col, val + valDiff);
    }
}
