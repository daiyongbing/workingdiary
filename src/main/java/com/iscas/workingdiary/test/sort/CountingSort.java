package com.iscas.workingdiary.test.sort;

import java.util.Arrays;

/**
 * 计数排序
 */
public class CountingSort {
    public static int[] CountingSortASC(int[] array) {
        if (array.length == 0) return array;
        int bias, min = array[0], max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
            if (array[i] < min)
                min = array[i];
        }
        bias = 0 - min;
        int[] bucket = new int[max - min + 1];
        Arrays.fill(bucket, 0);
        for (int i = 0; i < array.length; i++) {
            bucket[array[i] + bias]++;
        }
        int index = 0, i = 0;
        while (index < array.length) {
            if (bucket[i] != 0) {
                array[index] = i - bias;
                bucket[i]--;
                index++;
            } else
                i++;
        }
        return array;
    }
    public static void main(String[] args) {
        int[] array = {34, 96, 34, 54, 79, 61, 41, 93, 30, 13, 64};
        for (int a : CountingSortASC(array)) {
            System.out.print(a + " ");
        }
        /*System.out.print("\n");
        for (int a : CountingSortDESC(array)) {
            System.out.print(a + " ");
        }*/
    }
}
