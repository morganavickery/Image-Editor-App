package a8;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JPanel;

public class FramePuzzleWidget extends JPanel implements MouseListener, KeyListener {
  private PictureView          picture_view;

  private Picture              _blank;
  private int                  _blankRow;
  private int                  _blankColumn;

  private int                  _tileWidth;
  private int                  _tileHeight;
  private int                  _picWidth;
  private int                  _picHeight;

  private Iterator<SubPicture> _tileIterator;
  private Picture[][]          _tiles;

  public FramePuzzleWidget(Picture picture) {
    _picWidth = picture.getWidth();
    _picHeight = picture.getHeight();
    _tileWidth = picture.getWidth() / 5;
    _tileHeight = picture.getHeight() / 5;

    _tileIterator = new TileIterator(picture, _tileWidth, _tileHeight);
    _tiles = new Picture[5][5];

    for (int row = 0; row < 5; row++) {
      for (int column = 0; column < 5; column++) {
        if (_tileIterator.hasNext() == true) {
        }
        _tiles[column][row] = _tileIterator.next();

      }
    }

    _blank = new PictureImpl(_tileWidth, _tileHeight);
    for (int i = 0; i < _tileHeight; i++) {
      for (int j = 0; j < _tileWidth; j++) {
        _blank.setPixel(j, i, new ColorPixel(.5, .7, .9));
      }
    }

    _blankRow = 4;
    _blankColumn = 4;

    _tiles[4][4] = _blank;

    Picture stitched = this.stitchPic(_tiles, _tileWidth, _tileHeight);

    picture_view = new PictureView(stitched.createObservable());
    picture_view.addMouseListener(this);

    setLayout(new BorderLayout());
    add(picture_view, BorderLayout.CENTER);

    addKeyListener(this);
    setFocusable(true);
  }

  // stitch section
  public Picture stitchPic(Picture[][] tiles, int tileWidth, int tileHeight) {
    int picWidth = tileWidth * 5;
    int picHeight = tileWidth * 5;

    Picture stitched = new PictureImpl(picWidth, picHeight);
    Picture currentTile;
    for (int x = 0; x < picWidth; x++) {
      for (int y = 0; y < picHeight; y++) {
        if (y / tileHeight == 5) {
          break;
        }
        currentTile = _tiles[x / tileWidth][y / tileHeight];
        Pixel replacementPixel = currentTile.getPixel(x % tileWidth, y % tileHeight);
        stitched.setPixel(x, y, replacementPixel);
      }
    }
    return stitched;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    int row = y / _tileHeight;
    int column = x / _tileWidth;
    // same column diff row
    if (_blankRow != row && _blankColumn == column) {
      if (row > _blankRow) {
        int numSwitch = row - _blankRow;

        for (int i = 0; i < numSwitch; i++) {
          Picture holder = _tiles[_blankColumn][_blankRow + 1];
          _tiles[_blankColumn][_blankRow + 1] = _blank;
          _tiles[_blankColumn][_blankRow] = holder;

          _blankRow++;
        }

        _blankRow = row;
        // row < blank, higher
      } else {
        int numSwitch = _blankRow - row;
        for (int i = 0; i < numSwitch; i++) {
          Picture holder = _tiles[_blankColumn][_blankRow - 1];
          _tiles[_blankColumn][_blankRow - 1] = _blank;
          _tiles[_blankColumn][_blankRow] = holder;

          _blankRow--;
        }
        _blankRow = row;
      }
      // same row diff column
    } else if (_blankColumn != column && _blankRow == row) {
      if (column > _blankColumn) {
        int numSwitch = column - _blankColumn;
        for (int i = 0; i < numSwitch; i++) {
          Picture holder = _tiles[_blankColumn + 1][_blankRow];
          _tiles[_blankColumn + 1][_blankRow] = _blank;
          _tiles[_blankColumn][_blankRow] = holder;

          _blankColumn++;
        }
        _blankColumn = column;

        // column < blank, more left
      } else {
        int numSwitch = _blankColumn - column;
        for (int i = 0; i < numSwitch; i++) {
          Picture holder = _tiles[_blankColumn - 1][_blankRow];
          _tiles[_blankColumn - 1][_blankRow] = _blank;
          _tiles[_blankColumn][_blankRow] = holder;

          _blankColumn--;
        }
        _blankColumn = column;
      }
    }
    Picture pic = this.stitchPic(_tiles, _tileWidth, _tileHeight);

    picture_view.repaint();
    picture_view.revalidate();
    picture_view.setPicture(pic.createObservable());
    picture_view.setFocusable(false);

  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      // shift up
      if (_blankRow > 0) {
        Picture holder = _tiles[_blankColumn][_blankRow - 1];
        _tiles[_blankColumn][_blankRow - 1] = _blank;
        _tiles[_blankColumn][_blankRow] = holder;

        _blankRow--;
      }
      // shift down
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {

      if (_blankRow < 4) {
        Picture holder = _tiles[_blankColumn][_blankRow + 1];
        _tiles[_blankColumn][_blankRow + 1] = _blank;
        _tiles[_blankColumn][_blankRow] = holder;

        _blankRow++;
      }
      // shift left
    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

      if (_blankColumn > 0) {
        Picture holder = _tiles[_blankColumn - 1][_blankRow];
        _tiles[_blankColumn - 1][_blankRow] = _blank;
        _tiles[_blankColumn][_blankRow] = holder;

        _blankColumn--;
      }
      // shift right
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

      if (_blankColumn < 4) {
        Picture holder = _tiles[_blankColumn + 1][_blankRow];
        _tiles[_blankColumn + 1][_blankRow] = _blank;
        _tiles[_blankColumn][_blankRow] = holder;

        _blankColumn++;
      }
    }
    Picture pic = this.stitchPic(_tiles, _tileWidth, _tileHeight);

    picture_view.repaint();
    picture_view.revalidate();
    picture_view.setPicture(pic.createObservable());
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub

  }
}
