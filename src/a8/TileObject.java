package a8;

public class TileObject {

  double _width;
  double _height;
  int    _column;
  int    _row;

  public TileObject(Picture p, int row, int column) {
    _width = p.getWidth();
    _height = p.getHeight();
    _column = column;
    _row = row;
  }

  public void setColumn(int c) {
    _column = c;
  }

  public void setRow(int r) {
    _row = r;
  }

  public double getWidth() {
    return _width;
  }

  public double getHeight() {
    return _height;
  }

  public int getColumn() {
    return _column;
  }

  public int getRow() {
    return _row;
  }

}
