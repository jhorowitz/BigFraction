import com.sun.istack.internal.NotNull;

import java.math.BigInteger;


final class BigFraction extends Number implements Comparable<BigFraction> {

    private BigInteger numerator;
    private BigInteger denominator;
    public final static BigFraction ZERO = new BigFraction(BigInteger.ZERO);

    public BigFraction(String numerator, String denominator, boolean reduce) {
        this(new BigInteger(numerator), new BigInteger(denominator));
        if (reduce)
            this.reduce();
    }

    public BigFraction(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public BigFraction(String numerator, String denominator) {
        this(new BigInteger(numerator), new BigInteger(denominator));
    }

    public BigFraction(BigInteger numerator, BigInteger denominator) {
        if (denominator.signum() == 0)
            throw new IllegalArgumentException("Denominator can't be zero");
        if (numerator.signum() == 0) {
            denominator = BigInteger.ONE;
        }

        if (denominator.signum() == -1) {
            numerator = numerator.multiply(BigInteger.valueOf(-1));
            denominator = denominator.multiply(BigInteger.valueOf(-1));
        }

        this.numerator = numerator;
        this.denominator = denominator;

    }

    public void reduce() {
        if (!this.numerator.equals(BigInteger.ZERO)) {
            BigInteger GCF = (GCF(this.numerator, this.denominator)).abs();
            this.numerator = this.numerator.divide(GCF);
            this.denominator = this.denominator.divide(GCF);
        }
    }

    public BigFraction(BigInteger numerator) {
        this.numerator = numerator;
        this.denominator = BigInteger.ONE;
    }

    public static BigFraction valueOf(long numerator, long denominator) {
        return new BigFraction(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static BigFraction valueOf(long numerator) {
        return new BigFraction(BigInteger.valueOf(numerator));
    }

    public BigFraction multiply(BigFraction frac) {
        BigInteger newNumerator = this.getNumerator().multiply(frac.getNumerator());
        BigInteger newDenominator = this.getDenominator().multiply(frac.getDenominator());
        return new BigFraction(newNumerator, newDenominator);
    }

    public BigFraction divide(BigFraction frac) {
        return this.multiply(new BigFraction(frac.getDenominator(), frac.getNumerator()));
    }

    public BigFraction add(BigFraction frac) {
        BigInteger LCM = LCM(frac);
        BigInteger fracNum = (LCM.divide(frac.getDenominator())).multiply(frac.getNumerator());
        BigInteger thisNum = (LCM.divide(this.getDenominator())).multiply(this.getNumerator());
        return new BigFraction(thisNum.add(fracNum), LCM);
    }

    public BigFraction subtract(BigFraction frac) {
        BigInteger LCM = LCM(frac);
        BigInteger fracNum = (LCM.divide(frac.getDenominator())).multiply(frac.getNumerator());
        BigInteger thisNum = (LCM.divide(this.getDenominator())).multiply(this.getNumerator());
        return new BigFraction(thisNum.subtract(fracNum), LCM);
    }

    public String toString() {
        return this.getNumerator() + "/" + this.getDenominator();
    }

    public BigInteger getNumerator() {
        return this.numerator;
    }

    public BigInteger getDenominator() {
        return this.denominator;
    }

    @Override
    public int intValue() {
        return (int) this.doubleValue();
    }

    @Override
    public long longValue() {
        return (long) this.doubleValue();
    }

    @Override
    public float floatValue() {
        return (float) this.doubleValue();
    }

    @Override
    public double doubleValue() {
        return (this.numerator).doubleValue() / (this.denominator).doubleValue();
    }

    public boolean equals(BigFraction frac) {
        return this.compareTo(frac) == 0;
    }

    @Override
    public int compareTo(BigFraction frac) {
        BigInteger t = this.getNumerator().multiply(frac.getDenominator());
        BigInteger f = frac.getNumerator().multiply(this.getDenominator());

        int result = 0;

        if (t.max(f).equals(t))
            return 1;
        if (f.max(t).equals(f))
            return -1;
        return result;
    }

    public BigInteger LCM(BigFraction frac) {
        return (frac.getDenominator().multiply(this.getDenominator()).abs()).divide(GCF(frac.getDenominator(), this.getDenominator()));
    }

    public boolean isReduced() {
        BigInteger tempDenominator = numerator.min(denominator);
        BigInteger tempNumerator = numerator.max(denominator);

        BigInteger remainder = tempDenominator.mod(tempNumerator);
        while (!remainder.equals(BigInteger.ZERO)) {
            remainder = tempDenominator.mod(tempNumerator);
            tempDenominator = tempNumerator;
            tempNumerator = remainder;
        }
        return tempDenominator.equals(BigInteger.ONE);
    }

    private BigInteger GCF(BigInteger numerator, BigInteger denominator) {

        BigInteger tempDenominator = numerator.min(denominator);
        BigInteger tempNumerator = numerator.max(denominator);
        numerator = tempNumerator;
        denominator = tempDenominator;

        BigInteger remainder = denominator.mod(numerator);
        while (!remainder.equals(BigInteger.ZERO)) {
            remainder = denominator.mod(numerator);
            denominator = numerator;
            numerator = remainder;
        }

        return denominator;
    }
}