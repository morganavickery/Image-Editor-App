package a8;

public interface Picture {

  int getWidth();

  int getHeight();

  Pixel getPixel(int x, int y);

  Pixel getPixel(Coordinate c);

  void setPixel(int x, int y, Pixel p);

  void setPixel(Coordinate c, Pixel p);

  SubPicture extract(double _x, double _y, double _tileWidth, double _tileHeight);

  SubPicture extract(Coordinate a, Coordinate b);

  SubPicture extract(Region r);

  ObservablePicture createObservable();

  SubPicture extract(int xoff, int yoff, int width, int height);

  SubPicture extract(double _x, double _y, int _tileWidth, int _tileHeight);

}
