package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Class <code>ForLoopNode</code> is an extension of <code>Node</code>. 
 * It is a representation of a single for-loop construct.
 * 
 * @author stipe
 */
public class ForLoopNode extends Node {

	/**
	 * the variable in the loop
	 */
	private ElementVariable variable;
	/**
	 * variable's initial value
	 */
	private Element startExpression;
	/**
	 * end-condition of the loop
	 */
	private Element endExpression;
	/**
	 * value of variable's single step
	 */
	private Element stepExpression;
	
	/**
	 * Constructor that initializes all of this node's variables to the values of given parameters.
	 * @param variable variable name
	 * @param startExpression variable's initial value
	 * @param endExpression value of variable when for loop should end
	 * @param stepExpression value of variable's single step
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		super();
		this.variable = Objects.requireNonNull(variable);
		this.startExpression = Objects.requireNonNull(startExpression);
		this.endExpression = Objects.requireNonNull(endExpression);
		this.stepExpression = stepExpression;
	}

	/**
	 * Returns the variable as <code>ElementVariable</code> 
	 * @return <code>ElementVariable</code> of variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Returns the startExpression as <code>Element</code> 
	 * @return <code>Element</code> of startExpression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Returns the endExpression as <code>Element</code> 
	 * @return <code>Element</code> of endExpression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Returns the stepExpression as <code>Element</code> 
	 * @return <code>Element</code> of stepExpression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
	
	
}
