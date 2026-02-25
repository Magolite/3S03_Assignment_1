
// Initial Development
//public class Calculator {
//
//    public double divide(double dividend, double divisor) {
//    	double quotient = dividend / divisor;
//    	return quotient;
//    }
//
//}

//Second improvement
public class Calculator {

    public double divide(double dividend, double divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        double quotient = dividend / divisor;
        return quotient;
    }

}