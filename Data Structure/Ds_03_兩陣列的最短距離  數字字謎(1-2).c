#include<stdio.h>
#include<stdlib.h>

int find_min(int x,int y) //找最小值的function
{
	if(x<y)return x;
	else return y;
}
int main(void)
{
	int m,n,i,j,x[255],y[255],min,min_x; //min_x為暫存min值
	FILE *fp;

	fp=fopen("x.txt","r"); //開啟x.txt
	for(m=0;!feof(fp);m++){ //用迴圈讀取資料並忽略\n
		fscanf(fp,"%d\n",&x[m]);
	}
	fclose(fp); //關閉檔案

	fp=fopen("y.txt","r"); //開啟y.txt
	for(n=0;!feof(fp);n++){ //用迴圈讀取資料並忽略\n
		fscanf(fp,"%d\n",&y[n]);
	}
	fclose(fp); //關閉檔案
	
	min=abs(x[0]-y[0]); //初始值 min
	for(i=0;i<n;i++){ //使用非暴力法求解
		for(j=0;j<m;j++){
			if(x[j]>y[i])break;
		}
		if(j==m){min_x=abs(y[i]-x[j-1]);}
		else if(j==0){min_x=abs(x[0]-y[i]);}
		else {min_x=find_min(abs(y[i]-x[j-1]),abs(y[i]-x[j]));}		
		min=find_min(min_x,min);
	}
	printf("%d",min);
	system("pause");
	return 0;
}
