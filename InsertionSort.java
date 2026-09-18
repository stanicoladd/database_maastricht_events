import java.util.Arrays;
public class InsertionSort {
public static int[] sort(int[] numbers) {
int[] result = Arrays.copyOf(numbers, numbers.length);
for (int i = 1; i < result.length; i++) {
int current = result[i];
int j = i - 1;
while (j >= 0 && result[j] > current) {
result[j + 1] = result[j];
j--;
}
result[j + 1] = current;
}
return result;
}
}