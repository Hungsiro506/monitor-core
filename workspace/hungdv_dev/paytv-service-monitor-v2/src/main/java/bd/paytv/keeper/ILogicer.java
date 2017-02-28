package bd.paytv.keeper;
/**
 * Base define for all Logicer.
 * @author hungdv
 *
 * @param <T>
 */
public interface ILogicer<T> {
	public boolean isOutlier(T metrics);
}
