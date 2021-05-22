package hr.fer.zemris.java.raytracer;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Class {@code RayCaster} is an EASY AND FUN representation of a 
 * simplification of a ray-tracer for rendering of 3D scenes,
 * with animations. <p>
 * Uses forkpool parallelization the speed up computing.
 * 
 * @author stipe
 *
 */
public class RayCasterParallel2 {

	/**
	 * The main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		RayTracerViewer.show(getIRayTracerProducer(), 
				getIRayTracerAnimator(), 
				30, 30
			);
	}

	/**
	 * Creates and returns an object which implements {@code IRayTracerAnimator},
	 * describing animation support for the raytracer.
	 * 
	 * @return object implementing {@code IRayTracerAnimator}
	 */
	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {
			long time;

			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}

			@Override
			public Point3D getViewUp() { // fixed in time
				return new Point3D(0, 0, 10);
			}

			@Override
			public Point3D getView() { // fixed in time
				return new Point3D(-2, 0, -0.5);
			}

			@Override
			public long getTargetTimeFrameDuration() {
				return 150; // redraw scene each 150 milliseconds
			}

			@Override
			public Point3D getEye() { // changes in time
				double t = (double) time / 10000 * 2 * Math.PI;
				double t2 = (double) time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}
		};
	}

	/**
	 * Creates and returns an implementation of {@link IRayTracerProducer}
	 * used for 3D scene rendering.
	 * 
	 * @return object implementing {@code IRayTracerProducer}
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, 
					double horizontal, double vertical, int width, 
					int height, long requestNo, 
					IRayTracerResultObserver observer, 
					AtomicBoolean cancel) {

				System.out.println("Starting calculations...");

				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D vectorVUV = viewUp.normalize();

				// zAxis is normalized vectorOG
				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = vectorVUV.sub(zAxis.scalarMultiply(
						zAxis.scalarProduct(vectorVUV))).modifyNormalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).modifyNormalize();

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.modifyAdd(yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene2();

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new RayCasterJob(screenCorner, xAxis, yAxis, eye, scene, red, 
						green, blue, horizontal, vertical, width, height, 0, height - 1, cancel));
				pool.shutdown();

				System.out.println("Calculations done!");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Report done!");
			}

		};
	}

	/**
	 * Class {@code RayCasterJob} represents the job a thread performs.
	 * The job is split into smaller jobs threads will share, each one performing
	 * multiple. The job consists of finding objects within the scene and then
	 * computing which color should each point have.
	 * 
	 * @author stipe
	 *
	 */
	public static class RayCasterJob extends RecursiveAction {
		private static final long serialVersionUID = 1L;

		/*
		 * Upper left corner of the screen.
		 */
		private Point3D screenCorner;
		/**
		 * Normalized vector representing the x-axis.
		 */
		private Point3D xAxis;
		/**
		 * Normalized vector representing the y-axis.
		 */
		private Point3D yAxis;
		/**
		 * Position of the observer.
		 */
		private Point3D eye;
		/**
		 * The scene to be modeled.
		 */
		private Scene scene;
		/**
		 * Red component of the light.
		 */
		private short[] red;
		/**
		 * Green component of the light.
		 */
		private short[] green;
		/**
		 * Blue component of the light.
		 */
		private short[] blue;
		/**
		 * Horizontal width of observed space.
		 */
		private double horizontal;
		/**
		 * Vertical height of observed space.
		 */
		private double vertical; 
		/**
		 * Width of the screen.
		 */
		private int width;
		/**
		 * Height of the screen.
		 */
		private int height;
		/*
		 * First row of this job.
		 */
		private int yMin;
		/**
		 * Last row of this job.
		 */
		private int yMax;

		/**
		 * Tolerance for distance differences used because of double 
		 * value imprecision.
		 */
		private static final double TOLERANCE = 1e-4;
		/**
		 * Threshold of job dividing.
		 */
		private static final int THRESHOLD = 16 * 16 * 16;
		/**
		 * Thread-safe object used for canceling jobs.
		 */
		private AtomicBoolean cancel;

		/**
		 * Constructor.
		 * 
		 * @param screenCorner  {@link #screenCorner}
		 * @param xAxis			{@link #xAxis}
		 * @param yAxis			{@link #yAxis}
		 * @param eye			{@link #eye}
		 * @param scene			{@link #scene}
		 * @param red			{@link #red}
		 * @param green			{@link #green}
		 * @param blue			{@link #blue}
		 * @param horizontal	{@link #horizontal}
		 * @param vertical		{@link #vertical}
		 * @param width			{@link #width}
		 * @param height		{@link #height}
		 * @param yMin			{@link #yMin}
		 * @param yMax			{@link #yMax}
		 * @param cancel		{@link #cancel}
		 */
		public RayCasterJob(Point3D screenCorner, Point3D xAxis, Point3D yAxis, Point3D eye, 
				Scene scene, short[] red, short[] green, short[] blue, double horizontal, 
				double vertical, int width, int height, int yMin, int yMax, AtomicBoolean cancel) {

			this.screenCorner = screenCorner;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.eye = eye;
			this.scene = scene;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.cancel = cancel;
		}
		
		@Override
		public void compute() {
			if (yMax - yMin + 1 <= THRESHOLD) {
				computeDirect();
				return;
			}
			invokeAll(
					new RayCasterJob(screenCorner, xAxis, yAxis, eye, scene, red, green, blue, 
							horizontal, vertical, width, height, yMin, yMin + (yMax - yMin) / 2, cancel),
					new RayCasterJob(screenCorner, xAxis, yAxis, eye, scene, red, green, blue, 
							horizontal, vertical, width, height, yMin + (yMax - yMin) / 2 + 1, yMax, cancel));
		}

		/**
		 * Performs the job.
		 */
		public void computeDirect() {
			short[] rgb = new short[3];
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply((x * horizontal) / (width - 1)))
							.modifySub(yAxis.scalarMultiply((y * vertical) / (height - 1)));
					Ray ray = Ray.fromPoints(eye, screenPoint);

					tracer(scene, ray, rgb);

					red[y * width + x] = rgb[0] > 255 ? 255 : rgb[0];
					green[y * width + x] = rgb[1] > 255 ? 255 : rgb[1];
					blue[y * width + x] = rgb[2] > 255 ? 255 : rgb[2];
				}
			}
		}

		/**
		 * Searches the scene for obstructing objects and colors the scene
		 * depending on what was found.
		 * 
		 * @param scene scene
		 * @param ray ray
		 * @param rgb array containing red, green, and blue colors for each pixel
		 */
		private void tracer(Scene scene, Ray ray, short[] rgb) {
			RayIntersection inter = getClosestIntersection(scene, ray);

			if (inter == null) {
				Arrays.fill(rgb, (short) 0);
				return;
			}

			Arrays.fill(rgb, (short) 15);
			Point3D interPoint = inter.getPoint();

			for (LightSource ls : scene.getLights()) {
				Point3D lsPoint = ls.getPoint();
				Ray lsRay = Ray.fromPoints(lsPoint, interPoint);
				RayIntersection obstruction = getClosestIntersection(scene, lsRay);

				// If null or if obstructionDistance < intersectionDistance.
				if (obstruction != null && lsPoint.sub(obstruction.getPoint()).norm() 
							< lsPoint.sub(interPoint).norm() - TOLERANCE) {
					continue;
				}

				changeColors(ls, inter, ray, rgb);
			}

		}
		
		/**
		 * Changes the color of the pixel.
		 * 
		 * @param ls light source
		 * @param inter intersection
		 * @param ray ray
		 * @param rgb array containing red, green, and blue colors for each pixel
		 */
		private void changeColors(LightSource ls, RayIntersection inter, Ray ray, short[] rgb) {
			Point3D lsPoint = ls.getPoint();
			Point3D interPoint = inter.getPoint();
			Point3D vectorN = inter.getNormal();
			Point3D vectorL = lsPoint.sub(interPoint);
			double ln = vectorL.normalize().scalarProduct(vectorN);
			Point3D vectorLN = vectorN.scalarMultiply(vectorL.scalarProduct(vectorN));
			Point3D vectorR = vectorLN.add(vectorLN.sub(vectorL));
			Point3D vectorV = ray.start.sub(interPoint);
			double rv = vectorR.normalize().scalarProduct(vectorV.normalize());

			rgb[0] += getColor(ls.getR(), inter.getKdr(), inter.getKrr(), inter.getKrn(), ln, rv);
			rgb[1] += getColor(ls.getG(), inter.getKdg(), inter.getKrg(), inter.getKrn(), ln, rv);
			rgb[2] += getColor(ls.getB(), inter.getKdb(), inter.getKrb(), inter.getKrn(), ln, rv);
		}

		/**
		 * Gets the color values to be added.
		 * 
		 * @param lsInt light source intensity
		 * @param dif diffuse component of light
		 * @param refl reflective component of light
		 * @param krn coefficient of reflection
		 * @param ln scalar product of the ln vector
		 * @param rv scalar product of the rv vector
		 * @return color values for this point
		 */
		private double getColor(int lsInt, double dif, double refl, double krn, double ln, double rv) {
			return lsInt * (dif * (ln > 0 ? ln : 0) + refl * (rv > 0 ? Math.pow(rv, krn) : 0));
		}

		/**
		 * Finds the closest intersection of this ray and an object.
		 * 
		 * @param scene scene to be searched
		 * @param ray ray passing through the scene
		 * @return intersection of ray and object
		 */
		private RayIntersection getClosestIntersection(Scene scene, Ray ray) {
			RayIntersection closestInter = null;
			double minDistance = -1;

			for (GraphicalObject obj : scene.getObjects()) {
				RayIntersection newInter = obj.findClosestRayIntersection(ray);

				if (newInter != null && (minDistance == -1 || newInter.getDistance() < minDistance)) {
					closestInter = newInter;
					minDistance = newInter.getDistance();
				}
			}

			return closestInter;
		}
	}

}