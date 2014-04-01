package methods;

import net.sourceforge.jeval.EvaluationException;

import org.json.*;

import functions.Function;

/**
 * Serves as a template for any equation solving method.
 * @author halzate93
 *
 */
public abstract class Method {
	
	/**
	 * Is the unique EMethod value associated with an implementation
	 * of this abstract class. 
	 */
	private EMethod methodDescriptor;
	
	/**
	 * The max error tolerance that this run will allow.
	 */
	private float tolerance;
	
	/**
	 * This is the function to work with while looking for roots. 
	 */
	private Function function;
	
	public float getTolerance(){
		return tolerance;
	}
	
	protected void setTolerance(float tolerance){
		this.tolerance = tolerance;	
	}
	
	public Function getFunction(){
		return function;
	}
	
	public EMethod getMethodDescriptor(){
		return methodDescriptor;
	}
	
	public void setMethodDescriptor(EMethod descriptor){
		this.methodDescriptor = descriptor;
	}
	
	/**
	 * Verifies the current setup for the method and then runs the specific
	 * implementation of each method.
	 * @return a JSONObject containing the information of this run.
	 * @throws MissingParametersException If any required parameter is missing the 
	 * execution fails.
	 */
	public JSONObject run() throws MissingParametersException{
		EParameter[] missingParams = checkParameters();
		if(missingParams.length > 0)
			throw new MissingParametersException(missingParams, getMethodDescriptor());
		else
			return solve();
	}
	
	/**
	 * Runs the determined equation solving method.
	 * @return A JSONObject containing the specific output for this run and method.
	 */
	public abstract JSONObject solve();
	
	/**
	 * Checks if all the required parameters are set.
	 * @return all the missing parameters.
	 */
	public abstract EParameter[] checkParameters();
	
	/**
	 * Configures the method according to the JSON description sent, only changes according
	 * to the values contained in the JSON, if a parameter is not included, the previous
	 * value will be kept.
	 * This method is meant to be override by every implementation.
	 * @param parameters the JSONObject that contains the configuration.
	 */
	public void setup(JSONObject parameters) throws InvalidParameterException{
		if(parameters.has("function")){
			String value = parameters.getString("function");
			try {
				function = new Function(value);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (EvaluationException e) {
				throw new InvalidParameterException(EParameter.Function, value);
			}
		}
	}
}