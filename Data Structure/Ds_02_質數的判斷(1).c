#include<stdio.h>
#include<stdlib.h>
#include<math.h>    //㊣ sqrt function

int main(void)
{
    int no,num,i,limit,pn=2;  //no琌砆代计  num块计  pnヘ玡┮代借计计
    int *prime;   //ノㄓ纗借计

    printf("Please insert the prime limit: ");
    scanf("%d",&num);
    prime = malloc(sizeof(int)*num);  //借计计ゼ ノ笆篈皌竚ㄓ纗
    prime[0] = 2;
    prime[1] = 3;

    if(num >= 2){         // 狦 2┪3 2蛤3
        printf("2\n");
        if(num >= 3)printf("3\n");
    }

    for(no=5;no <= num ;no+=2){    //代刚琌借计
        limit = (int)sqrt(no);
        for(i=1;i<pn;i++){
            if(prime[i]<=limit && no%prime[i]==0)break;
        }
        if(i==pn){
            printf("%d\n",no);
            prime[pn] = no;
            pn++;
        }
    }
    return 0;
}
