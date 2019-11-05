
public class Driver {

	public static void main(String[] args) {

		// For time keeping
		long start, finish, timeElapsed;
		// Use System.nanoTime();
		// Make sure to divide by 1,000,000 for nanoSeconds to milliSeconds
		
		int power = 10; // I changed this value manually for each size
		
		int[][] mat = new int[(int) Math.pow(2, power)][(int) Math.pow(2, power)]; 	
		int[][] mat2 = new int[(int) Math.pow(2, power)][(int) Math.pow(2, power)];	
		int[][] matResult = new int[(int) Math.pow(2, power)][(int) Math.pow(2, power)];
		
		
		System.out.println("The power is " + power);
		// Classical
		for(int i = 0; i < 10 ; i++) {
		fillMatrix(mat, 2);
		fillMatrix(mat2, 2);
		
		start = System.nanoTime();
		matResult = classicalMult(mat, mat2);
		finish = System.nanoTime();
		timeElapsed = (finish - start);
		System.out.println(i + " Classical: " + timeElapsed + " nanoseconds");
		}
		
		// Divide and Conquer
		for(int i = 0; i < 10 ; i++) {
			fillMatrix(mat, 2);
			fillMatrix(mat2, 2);
			
			start = System.nanoTime();
			matResult = divideAndConquer(mat, mat2);
			finish = System.nanoTime();
			timeElapsed = (finish - start);
			System.out.println(i + " Divide and Conquer: " + timeElapsed + " nanoseconds");
			}
		
		// Strassen's
		for(int i = 0; i < 10 ; i++) {
			fillMatrix(mat, 2);
			fillMatrix(mat2, 2);
			
			start = System.nanoTime();
			matResult = Strassen((int) Math.pow(2, power), mat, mat2);
			finish = System.nanoTime();
			timeElapsed = (finish - start);
			System.out.println(i + " Strassen: " + timeElapsed + " nanoseconds");
			}
	}

	static private void fillMatrix(int[][] matrix, int x) // Fills a matrix to capacity with x's
	{
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++)
				matrix[i][j] = x;
	}

	static private void printMatrix(int[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++)
				System.out.print(a[i][j] + " ");
			System.out.println();
		}
	}

	// Multiplies two matrices classically
	// Returns a new matrix
	// Assumes two square matrices of same size
	// O(n^3)
	private static int[][] classicalMult(int[][] a, int[][] b) {
		int[][] c = new int[a.length][a[0].length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a.length; j++)
				for (int k = 0; k < a.length; k++)
					c[i][j] += a[i][k] * b[k][j];
		return c;
	}

	// Divides the two factors into quarters, then recombines into product
	private static int[][] divideAndConquer(int[][] a, int[][] b) {
		int n = a.length;

		int[][] a11 = new int[n / 2][n / 2];
		int[][] a12 = new int[n / 2][n / 2];
		int[][] a21 = new int[n / 2][n / 2];
		int[][] a22 = new int[n / 2][n / 2];

		split(a, a11, 0, 0);
		split(a, a12, 0, n / 2);
		split(a, a21, n / 2, 0);
		split(a, a22, n / 2, n / 2);

		int[][] b11 = new int[n / 2][n / 2];
		int[][] b12 = new int[n / 2][n / 2];
		int[][] b21 = new int[n / 2][n / 2];
		int[][] b22 = new int[n / 2][n / 2];

		split(b, b11, 0, 0);
		split(b, b12, 0, n / 2);
		split(b, b21, n / 2, 0);
		split(b, b22, n / 2, n / 2);

		int[][] c11 = add(classicalMult(a11, b11), classicalMult(a12, b21));
		int[][] c12 = add(classicalMult(a12, b12), classicalMult(a12, b22));
		int[][] c21 = add(classicalMult(a21, b11), classicalMult(a22, b21));
		int[][] c22 = add(classicalMult(a21, b12), classicalMult(a22, b22));

		int[][] c = new int[n][n];

		join(c11, c, 0, 0);
		join(c12, c, 0, n / 2);
		join(c21, c, n / 2, 0);
		join(c22, c, n / 2, n / 2);

		return c;
	}

	public static int[][] Strassen(int n, int[][] a, int[][] b) {
		int[][] a11 = new int[n / 2][n / 2];
		int[][] a12 = new int[n / 2][n / 2];
		int[][] a21 = new int[n / 2][n / 2];
		int[][] a22 = new int[n / 2][n / 2];
		int[][] b11 = new int[n / 2][n / 2];
		int[][] b12 = new int[n / 2][n / 2];
		int[][] b21 = new int[n / 2][n / 2];
		int[][] b22 = new int[n / 2][n / 2];

		split(a, a11, 0, 0);
		split(a, a12, 0, n / 2);
		split(a, a21, n / 2, 0);
		split(a, a22, n / 2, n / 2);
		split(b, b11, 0, 0);
		split(b, b12, 0, n / 2);
		split(b, b21, n / 2, 0);
		split(b, b22, n / 2, n / 2);

		if (n == 2) {
			int[][] c11 = add(classicalMult(a11, b11), classicalMult(a12, b21));
			int[][] c12 = add(classicalMult(a12, b12), classicalMult(a12, b22));
			int[][] c21 = add(classicalMult(a21, b11), classicalMult(a22, b21));
			int[][] c22 = add(classicalMult(a21, b12), classicalMult(a22, b22));

			int[][] c = new int[n][n];

			join(c11, c, 0, 0);
			join(c12, c, 0, n / 2);
			join(c21, c, n / 2, 0);
			join(c22, c, n / 2, n / 2);

			return c;
		} else {
			int[][] p = Strassen(n / 2, add(a11, a22), add(b11, b22));
			int[][] q = Strassen(n / 2, add(a21, a22), b11);
			int[][] r = Strassen(n / 2, a11, sub(b12, b22));
			int[][] s = Strassen(n / 2, a22, sub(b21, b11));
			int[][] t = Strassen(n / 2, add(a11, a12), b22);
			int[][] u = Strassen(n / 2, sub(a21, a11), add(b11, b12));
			int[][] v = Strassen(n / 2, sub(a12, a22), add(b21, b22));

			int[][] c11 = sub(add(p, s), add(t, v));
			int[][] c12 = add(r, t);
			int[][] c21 = add(q, s);
			int[][] c22 = sub(add(p, r), add(q, u));

			int[][] c = new int[n][n];

			join(c11, c, 0, 0);
			join(c12, c, 0, n / 2);
			join(c21, c, n / 2, 0);
			join(c22, c, n / 2, n / 2);

			return c;
		}

	}

	public static void split(int[][] P, int[][] C, int iB, int jB) {
		for (int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
			for (int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
				C[i1][j1] = P[i2][j2];
	}

	public static void join(int[][] C, int[][] P, int iB, int jB) {
		for (int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
			for (int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
				P[i2][j2] = C[i1][j1];
	}

	public static int[][] sub(int[][] a, int[][] b) {
		int n = a.length;
		int[][] c = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				c[i][j] = a[i][j] - b[i][j];
		return c;
	}

	public static int[][] add(int[][] a, int[][] b) {
		int[][] c = new int[a.length][a.length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a.length; j++)
				c[i][j] = a[i][j] + b[i][j];
		return c;
	}

}