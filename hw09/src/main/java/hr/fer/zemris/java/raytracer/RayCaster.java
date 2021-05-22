package hr.fer.zemris.java.raytracer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
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
 * simplification of a ray-tracer for rendering of 3D scenes. 
 * 
 * @author stipe
 *
 */
public class RayCaster {

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), 
				new Point3D(10, 0, 0), 
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 
				20, 20
			);
	}

	/**
	 * Creates and returns an implementation of {@link IRayTracerProducer}
	 * used for 3D scene rendering.
	 * 
	 * @return object implementing {@code IRayTracerProducer}
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			private static final double TOLERANCE = 1e-4;
			
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
						double horizontal, double vertical, int width, int height,
						long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				
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
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				short[] rgb = new short[3];
				int offset = 0;
				for(int y = 0; y < height; y++) {
					for(int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply((x * horizontal) / (width - 1)))
								.modifySub(yAxis.scalarMultiply((y * vertical) / (height - 1)));
						Ray ray = Ray.fromPoints(eye, screenPoint);
						
						tracer(scene, ray, rgb);
						
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						
						offset++;
					}
				}
				
				System.out.println("Calculations done!");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Report done!");
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
			
		};
	}
}
