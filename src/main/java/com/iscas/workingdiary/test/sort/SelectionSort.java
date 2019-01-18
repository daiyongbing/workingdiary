package com.iscas.workingdiary.test.sort;

import java.util.Collections;

/**
 * 选择排序
 */
public class SelectionSort {
    /**
     * 升序
     * @param array
     * @return
     */
    public static int[] selectionSortASC(int[] array){
        if (array.length == 0)
            return array;
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] < array[minIndex])
                    minIndex = j;
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }

    /**
     * 降序
     * @param array
     * @return
     */
    public static int[] selectionSortDESC(int[] array){
        if (array.length == 0){
            return array;
        }
        for (int i = 0; i < array.length; i++) {
            int maxIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] > array[maxIndex])
                    maxIndex = j;
            }
            int temp = array[maxIndex];
            array[maxIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }
    public static void main(String[] args){
        int[] array = {45,45,46,12,94,23,96,65,24};
        for (int a:selectionSortDESC(array)){
            System.out.print(a+" ");
        }
        System.out.print("\n");
        for (int a:selectionSortASC(array)){
            System.out.print(a+" ");
        }
    }
}
