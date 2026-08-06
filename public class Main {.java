package simha;
import java.util.Scanner;
public class grade {

	public static void main(String[] args) {
		
		        Scanner scanner = new Scanner(System.in);

		        System.out.print("Enter the marks: ");
		        int n = scanner.nextInt();

		        if (n < 0 || n > 100) {
		            System.out.println("Invalid");
		        } else if (n >= 90) {
		            System.out.println("A+");
		        } else if (n >= 80) {
		            System.out.println("A");
		        } else if (n >= 70) {
		            System.out.println("B+");
		        } else if (n >= 60) {
		            System.out.println("B");
		        } else if (n >= 50) {
		            System.out.println("C+");
		        } else if (n >= 35) {
		            System.out.println("C");
		        } else {
		            System.out.println("Fail");
		        }

		        scanner.close();
		    }
		

	}


