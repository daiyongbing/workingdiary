package com.iscas.workingdiary.test.sort;

/**
 * 希尔排序
 */
public class SellSort {
    /**
     * 升序
     * @param array
     * @return
     */
    public static int[] shellSortASC(int[] array){
        int len = array.length;
        int temp, gap = len / 2;
        while (gap > 0) {
            for (int i = gap; i < len; i++) {
                temp = array[i];
                int preIndex = i - gap;
                while (preIndex >= 0 && array[preIndex] > temp) {
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }
                array[preIndex + gap] = temp;
            }
            gap /= 2;
        }
        return array;
    }

    /**
     * 降序
     * @param array
     * @return
     */
    public static int[] shellSortDESC(int[] array){
        int len = array.length;
        int temp, gap = len / 2;
        while (gap > 0) {
            for (int i = gap; i < len; i++) {
                temp = array[i];
                int preIndex = i - gap;
                while (preIndex >= 0 && array[preIndex] < temp) {
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }
                array[preIndex + gap] = temp;
            }
            gap /= 2;
        }
        return array;
    }

    public static void main(String[] args){
        int[] array = {45,45,46,12,94,23,96,65,24};
        for (int a:shellSortASC(array)){
            System.out.print(a+" ");
        }
        System.out.print("\n");
        for (int a:shellSortDESC(array)){
            System.out.print(a+" ");
        }
    }
}
