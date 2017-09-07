package a8;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TileIterator implements Iterator<SubPicture> {

  private int     _tileWidth;
  private int     _tileHeight;
  private Picture _pic;
  private int     _x;
  private int     _y;

  public TileIterator(Picture pic, int tileWidth, int tileHeight) {

    if (pic == null) {
      throw new IllegalArgumentException("Stahp");
    }

    if (tileWidth < 0 || tileWidth > pic.getWidth()) {
      throw new IllegalArgumentException("Stahp");
    }

    if (tileHeight < 0 || tileHeight > pic.getHeight()) {
      throw new IllegalArgumentException("Stahp");
    }

    if (tileWidth == 0 || tileHeight == 0) {
      throw new IllegalArgumentException("asdf");
    }

    _tileWidth = tileWidth;
    _tileHeight = tileHeight;
    _pic = pic;
    _x = 0;
    _y = 0;
  }

  @Override
  public boolean hasNext() {

    if (_x + _tileWidth - 1 >= _pic.getWidth()
        || _y + _tileHeight - 1 >= _pic.getHeight()) {
      return false;
    }

    return true;
  }

  @Override
  public SubPicture next() {

    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    SubPicture sub = _pic.extract(_x, _y, _tileWidth, _tileHeight);
    _x = _x + _tileWidth;

    if (_x + _tileWidth > _pic.getWidth()) {

      _x = 0;
      _y = _y + _tileHeight;
    }

    return sub;
  }

}
