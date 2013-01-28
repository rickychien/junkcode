#include<stdio.h>
#include<stdlib.h>

int main(void)
{
	int VINGT,CINQ,TRENTE,V,I,N,G,T=1,C,Q,R=0,E,count=0; //count ¥Î¨Ó­pºâ¦¸¼Æ

	for(V=8;V<=9;V++){   //§PÂ_ V
		for(I=2;I<=8;I+=2){   //§PÂ_ I
			if(I!=V){
				for(N=2;N<=9;N++){   //§PÂ_ N
					if(N!=I&&N!=V){
						for(G=2;G<=9;G++){   //§PÂ_ G
							if(G!=V&&G!=I&&G!=N){
								for(C=2;C<=9;C++){   //§PÂ_ C
									if(C!=V&&C!=I&&C!=N&&C!=G&&C!=T){
										for(Q=2;Q<=9;Q++){   //§PÂ_ Q
											if(Q!=V&&Q!=I&&Q!=N&&Q!=G&&Q!=T&&Q!=C){
												for(E=3;E<=9;E+=2){   //§PÂ_ E
													if(E!=V&&E!=I&&E!=N&&E!=G&&E!=T&&E!=C&&E!=Q){
														count++;
														VINGT=V*10000+I*1000+N*100+G*10+T;
														CINQ=C*1000+I*100+N*10+Q;
														TRENTE=T*100000+R*10000+E*1000+N*100+T*10+E;
														if(TRENTE==VINGT+CINQ+CINQ){
															printf("V = %d\n",V);
															printf("I = %d\n",I);
								        					printf("N = %d\n",N);
															printf("G = %d\n",G);
															printf("T = %d\n",T);
															printf("C = %d\n",C);
															printf("Q = %d\n",Q);
															printf("R = %d\n",R);
															printf("E = %d\n",E);
															printf("¦¸¼Æ: %d",count);
														}
									               	}
												}
											}
										}
									}
								}
							}
						}
					}
				}
            }
		}
	}
	system("pause");
	return 0;
}
