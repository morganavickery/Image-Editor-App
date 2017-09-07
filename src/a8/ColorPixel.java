package a8;

public class ColorPixel implements Pixel {

  private double              red;
  private double              green;
  private double              blue;

  private static final double RED_INTENSITY_FACTOR   = 0.299;
  private static final double GREEN_INTENSITY_FACTOR = 0.587;
  private static final double BLUE_INTENSITY_FACTOR  = 0.114;

  private static final char[] PIXEL_CHAR_MAP         = { '#', 'M', 'X', 'D', '<', '>',
      's', ':', '-', ' ', ' ' };

  public ColorPixel(double r, double g, double b) {
    if (r > 1.0 || r < 0.0) {
      throw new IllegalArgumentException("Red out of bounds");
    }
    if (g > 1.0 || g < 0.0) {
      throw new IllegalArgumentException("Green out of bounds");
    }
    if (b > 1.0 || b < 0.0) {
      throw new IllegalArgumentException("Blue out of bounds");
    }
    red = r;
    green = g;
    blue = b;
  }

  public Pixel darken(double factor) {
    if (factor < 0 || factor > 1.0) {
      throw new RuntimeException("factor is out of bounds");
    }
    Pixel black = new ColorPixel(0.0, 0.0, 0.0);
    Pixel pixel = this.blend(black, 1 - factor);
    return pixel;
  }

  public Pixel lighten(double factor) {
    if (factor < 0 || factor > 1.0) {
      throw new RuntimeException("factor is out of bounds");
    }
    Pixel white = new ColorPixel(1.0, 1.0, 1.0);
    Pixel pixel = this.blend(white, 1 - factor);
    return pixel;
  }

  public Pixel blend(Pixel p, double weight) {
    if (weight < 0 || weight > 1.0) {
      throw new RuntimeException("weight is out of bounds");
    }
    if (p == null) {
      throw new RuntimeException("pixel p is null");
    }
    double pWeight = 1.0 - weight;
    double currentWeight = weight;
    double pRed = p.getRed();
    double blendRed = ((pRed * pWeight) + (this.getRed() * currentWeight));
    double pGreen = p.getGreen();
    double blendGreen = ((pGreen * pWeight) + (this.getGreen() * currentWeight));
    double pBlue = p.getBlue();
    double blendBlue = ((pBlue * pWeight) + (this.getBlue() * currentWeight));
    // double pIntensity = p.getIntensity();
    // double currentIntensity = this.getIntensity(this.getRed(),
    // this.getGreen(), this.getBlue());
    // double blendIntensity = (pWeight * pIntensity) + (currentWeight *
    // currentIntensity);
    Pixel pixel = new ColorPixel(blendRed, blendGreen, blendBlue);
    return pixel;
  }

  @Override
  public double getRed() {
    return red;
  }

  @Override
  public double getBlue() {
    return blue;
  }

  @Override
  public double getGreen() {
    return green;
  }

  @Override
  public double getIntensity() {
    return RED_INTENSITY_FACTOR * getRed() + GREEN_INTENSITY_FACTOR * getGreen()
        + BLUE_INTENSITY_FACTOR * getBlue();
  }

  @Override
  public char getChar() {
    int char_idx = (int) (getIntensity() * 10.0);
    return PIXEL_CHAR_MAP[char_idx];
  }
}
