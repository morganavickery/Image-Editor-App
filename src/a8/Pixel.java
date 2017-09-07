package a8;

public interface Pixel {

  public double getRed();

  public double getBlue();

  public double getGreen();

  public double getIntensity();

  public char getChar();

  public Pixel darken(double weight);

  public Pixel lighten(double factor);
}
