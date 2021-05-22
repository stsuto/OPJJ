package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Engine used for using smart scripts.
 * 
 * @author stipe
 *
 */
public class SmartScriptEngine {
	
	/**
	 * Document node with all the text.
	 */
	private DocumentNode documentNode;
	/**
	 * Context of the script.
	 */
	private RequestContext requestContext;
	/**
	 * Stack used for operations.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	/**
	 * Visitor used for operations upon nodes and elements.
	 */
	private INodeVisitor visitor = new INodeVisitor() {
		
		@Override
		public void visitTextNode(TextNode node) {
			try {
				String text = node.getText();
				if (!text.equals("")) requestContext.write(text);
			} catch (IOException e) {
				throw new RuntimeException("Couldn't read text node!");
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			String initialValue = node.getStartExpression().asText();
			multistack.push(variable, new ValueWrapper(initialValue));
			
			String endValue = node.getEndExpression().asText();
			Element stepValue = node.getStepExpression();
			int step = stepValue == null ? 1 : Integer.parseInt(stepValue.asText());
			
			int childrenNumber = node.numberOfChildren();
			while (multistack.peek(variable).numCompare(endValue) <= 0) {
				for (int i = 0; i < childrenNumber; i++) {
					node.getChild(i).accept(this);
				}
				multistack.peek(variable).add(step);
			}
			
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> tempStack = new Stack<>();
			
			for (Element element : node.getElements()) {
				if (element == null) {
					break;
				}
				
				String type = element.getClass().getSimpleName();
				switch (type) {
				case "ElementConstantDouble":
				case "ElementConstantInteger":
				case "ElementString":
					tempStack.push(element.asText());
					break;
					
				case "ElementVariable":
					tempStack.push(multistack.peek(element.asText()).getValue());
					break;
					
				case "ElementOperator":
					if (tempStack.size() < 2) {
						throw new IllegalStateException("Not enough operands for operation!");
					}
					ValueWrapper result = performOperation(element.asText(), 
								tempStack.pop(), new ValueWrapper(tempStack.pop())
							);
					tempStack.push(result.getValue());
					break;
					
				case "ElementFunction":
					performFunction(element.asText(), tempStack);
					break;
					
				default:
					break;
				}
			}
			
			tempStack.forEach(s -> {
				try {
					requestContext.write(s.toString());
				} catch (IOException e) {
					throw new RuntimeException("Couldn't write results!");
				}
			}); 
		}

		/**
		 * Performs the function given by parameter.
		 * 
		 * @param function function
		 * @param tempStack stack with operands
		 */
		private void performFunction(String function, Stack<Object> tempStack) {
			switch (function) {
			case "@sin":
				calculateSin(tempStack);
				return;

			case "@decfmt":
				formatNumber(tempStack);
				return;
				
			case "@dup":
				tempStack.push(tempStack.peek());
				return;
				
			case "@swap":
				swapValues(tempStack);
				return;
			
			case "@setMimeType":
				requestContext.setMimeType((String) tempStack.pop());
				return; 
				
			case "@paramGet":
				parameterGet(tempStack, requestContext::getParameter);
				return;
			
			case "@pparamGet":
				parameterGet(tempStack, requestContext::getPersistentParameter);
				return;
				
			case "@pparamSet":	
				parameterSet(tempStack, requestContext::setPersistentParameter);
				return;
			
			case "@pparamDel":
				parameterDelete(tempStack, requestContext::removePersistentParameter);
				return;
				
			case "@tparamGet":
				parameterGet(tempStack, requestContext::getTemporaryParameter);
				return;
				
			case "@tparamSet":
				parameterSet(tempStack, requestContext::setTemporaryParameter);
				return;
			
			case "@tparamDel":
				parameterDelete(tempStack, requestContext::removeTemporaryParameter);
				return;
				
			default:
				throw new UnsupportedOperationException("Invalid function!");
			}
			
		}

		/**
		 * Calculates sine.
		 * 
		 * @param tempStack stack with operands
		 */
		private void calculateSin(Stack<Object> tempStack) {
			Object value = tempStack.pop();
			double x = 0d;
			if (value instanceof String) {
				x = Double.parseDouble((String) value);
			} else if (value instanceof Integer) {
				x = ((Integer) value).intValue();
			} else {
				x = (Double) value;
			}
			tempStack.push(Math.sin(Math.toRadians(x)));
		}

		/**
		 * Formats the number with 2 arguments and saves the result on stack..
		 * 
		 * @param tempStack stack with operands
		 */
		private void formatNumber(Stack<Object> tempStack) {
			String f = tempStack.pop().toString();
			String x = tempStack.pop().toString();
			DecimalFormat decfmt = new DecimalFormat(f);
			tempStack.push(decfmt.format(Double.parseDouble(x)));
		}

		/**
		 * Swaps two top-most values on stack.
		 * 
		 * @param tempStack stack with operands
		 */
		private void swapValues(Stack<Object> tempStack) {
			Object a = tempStack.pop();
			Object b = tempStack.pop();
			tempStack.push(a);
			tempStack.push(b);
		}

		/**
		 * Deletes the top most parameter from the given map.
		 * 
		 * @param tempStack stack with operands
		 * @param deleter consumer defining which map is to be deleted from
		 */
		private void parameterDelete(Stack<Object> tempStack, Consumer<String> deleter) {
			deleter.accept((String) tempStack.pop());
		}

		/**
		 * Sets a paramter.
		 * 
		 * @param tempStack stack with operands
		 * @param setter Biconsumer defining where the parameter is going to be set to
		 */
		private void parameterSet(Stack<Object> tempStack, BiConsumer<String, String> setter) {
			String name = tempStack.pop().toString();
			String value = tempStack.pop().toString();
			setter.accept(name, value);
		}

		/**
		 * Gets a parameter.
		 * 
		 * @param tempStack stack with operands
		 * @param fun function defining which parameter is to be gotten from
		 */
		private void parameterGet(Stack<Object> tempStack, Function<String,String> fun) {
			Object pDefValue = tempStack.pop();
			String pName = (String) tempStack.pop();
			String pValue = fun.apply(pName);
			tempStack.push(pValue == null ? pDefValue : pValue);
		}

		/**
		 * Performs binary operation.
		 * 
		 * @param op operation
		 * @param second second operand
		 * @param first first operand
		 * @return result of the operation
		 */
		private ValueWrapper performOperation(String op, Object second, ValueWrapper first) {
			switch (op) {
			case "+":
				first.add(second);
				return first;
			
			case "-":
				first.subtract(second);
				return first;
			
			case "*":
				first.multiply(second);
				return first;
			
			case "/":
				first.divide(second);
				return first;
			
			default:
				throw new UnsupportedOperationException("Invalid operand!");
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int childrenNumber = node.numberOfChildren();
			for (int i = 0; i < childrenNumber; i++) {
				node.getChild(i).accept(this);
			}
		}

	};

	/**
	 * Constructor.
	 * 
	 * @param documentNode {@link #documentNode}
	 * @param requestContext {@link #requestContext}i
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Starts the script processing.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}