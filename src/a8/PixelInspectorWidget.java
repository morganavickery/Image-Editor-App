package a8;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorWidget extends JPanel implements MouseListener {

  int         _x;
  int         _y;
  double      _red;
  double      _blue;
  double      _green;
  double      _bright;
  Picture     _pic;
  Pixel       _pixel;
  JLabel      red;
  JLabel      blue;
  JLabel      green;
  JLabel      bright;
  JLabel      x;
  JLabel      y;
  PictureView picture_view;

  public PixelInspectorWidget(Picture picture, String title) {
    setLayout(new BorderLayout());

    picture_view = new PictureView(picture.createObservable());
    picture_view.addMouseListener(this);
    add(picture_view, BorderLayout.CENTER);

    _pic = picture;

    JPanel left_panel = new JPanel();
    left_panel.setLayout(new GridLayout(6, 1));
    add(left_panel, BorderLayout.WEST);

    x = new JLabel("X: ");
    y = new JLabel("Y: ");
    red = new JLabel("Red: ");
    blue = new JLabel("Blue: ");
    green = new JLabel("Green: ");
    bright = new JLabel("Brightness: ");

    left_panel.add(x);
    left_panel.add(y);
    left_panel.add(red);
    left_panel.add(blue);
    left_panel.add(green);
    left_panel.add(bright);
  }

  public Picture getPicture(Picture p) {
    return p;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    System.out.println("You clicked on the frame at: " + e.getX() + "," + e.getY());

    _x = e.getX();
    _y = e.getY();
    _pixel = _pic.getPixel(_x, _y);

    _red = (_pixel.getRed());
    _blue = (_pixel.getBlue());
    _green = (_pixel.getGreen());
    _bright = (_pixel.getIntensity());

    x.setText("X: " + _x);
    y.setText("Y: " + _y);
    red.setText("Red: " + _red);
    blue.setText("Blue: " + _blue);
    green.setText("Green: " + _green);
    bright.setText("Brightness: " + _bright);
  }

  public double shorten(double dVal) {
    double rounded = Math.round((dVal * 100) / 100);
    return rounded;
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

}
