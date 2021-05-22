package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

/**
 * The <code>TurtleState</code> class represents one state of a turtle somewhere on the screen capable of moving on it.
 * The turtle can draw lines by moving, change the color of its trail, and change its position and direction.
 * The area that the turtle can move on is defined by a coordinate system on the graphic window.
 * Origin of the coordinate system is the bottom left angle of the window, and the axes are pointed towards the right (x-axis),
 * and towards up (y-axis). The top right position has coordinates (1, 1).
 * 
 * @author stipe
 *
 */
public class TurtleState {

	/**
	 * Turtle's position represented by a radius-vector.
	 */
	private Vector2D position;
	/**
	 * Turtle's direction represented by a unit vector.
	 */
	private Vector2D direction;
	/**
	 * The color of the turtle's trail.
	 */
	private Color color;
	/**
	 * Multiplier of the turtle's regular step.
	 */
	private double effectiveStep;
	
	/**
	 * Constructor which creates a new <code>TurtleState</code> object with the given parameters.
	 * 
	 * @param position {@link #position} of the created turtle
	 * @param direction {@link #direction} of the created turtle
	 * @param color {@link #color} of the created turtle
	 * @param effectiveStep {@link #effectiveStep} of the created turtle
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double effectiveStep) {
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.effectiveStep = effectiveStep;
	}

	/**
	 * Copies this turtle using its properties and returns the copy.
	 * If one of the original or the copy changes, the other one is left unchanged.
	 * 
	 * @return <code>TurtleState</code> with the copied properties of this state
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), direction.copy(), color, effectiveStep);
	}

	/**
	 * Getter of this <code>TurtleState</code>'s current position.
	 * 
	 * @return {@link #position}
	 */
	public Vector2D getPosition() {
		return position;
	}
	
	/**
	 * Getter of this <code>TurtleState</code>'s current direction.
	 * 
	 * @return {@link #direction}
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * Getter of this <code>TurtleState</code>'s current color.
	 * 
	 * @return {@link #color}
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Getter of this <code>TurtleState</code>'s current effective step.
	 * 
	 * @return {@link #effectiveStep}
	 */
	public double getEffectiveStep() {
		return effectiveStep;
	}

	/**
	 * Setter of this <code>TurtleState</code>'s current position.
	 * 
	 * @param position {@link #position} to be set to this <code>TurtleState</code>
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * Setter of this <code>TurtleState</code>'s current direction.
	 * 
	 * @param direction {@link #direction} to be set to this <code>TurtleState</code>
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	/**
	 * Setter of this <code>TurtleState</code>'s current color.
	 * 
	 * @param color {@link #color} to be set to this <code>TurtleState</code>
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Setter of this <code>TurtleState</code>'s current effective step.
	 * 
	 * @param effectiveStep {@link #effectiveStep} to be set to this <code>TurtleState</code>
	 */
	public void setEffectiveStep(double effectiveStep) {
		this.effectiveStep = effectiveStep;
	}
	
}
