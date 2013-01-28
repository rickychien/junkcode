#include<stdio.h>
#include<stdlib.h>

int recursive_factorial(int n);
int iterative_factorial(int n);

int main(void)
{
    int n;
    printf("Input the factorial(n): ");
    scanf("%d",&n);
    printf("Recursive vision: %d\n",recursive_factorial(n));
    printf("Recursive vision: %d\n",iterative_factorial(n));
    system("pause");
    return 0;
}

int recursive_factorial(int n)
{
    if(n==1)return 1;
    return n * recursive_factorial(n-1);
}

int iterative_factorial(int n)
{
    int sum = 1;
    while(n>0)
        sum *= n--;
    return sum;
}
