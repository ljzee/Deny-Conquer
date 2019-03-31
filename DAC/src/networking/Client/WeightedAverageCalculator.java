package networking.Client;

public class WeightedAverageCalculator {
	private double alpha;
	private Long currentAverage;
	
	public WeightedAverageCalculator(double alpha) {
		this.alpha = alpha;
	}
	
	public void updateAverage(long value) {
		if(currentAverage == null) {
			currentAverage = new Long(value);
		}
		long newValue = (long) ((1.0 - alpha)*currentAverage.longValue()) + (long) (alpha * value);
		currentAverage = new Long(newValue);
	}
	
	public long getAverage() {
		return currentAverage.longValue();
	}
	
}
