import java.util.Arrays;
public class selectionsort {
    public static int[] sort(int[] numbers) {
        int[] result = Arrays.copyOf(numbers, numbers.length);
        
        for (int i = 0; i < result.length - 1; i++) {
            int smallest = i;

            for (int j = i + 1; j < result.length; j++) {
                if (result[j] < result[smallest]) {
                    smallest = j;
                }
            }
            
            int temp = result[i];
            result[i] = result[smallest];
            result[smallest] = temp;
        }

        return result;
    }
}