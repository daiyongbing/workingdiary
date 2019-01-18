package com.iscas.workingdiary.test.sort;

import java.util.Arrays;

/**
 * 归并排序
 */
public class MergeSort {
    /**
     * 将已排序的两个数组结合成一个有序数组
     * @param left
     * @param right
     * @return
     */
    public static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }
    /**
     * 升序
     * @param array
     * @return
     */
    public static int[] mergeSortASC(int[] array){
        if (array.length < 2) return array;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(mergeSortASC(left), mergeSortASC(right));
    }

    public static void main(String[] args){
        int[] array = {45,45,46,12,94,23,96,65,24};
        for (int a:mergeSortASC(array)){
            System.out.print(a+" ");
        }
    }
}
