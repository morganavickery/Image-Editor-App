package a8;

public class GrayPixel implements Pixel {

  private double              intensity;

  private static final char[] PIXEL_CHAR_MAP = { '#', 'M', 'X', 'D', '<', '>', 's', ':',
      '-', ' ' };

  public GrayPixel(double intensity) {
    if (intensity < 0.0 || intensity > 1.0) {
      throw new IllegalArgumentException("Intensity of gray pixel is out of bounds.");
    }
    this.intensity = intensity;
  }

  @Override
  public double getRed() {
    return getIntensity();
  }

  @Override
  public double getBlue() {
    return getIntensity();
  }

  @Override
  public double getGreen() {
    return getIntensity();
  }

  @Override
  public double getIntensity() {
    return intensity;
  }

  @Override
  public char getChar() {
    return PIXEL_CHAR_MAP[(int) (getIntensity() * 10.0)];
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
}
