#include<stdio.h>
#include<math.h>

int main(void)
{
	int m,n,i,j,x[255],y[255],min;
	FILE *fp;

	fp=fopen("x.txt","r"); //開啟x.txt
	for(m=0;!feof(fp);m++){ //用迴圈讀取資料並忽略\n
		fscanf(fp,"%d\n",&x[m]);
	}
	fclose(fp); //關閉檔案

	fp=fopen("y.txt","r"); //開啟x.txt
	for(n=0;!feof(fp);n++){ //用迴圈讀取資料並忽略\n
		fscanf(fp,"%d\n",&y[n]);
	}
	fclose(fp); //關閉檔案
	
	min=abs(x[0]-y[0]); //初始值 min
	for(i=0;i<m;i++){ //使用暴力法求解
		for(j=0;j<n;j++){
			if(abs(x[i]-y[j])<min)min=abs(x[i]-y[j]);
		}
	}
	printf("%d",min);
	system("pause");
	return 0;
}
