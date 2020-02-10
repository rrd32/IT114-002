import java.math.BigDecimal;

public class MyClass {

	public static void main(String[] args) {
		System.out.println("Hello");
		int myInt = 5;
		long myLong = 1l;
		float myFloat = 1.0f;
		double myDouble = 2.0d;
		
		System.out.println("My int is " + myInt);
		System.out.println("My float is " + myFloat);
		System.out.println("My double is " + myDouble);
		System.out.println("My long is " + myLong);
		
		BigDecimal a = new java.math.BigDecimal(0);
		BigDecimal b = new BigDecimal(1);
		
		for(int i = 0; i <10; i++) {
			a = a.add(new java.math.BigDecimal(0.1));
		}
		System.out.println("A equals B: " + (a== b));
		System.out.println("A: " + a);
		System.out.println("B: " + b);
		String s = new String("Hello");
		String s2 = "";
		int pointToAdd = 1000;
		int score = 0;
		for(int i=0; i < 500000; i++) {
			if(score < Integer.MAX_VALUE - pointToAdd) {
				score += pointToAdd;
				
			}
		}
		}

	}


