package hr.fer.zemris.java.raytracer.model;

/**
 * Class {@code Sphere} represents a graphical object located in scene.
 * 
 * @author stipe
 *
 */
public class Sphere extends GraphicalObject {

	/**
	 * Sphere's center.
	 */
	private Point3D center;
	/**
	 * Sphere's radius.
	 */
	private double radius;
	/**
	 * Red diffuse light component.
	 */
	private double kdr;
	/**
	 * Green diffuse light component.
	 */
	private double kdg;
	/**
	 * Blue diffuse light component.
	 */
	private double kdb;
	/**
	 * Red reflection light component.
	 */
	private double krr;
	/**
	 * Green reflection light component.
	 */
	private double krg;
	/**
	 * Blue reflection light component.
	 */
	private double krb;
	/**
	 * Reflection light constant.
	 */
	private double krn;

	/**
	 * Constructor.
	 * 
	 * @param center	{@link #center}
	 * @param radius	{@link #radius}
	 * @param kdr		{@link #kdr}
	 * @param kdg		{@link #kdg}
	 * @param kdb		{@link #kdb}
	 * @param krr		{@link #krr}
	 * @param krg		{@link #krg}
	 * @param krb		{@link #krb}
	 * @param krn		{@link #krn}
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D start = ray.start;
		Point3D dir = ray.direction;

		double lambda = getMinDistance(start, dir);
		if (lambda < 0) {
			return null;
		}
		Point3D intersection = start.add(dir.scalarMultiply(lambda));

		// Sto s ovim isOuter?
		return new SphereIntersection(intersection, lambda, true);

	}

	/**
	 * Calculates the minimum distance of all teh intersection distances.
	 * 
	 * @param start start of the ray
	 * @param dir direction of the ray
	 * @return distance from the start to the closest intersection ahead of the observer,
	 * if no such intersection, returns {@code -1}
	 */
	private double getMinDistance(Point3D start, Point3D dir) {
		Point3D distance = start.sub(center);
		double b = dir.scalarMultiply(2).scalarProduct(distance);
		double c = distance.scalarProduct(distance) - radius * radius;
		double discriminant = b * b - 4 * c;

		// Intersection is a complex number -> no real intersection!
		if (discriminant < 0) {
			return -1;
		}

		// We are looking for the nearest intersection, i.e. where lambda is smallest,
		// but only as long as it is still a positive number.
		double minLambda = (-b - Math.sqrt(discriminant)) / 2;
		double maxLambda = (-b + Math.sqrt(discriminant)) / 2;
		if (minLambda < 0 && maxLambda > 0) {
			minLambda = maxLambda; // The lesser one is negative, but the greater is OK.
		}

		return minLambda;
	}

	/**
	 * Class {@code SphereIntersection} represents an intersection of a ray with a sphere.
	 * 
	 * @author stipe
	 *
	 */
	private class SphereIntersection extends RayIntersection {
		/**
		 * Point of intersection.
		 */
		private Point3D point;

		/**
		 * Constructor.
		 * 
		 * @param point		point of intersection
		 * @param distance	distance between the ray start and the intersection
		 * @param outer		flag defining if the intersection is from the outside towards the object
		 */
		protected SphereIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
			this.point = point;
		}

		@Override
		public Point3D getNormal() {
			return point.sub(center).modifyNormalize();
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}
	}

}