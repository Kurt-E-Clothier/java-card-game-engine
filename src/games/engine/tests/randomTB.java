package games.engine.tests;

import games.Strings;
import games.engine.*;
import games.engine.plugin.*;
import games.engine.util.*;

public final class randomTB {
	
	private randomTB(){}
	
	public static void test(final String string) {
		System.out.println(string);
	}
	
	public static void main(String[] args) {
		
		System.out.println("-- Random Testing --");
		
		Operation.Parameter.Value sValue = new Operation.Parameter.Value("hello");
		Operation.Parameter.Value iValue = new Operation.Parameter.Value(1);
		
		test(String.class.cast(sValue.get()));
		test(String.class.toString());
		
		test(sValue.get().getClass().toString());
		test(String.class.cast(sValue.get()).getClass().toString());
		test(sValue.getClass().toString());
		test(sValue.getParamClass().toString());
		test(sValue.getParamClass().toString());
		
		//String s = sValue.getParamClass().cast(sValue.get());
	}

}
