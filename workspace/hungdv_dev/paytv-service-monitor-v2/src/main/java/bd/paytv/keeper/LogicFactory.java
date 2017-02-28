package bd.paytv.keeper;

public class LogicFactory {
	public static ILogicer getLogicer(String logicerType){
		ILogicer logicer = null;
		if(logicerType.equalsIgnoreCase("THREESIGMARULE")){
			logicer = new ThreeSigmaRule();
		}
		// add some more logicer type here.
		return logicer;
	}
}
