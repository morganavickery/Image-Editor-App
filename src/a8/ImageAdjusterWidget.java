package a8;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageAdjusterWidget extends JPanel implements ChangeListener {

  PictureView picture_view;
  Picture     _pic;
  JLabel      _blurLabel;
  JLabel      _brightLabel;
  JLabel      _satLabel;
  JSlider     _blurSlider;
  JSlider     _satSlider;
  JSlider     _brightSlider;
  // List<ChangeListener> change_listeners;

  public ImageAdjusterWidget(Picture picture) {
    setLayout(new BorderLayout());

    picture_view = new PictureView(picture.createObservable());
    add(picture_view, BorderLayout.CENTER);

    _pic = picture;

    JPanel bottom_panel = new JPanel();
    bottom_panel.setLayout(new GridLayout(3, 2));
    add(bottom_panel, BorderLayout.SOUTH);

    // blur label
    _blurLabel = new JLabel();
    _blurLabel.setText("Blur");
    bottom_panel.add(_blurLabel);

    // blur slider
    _blurSlider = new JSlider();
    _blurSlider = new JSlider(0, 5);
    _blurSlider.setMajorTickSpacing(1);
    _blurSlider.setPaintLabels(true);
    _blurSlider.setPaintTicks(true);
    _blurSlider.setSnapToTicks(true);
    _blurSlider.setValue(0);
    // _blurSlider.addChangeListener(this);
    bottom_panel.add(_blurSlider);

    // brightness label
    _brightLabel = new JLabel();
    _brightLabel.setText("Brightness:");
    bottom_panel.add(_brightLabel);

    // brightness slider
    _brightSlider = new JSlider();
    _brightSlider = new JSlider(-100, 100);
    _brightSlider.setMajorTickSpacing(25);
    _brightSlider.setPaintLabels(true);
    _brightSlider.setPaintTicks(true);
    bottom_panel.add(_brightSlider);

    // saturation label
    _satLabel = new JLabel();
    _satLabel.setText("Saturation:");
    bottom_panel.add(_satLabel);

    // saturation slider
    _satSlider = new JSlider();
    _satSlider = new JSlider(-100, 100);
    _satSlider.setMajorTickSpacing(25);
    _satSlider.setPaintLabels(true);
    _satSlider.setPaintTicks(true);
    bottom_panel.add(_satSlider);

    _blurSlider.addChangeListener(this);
    _brightSlider.addChangeListener(this);
    _satSlider.addChangeListener(this);

  }

  public void stateChanged(ChangeEvent e) {
    int blurValue = _blurSlider.getValue();
    System.out.println(blurValue);
    double brightValue = _brightSlider.getValue();
    double saturateValue = _satSlider.getValue();

    Picture blurred = blur(_pic, blurValue);
    Picture brightened = brighten(blurred, brightValue);
    Picture saturated = saturate(brightened, saturateValue);

    picture_view.setPicture(saturated.createObservable());

    // picture_view.setPicture(blurred.createObservable());
  }

  public Picture saturate(Picture pic, double factor) {
    if (factor == 0) {
      return pic;
    }

    Picture newPic = new PictureImpl(pic.getWidth(), pic.getHeight());

    if (factor < 0) {
      for (int i = 0; i < pic.getWidth(); i++) {
        for (int j = 0; j < pic.getHeight(); j++) {
          Pixel current = pic.getPixel(i, j);
          double b = current.getIntensity();
          double oldRed = current.getRed();
          double oldBlue = current.getBlue();
          double oldGreen = current.getGreen();

          double newRed = oldRed * (1.0 + (factor / 100)) - (b * factor / 100);
          double newBlue = oldBlue * (1.0 + (factor / 100)) - (b * factor / 100);
          double newGreen = oldGreen * (1.0 + (factor / 100)) - (b * factor / 100);

          Pixel replacement = new ColorPixel(newRed, newGreen, newBlue);
          Coordinate c = new Coordinate(i, j);
          newPic.setPixel(c, replacement);
        }
      }
    }
    if (factor > 0) {
      for (int i = 0; i < pic.getWidth(); i++) {
        for (int j = 0; j < pic.getHeight(); j++) {
          Pixel current = pic.getPixel(i, j);
          double b = current.getIntensity();
          double oldRed = current.getRed();
          double oldBlue = current.getBlue();
          double oldGreen = current.getGreen();

          double a = Math.max((Math.max(oldRed, oldGreen)), oldBlue);

          double newRed = oldRed * ((a + ((1.0 - a) * (factor / 100))) / a);
          double newBlue = oldBlue * ((a + ((1.0 - a) * (factor / 100))) / a);
          double newGreen = oldGreen * ((a + ((1.0 - a) * (factor / 100))) / a);

          Pixel replacement = new ColorPixel(newRed, newGreen, newBlue);
          Coordinate c = new Coordinate(i, j);
          newPic.setPixel(c, replacement);
        }
      }
    }
    return newPic;
  }

  public Picture brighten(Picture pic, double brightValue) {
    if (brightValue == 0) {
      return pic;
    }
    Picture newPic = new PictureImpl(pic.getWidth(), pic.getHeight());
    double weight = brightValue / 100;
    // negative = darken
    if (brightValue < 0) {

      weight = weight * -1.0;

      for (int i = 0; i < pic.getWidth(); i++) {
        for (int j = 0; j < pic.getHeight(); j++) {

          Pixel current = pic.getPixel(i, j);
          Pixel replacement = null;

          replacement = current.darken(weight);

          Coordinate c = new Coordinate(i, j);
          newPic.setPixel(c, replacement);
        }
      }
      // positive = lighten
    } else if (brightValue > 0) {
      for (int i = 0; i < pic.getWidth(); i++) {
        for (int j = 0; j < pic.getHeight(); j++) {

          Pixel replacement = null;
          Pixel current = pic.getPixel(i, j);

          replacement = current.lighten(weight);

          Coordinate c = new Coordinate(i, j);
          newPic.setPixel(c, replacement);
        }
      }
    }
    return newPic;
  }

  public Picture blur(Picture pic, int blurValue) {
    if (blurValue == 0) {
      return pic;
    }
    Picture newPic = new PictureImpl(pic.getWidth(), pic.getHeight());
    double redTotal;
    double greenTotal;
    double blueTotal;
    double redAverage;
    double blueAverage;
    double greenAverage;
    int counter;

    for (int i = 0; i < pic.getWidth(); i++) {
      for (int j = 0; j < pic.getHeight(); j++) {
        counter = 0;
        redTotal = 0;
        greenTotal = 0;
        blueTotal = 0;
        for (int a = i - blurValue; a <= i + blurValue; a++) {
          for (int b = j - blurValue; b <= j + blurValue; b++) {

            try {
              redTotal = redTotal + pic.getPixel(a, b).getRed();
              greenTotal = greenTotal + pic.getPixel(a, b).getGreen();
              blueTotal = blueTotal + pic.getPixel(a, b).getBlue();
              counter++;
            } catch (RuntimeException e) {

            }
          }
          redAverage = redTotal / counter;
          blueAverage = blueTotal / counter;
          greenAverage = greenTotal / counter;

          Coordinate c = new Coordinate(i, j);
          newPic.setPixel(c, new ColorPixel(redAverage, greenAverage, blueAverage));
        }
      }
    }
    return newPic;
  }
}
