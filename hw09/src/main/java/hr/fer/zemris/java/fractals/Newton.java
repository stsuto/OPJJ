package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.util.Util;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Class {@code Newton} computes and offers visualization of fractals derived
 * from Newton-Raphson iteration.
 * <p>
 * To start computing fractals, the user must input at least 2 legal complex
 * roots which will be used for the creation of fractals.
 * 
 * @author stipe
 *
 */
public class Newton {

	/**
	 * The main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Complex[] roots = startInteraction();
		ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
		FractalViewer.show(new MyProducer(polynomial));
		
	}

	/**
	 * Starts the interaction with the user, using his input to create complex
	 * numbers and add them to an array.
	 * 
	 * @return array of complex numbers
	 */
	private static Complex[] startInteraction() {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n"
				+ "Please enter at least two roots, one root per line. Enter 'done' when done.");

		Scanner sc = new Scanner(System.in);
		List<Complex> roots = new ArrayList<>();
		int rootNum = 1;

		while (true) {
			System.out.printf("Root %d> ", rootNum++);
			String root = sc.nextLine();

			if ("done".equals(root)) {
				if (roots.size() < 2) {
					System.out.println("Invalid input! At least two roots are needed!");
					System.exit(1);
				}
				System.out.println("Image of fractal will appear shortly. Thank you.");
				break;
			}

			try {
				roots.add(Complex.parse(root));
			} catch (NullPointerException | NumberFormatException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}

		sc.close();
		return roots.toArray(new Complex[] {});
	}

	/**
	 * Class {@code MyProducer} is an implementation of {@link IFractalProducer}
	 * used for the production of fractals. It uses the given complex root polynomial
	 * for the creation of the fractals.
	 * 
	 * @author stipe
	 *
	 */
	public static class MyProducer implements IFractalProducer {
		/**
		 * The root polynomial.
		 */
		private ComplexRootedPolynomial rootPol;
		/**
		 * Normal polynomial, equivalent of {@link #rootPol}.
		 */
		private ComplexPolynomial pol;
		/**
		 * Threadpool used for fractal computation.
		 */
		private ExecutorService pool;

		/**
		 * Constant representing the multiplier used for job creation.
		 */
		private static final int JOB_MULTIPLIER = 8;
		/**
		 * Thread factory which creates daemon thread used for fractal computation.
		 */
		private static final ThreadFactory DAEMON_THREAD_FACTORY = r -> {
			Thread worker = new Thread(r);
			worker.setDaemon(true);
			return worker;
		};
		
		/**
		 * Constructor which start the fractal computation with the given polynomial.
		 * 
		 * @param rootPol polynomial
		 * @throws NullPointerException if the given argument is null
		 */
		public MyProducer(ComplexRootedPolynomial rootPol) {
			this.rootPol = Util.requireNonNull(rootPol, "Root polynomial");
			this.pol = rootPol.toComplexPolynom();
			this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), DAEMON_THREAD_FACTORY);
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Starting calculation...");
			short[] data = new short[width * height];
			final int numOfJobs = JOB_MULTIPLIER * Runtime.getRuntime().availableProcessors();
			int heightOfJob = height / numOfJobs;

			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < numOfJobs; i++) {
				int yMin = i * heightOfJob;
				int yMax = (i + 1) * heightOfJob - 1;
				if (i == numOfJobs - 1) {
					yMax = height - 1;
				}
				CalculateJob job = new CalculateJob(rootPol, pol, reMin, reMax, imMin,
						imMax, width, height, yMin, yMax, data);
				results.add(pool.submit(job));
			}

			for (Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}

			System.out.println("Calculation done. Notifying observer, i.e. GUI!");
			observer.acceptResult(data, (short) (pol.order() + 1), requestNo);
		}
	}

	/**
	 * Class {@code CalculateJob} is an implementation of {@link Callable} and 
	 * represents a job performed my multiple threads for fractal computing.
	 * 
	 * @author stipe
	 *
	 */
	public static class CalculateJob implements Callable<Void> {
		/**
		 * The root polynomial.
		 */
		private ComplexRootedPolynomial rootPol;
		/**
		 * The normal polynomial.
		 */
		private ComplexPolynomial pol;
		/**
		 * Minimum real value.
		 */
		private double reMin;
		/**
		 * Maximum real value.
		 */
		private double reMax;
		/**
		 * Minimum imaginary value.
		 */
		private double imMin;
		/**
		 * Maximum imaginary value.
		 */
		private double imMax;
		/**
		 * Width of the screen.
		 */
		private int width;
		/**
		 * Height of the screen.
		 */
		private int height;
		/**
		 * First row of this job.
		 */
		private int yMin;
		/**
		 * Last row of this job.
		 */
		private int yMax;
		/**
		 * Data containing pixel information.
		 */
		private short[] data;

		/**
		 * Number of iterations performed to test convergence.
		 */
		private static final int MAX_ITER = 16 * 16 * 16;
		/**
		 * Root distance allowed.
		 */
		private static final double ROOT_DISTANCE = 0.002;
		/**
		 * Convergence threshold allowed.
		 */
		private static final double CONVERGENCE_THRESHOLD = 0.001;

		/**
		 * Constructor.
		 * 
		 * @param rootPol 	{@link #rootPol}
		 * @param pol 		{@link #pol}
		 * @param reMin 	{@link #reMin}
		 * @param reMax 	{@link #reMax}
		 * @param imMin 	{@link #imMin}
		 * @param imMax 	{@link #imMax}
		 * @param width 	{@link #width}
		 * @param height 	{@link #height}
		 * @param yMin 		{@link #yMin}
		 * @param yMax 		{@link #yMax}
		 * @param data 		{@link #data}
		 */
		public CalculateJob(ComplexRootedPolynomial rootPol, ComplexPolynomial pol, double reMin, double reMax,
					double imMin, double imMax, int width, int height, int yMin, int yMax, short[] data) {
			this.rootPol = rootPol;
			this.pol = pol;
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
		}

		@Override
		public Void call() {
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x <= width; x++) {
					Complex zn = mapToComplexPlain(x, y);

					int iter = 0;
					double module = 0;

					do {
						iter++;
						Complex numerator = pol.apply(zn);
						Complex denominator = pol.derive().apply(zn);
						Complex znOld = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = znOld.sub(zn).module();

					} while (module > CONVERGENCE_THRESHOLD && iter < MAX_ITER);

					int index = rootPol.indexOfClosestRootFor(zn, ROOT_DISTANCE);
					data[y * width + x] = (short) (index + 1);
				}
			}

			return null;
		}

		/**
		 * Maps the current coordinates to the complex plain.
		 * 
		 * @param x x-coordinate
		 * @param y y-coordinate
		 * @return Complex representation of the position
		 */
		private Complex mapToComplexPlain(int x, int y) {
			double re = (double) x / (width - 1) * (reMax - reMin) + reMin;
			double im = (double) (height - 1 - y) / (height - 1) * (imMax - imMin) + imMin;

			return new Complex(re, im);
		}
	}

}
