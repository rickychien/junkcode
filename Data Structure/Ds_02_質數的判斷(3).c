#include<stdio.h>
#include<stdlib.h>
#include<math.h>

int check_prime(int n);

int main(void)
{
    int n,i,count=0;
    
    printf("Input the number of Twin prime: ");
    scanf("%d",&n);
    
    for(i=3,count=0;count<n;i+=2){
        if(check_prime(i) && check_prime(i+2))count++;
    }
    printf("(%d,%d)",i-2,i);
    system("pause");
    return 0;
}

int check_prime(int n)
{
    int i,limit;
    
    limit = sqrt(n);
    for(i=2;i<=limit;i++){
        if(n%i==0)return 0;
    }
    return 1;
}
