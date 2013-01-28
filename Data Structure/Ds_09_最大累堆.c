#include <stdio.h>
#include <stdlib.h>
#define HEAP_SIZE 101
#define SWAP(a,b) {int t; t = a; a = b; b = t;} 

void insert_heap(int heap[], int data, int *n);
void delete_heap(int heap[], int *n);
void print_heap(int heap[], int n);

int main(void)
{
    FILE *fp;
    int heap[HEAP_SIZE] = {0}, data, i;
    int n = 1, count; /* n從 array的第1個開始使用  count 用來暫存node個數 */
    
    fp = fopen("heap.txt","r"); /* 開檔 */ 
    for(i = 0;!feof(fp);i++) { /* 每讀一個值就把資料放入heap */ 
        fscanf(fp,"%d",&data);
        insert_heap(heap, data, &n);  
        print_heap(heap ,n);
    }
    fclose(fp); /* 關檔 */ 
    count = n-1; /* node的個數為n-1 因為 heap[0]不使用 */
    for(i = 0;i < count;i++) {
        delete_heap(heap, &n);
        print_heap(heap ,n);
    }
    system("pause");
    return 0;
}
void insert_heap(int heap[], int data, int *n) /* 把資料插入heap並依照大小排列 */ 
{
    int i = *n;
    if(*n == HEAP_SIZE) {
        fprintf(stderr,"The heap is full.\n");
        system("pause");
        exit(1);
    }
    heap[(*n)] = data;
    if(*n != 1) {
        while(i != 1) {
            if(heap[i] > heap[i/2])  /* 如果新插入的heap值 > 父節點值則執行bubbling up */ 
                SWAP(heap[i], heap[i/2])
            i /= 2;
        }
    }
    (*n)++;
}
void delete_heap(int heap[], int *n) /* 用來刪除第一個值並bubbling up */ 
{
    int data, i = --(*n), parent = 1;
    if(*n == 0) {
        fprintf(stderr,"The heap is empty.\n");
        system("pause");
        exit(1);
    }
    heap[1] = heap[i];
    heap[i] = 0;
    while(heap[parent*2] && heap[parent*2+1]) { /* 若兩個子節點都存在則進入回圈 */ 
        if(heap[parent*2] > heap[parent*2+1]) { /* 若左子節點大於右子節點則SWAP */ 
            SWAP(heap[parent*2], heap[parent])
            parent = parent * 2;
        }
        else {
            SWAP(heap[parent*2+1], heap[parent]) /* 若右子節點大於左子節點則SWAP */ 
            parent = parent * 2 + 1;
        }
    }
}
void print_heap(int heap[], int n) /* 印出Level order的heap */
{
    int i;
    for (i = 1;i < n;i++) 
        printf("%d[%d]\t",i, heap[i]);
    printf("\n");
}
