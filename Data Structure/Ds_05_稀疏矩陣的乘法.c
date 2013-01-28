#include<stdio.h>
#include<stdlib.h>
#define MAX_TERMS 101
#define MAX_SIZE 100

typedef struct {
    int row;
    int col;
    int value;
}term;

term a[MAX_TERMS],b[MAX_TERMS],c[MAX_TERMS]; //宣告矩陣為全域變數.

int COMPARE(int a, int b);
void print_matrix(term matrix[]);
void Transpose_matrix(term a[],term b[]);
void mmult(term a[], term b[], term d[]);
void storesum(term d[], int *totald, int row, int column, int *sum);

int main(void)
{
    FILE *fp;
    int i;
    
    fp=fopen("matrix_a.txt","r"); //開檔並讀取.
    for(i=0;!feof(fp);i++){
        fscanf(fp,"%d\t%d\t%d\n",&a[i].row,&a[i].col,&a[i].value);
    }
    fclose(fp);
    
    fp=fopen("matrix_b.txt","r"); //開檔並讀取.
    for(i=0;!feof(fp);i++){
        fscanf(fp,"%d\t%d\t%d\n",&b[i].row,&b[i].col,&b[i].value);
    }
    fclose(fp);
    
    printf("Matrix A:\n\n"); 
    print_matrix(a); //印出矩陣 A.
    printf("\n\nMatrix B:\n\n");
    print_matrix(b); //印出矩陣 B.
    
    mmult(a,b,c); //將A矩陣乘上B矩陣 在放入C矩陣中.
    printf("\n\n");
    
    printf("Matrix A * B:\n\n");
    print_matrix(c);
    system("pause");
    return 0;
}
/*void print_matrix(term matrix[]) //印出矩陣圖形.
{
    int i,j,k = 1;
    for(i=0;i < matrix[0].row;i++){
        for(j=0;j < matrix[0].col;j++){
            if(matrix[k].row == i && matrix[k].col == j){
                printf("%-5d ",matrix[k].value);
                k++;
            }else printf("%-5d ",0);
        }
        printf("\n");
    }
}*/
void print_matrix(term matrix[]) //印出非零項矩陣.
{
    int i;
    printf("Row   Col   Value\n");
    for(i=0;i<=matrix[0].value;i++){ //印出C矩陣
        printf("%-5d %-5d %-5d\n",matrix[i].row,matrix[i].col,matrix[i].value);
    }
}
void Transpose_matrix(term a[],term b[]) //轉置矩陣.
{
    int i, j, currentb = 1;
    
    b[0].row = a[0].col;
    b[0].col = a[0].row;
    b[0].value = a[0].value;
    
    if(a[0].value > 0) {
        for(i=0; i < a[0].col; i++){
            for (j=1; j <= a[0].value; j++){
                if (a[j].col == i) {
                    b[currentb].row = a[j].col;
                    b[currentb].col = a[j].row;
                    b[currentb].value = a[j].value;
                    currentb++;
                }
            }
        }
    }
}
void fastTranspose(term a[],term b[]) //快速版轉置矩陣.
{
    int i, j, rowTerms[100]={0}, startingPos[100]={1};
    
    if(a[0].col > 0){
        for(i=0;i <= a[0].value;i++)
            rowTerms[a[i].col]++;
        for(i=1;i < a[0].col;i++)
            startingPos[i] = startingPos[i-1] + rowTerms[i-1];
        for(i=0;i <= a[0].value;i++){
            j = startingPos[a[i].col]++;
            b[j].row = a[i].col;
            b[j].col = a[i].row;
            b[j].value = a[i].value;
        }
    }
}
void mmult(term a[], term b[], term d[]) //矩陣相乘.
{
    int i, j, column, totald = 0;
    int row_begin = 1, row = a[1].row, sum = 0;
    term new_b[MAX_TERMS];

    if (a[0].col != b[0].row){ //矩陣A的Column與矩陣B的Row相等才能相乘.
        fprintf (stderr,"Incompatible matrices\n");
        exit (1);
    }
    fastTranspose(b,new_b); //將B矩陣轉置存到 new_b.
    
    a[a[0].value+1].row = a[0].row; //設置分界點.
    new_b[b[0].value+1].row = b[0].col;
    new_b[b[0].value+1].col = 0;
    
    for (i = 1; i <= a[0].value; ) {
        sum = 0;
        column = new_b[1].row;
        for (j = 1; j <= b[0].value+1;) {
            if (a[i].row != row) {
                storesum(d, &totald, row, column, &sum);
                i = row_begin;
                for (; new_b[j].row == column; j++);
                column = new_b[j].row;
            } else if (new_b[j].row != column) {
                storesum(d, &totald, row, column, &sum);
                i = row_begin;
                column = new_b[j].row;
            } else {
                switch (COMPARE(a[i].col,new_b[j].col)) {
                    case 1: i++; break;  //前 < 後.
                    case -1: j++; break; //前 > 後.
                    case 0: if((a[i].row+a[i].col+b[j].row)%2){ //前後項相等 再判斷 i+j+k 為奇數 x (-1).
                                sum += (a[i++].value * new_b[j++].value)*(-1);
                            } else
                                sum += (a[i++].value * new_b[j++].value);
                }
            }
        }
        for (; a[i].row == row; i++);
        row_begin = i;
        row = a[i].row;
    }
    d[0].row = a[0].row;
    d[0].col = b[0].col;
    d[0].value = totald;
}
void storesum(term d[], int *totald, int row, int column, int *sum) //相加儲存.
{
    if (*sum){
        if (*totald < MAX_TERMS) {
            d[++*totald].row = row;
            d[*totald].col = column;
            d[*totald].value = *sum;
            *sum = 0;
        } else {
            fprintf(stderr, "Numbers of terms in productexceed %d\n", MAX_TERMS);
            exit(1);
        }
    }
}
int COMPARE(int a, int b) //比較前後項大小.
{
    if(a>b)return -1;
    if(a==b)return 0;
    if(a<b)return 1;
}
