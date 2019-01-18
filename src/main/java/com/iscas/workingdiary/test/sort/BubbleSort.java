package com.iscas.workingdiary.test.sort;

import java.io.Serializable;

/**
 * 冒泡排序
 */
public class BubbleSort implements Serializable {
    private static final long serialVersionUID = -3868448288308714454L;

    public static int[] bubbleSort(int[] array){
        int count = 0;
        for (int j=array.length;j>0;j--){
            for (int i=0; i<array.length-1;i++){
                int temp = array[i];
                if (temp>array[i+1]){
                    array[i]=array[i+1];
                    array[i+1]=temp;
                }
                count++;
            }
        }
        System.out.println("count:"+count);
        return array;
    }

    public static int[] bubbleSort2(int[] array)
    {
        int temp = 0;
        int size = array.length;
        int count=0;
        for(int i = 0 ; i < size-1; i ++)
        {
            for(int j = 0 ;j < size-1-i ; j++)
            {
                if(array[j] > array[j+1])
                {
                    temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
                count++;
            }
        }
        System.out.println("count:"+count);
        return array;
    }

    public static void main(String[] args){
        int[] array = {45,45,46,12,94,23,96,65,24};
        for (int i:bubbleSort2(array)){
            System.out.println(i);
        }
    }
}
