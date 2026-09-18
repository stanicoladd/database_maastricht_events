import java.util.Arrays;
public class BubbleSort {

    public static int[] sort(int[] numbers) {
        int[] result = Arrays.copyOf(numbers, numbers.length);
        for (int end = result.length - 1; end > 0; end--) {
            for (int i = 0; i < end; i++) {
                if (result[i] > result[i + 1]) {
                    int temp = result[i];
                    result[i] = result[i + 1];
                    result[i + 1] = temp;
                }
            }
        }
        return result;
    }
}

