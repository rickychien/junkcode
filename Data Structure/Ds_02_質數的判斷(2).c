#include<stdio.h>
#include<stdlib.h>
#include<math.h>

int is_prime_word(char str[])          //測試是否為質數 
{
    int i,no,limit,sum=0,count=0;
    for(i=0;;i++){      //計算輸入字串長度 
        if(str[i]!='\0'){count++;}
        else break;
    }
    for(i=0;i<count;i++){       //設定每個字母的值 
        if(str[i]>='a'&&str[i]<='z')sum += str[i]-96;   //小寫字母 
        else if(str[i]>='A'&&str[i]<='Z')sum += str[i]-38;      //大寫字母 
    }
    limit = sqrt(sum);
    for(no=2;no<=limit;no++){       //計算是否被整除  若被整除則為非質數  return 0;
        if(sum%no==0) return 0;     
    }
    return 1;                       //沒被整除完則為質數  return 1;
}

int main(void)
{
    int num,x;  
    char str[100];
    printf("輸入字串: ");
    scanf("%s",str);
    
    if(is_prime_word(str)){         //呼叫函數用if else輸出 
        printf("It is a prime word!\n");
    }else{
        printf("It is not a prime word!\n");
    }
            
    system("pause");
    return 0;
}
