package d2;
import java.util.ArrayList;
import java.util.Arrays;


public class RandomPickerExperiment {
	public static void main(String[] args) {
		int sum = 0;
		long s = System.currentTimeMillis();
		for (int i = 0 ; i < 10000000; i++) {
			ArrayList<Double> rands = new ArrayList<Double>();
			for (int j = 0 ; j < 4; j++) {
				double rand = Math.random();
				rands.add(rand);
			}
			sum += rands.size();
		}
		System.out.println(System.currentTimeMillis()-s);
		System.out.println(sum);
		
		
	}
	
}
