package hu.advancedweb;

import com.google.common.collect.ImmutableList;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sashee on 11/22/14.
 */
public class Main {

	private static List<Variable> makeVariables(){
		ImmutableList.Builder<Variable> result=ImmutableList.builder();
		result.add(new Variable("X1").weight(9));
		result.add(new Variable("X2").weight(5));
		result.add(new Variable("X3").weight(6));
		result.add(new Variable("X4").weight(4));
		return result.build();
	}

	private static void printResult(Optimisation.Result result){
		System.out.println("X1: "+result.get(0));
		System.out.println("X2: "+result.get(1));
		System.out.println("X3: "+result.get(2));
		System.out.println("X4: "+result.get(3));

		System.out.println("Revenue: " + result.getValue());
	}

	private static void solve(boolean IP){
		List<Variable> variables=makeVariables();

		final ExpressionsBasedModel model = new ExpressionsBasedModel();
		for(Variable v:variables){
			model.addVariable(v);
		}

		model.setMaximisation(true);

		{
			Expression expression = model.addExpression("C1").upper(10);
			expression.setLinearFactor(variables.get(0), 6);
			expression.setLinearFactor(variables.get(1), 3);
			expression.setLinearFactor(variables.get(2), 5);
			expression.setLinearFactor(variables.get(3), 2);
		}

		for(Variable v:variables){
			Expression expression = model.addExpression("V_1_"+v.getName()).lower(0);
			expression.setLinearFactor(v, 1);
		}

		for(Variable v:variables){
			Expression expression = model.addExpression("V_2_"+v.getName()).upper(1);
			expression.setLinearFactor(v, 1);
		}

		if(IP) {
			for (Variable v : model.getVariables()) {
				v.setInteger(true);
			}
		}

		printResult(model.solve());
	}

	public static void main(String[] args){
		System.out.println("======= Solving the LP problem =======");
		solve(false);
		System.out.println("======= Solving the IP problem =======");
		solve(true);
	}
}
