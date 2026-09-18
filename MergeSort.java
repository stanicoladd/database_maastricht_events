import java.util.Arrays;
public class MergeSort {
public static int[] sort(int[] numbers) {
if (numbers.length <= 1) {
return Arrays.copyOf(numbers, numbers.length);
}
int middle = numbers.length / 2;
int[] left = sort(Arrays.copyOfRange(numbers, 0, middle));
int[] right = sort(Arrays.copyOfRange(numbers, middle,
numbers.length));
return merge(left, right);
}
private static int[] merge(int[] left, int[] right) {
int[] merged = new int[left.length + right.length];
int i = 0;
int j = 0;
int k = 0;
while (i < left.length && j < right.length) {
if (left[i] <= right[j]) {
merged[k] = left[i];
i++;
} else {
merged[k] = right[j];
j++;
}
k++;
}
while (i < left.length) {
merged[k] = left[i];
i++;
k++;
}
while (j < right.length) {
merged[k] = right[j];
j++;
k++;
}
return merged;
}
}