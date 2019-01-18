package com.iscas.workingdiary.test.sort;

/**
 * 插入排序
 */
public class InsertSort {
    /**
     * 升序
     * @param arrary
     * @return
     */
    public static int[] inserSortASC(int[] arrary){
        int len = arrary.length;
        int insertNum;
        for(int i=1; i<len; i++){
            insertNum = arrary[i];
            int j = i-1;
            while(j>=0 && arrary[j]>insertNum){
                arrary[j+1] = arrary[j];
                j--;
            }
            arrary[j+1] = insertNum;
        }
        return arrary;
    }

    /**
     * 降序
     * @param arrary
     * @return
     */
    public static int[] inserSortDESC(int[] arrary){
        int len = arrary.length;
        int insertNum;
        for(int i=1; i<len; i++){
            insertNum = arrary[i];
            int j = i-1;
            while(j>=0 && arrary[j]<insertNum){
                arrary[j+1] = arrary[j];
                j--;
            }
            arrary[j+1] = insertNum;
        }
        return arrary;
    }

    public static void main(String[] args){
        int[] array = {34,96,34,54,79,61,41,93,30,13,64};
        for (int a:inserSortASC(array)){
            System.out.print(a+" ");
        }
        System.out.print("\n");
        for (int a:inserSortDESC(array)){
            System.out.print(a+" ");
        }
    }
}
