#include <stdio.h>
#include <stdlib.h>
#define MAX_SIZE 12

typedef struct node *nodePtr;
typedef struct node {
	int data;
	nodePtr link;
};

int main(int argc, const char *argv[])
{
	short int out[MAX_SIZE];
	nodePtr seq[MAX_SIZE];
	nodePtr x, y, top;
	int i, j;
	int a[] = {0, 3, 6, 8, 7, 6, 3, 2, 11};
	int b[] = {4, 1, 10, 9, 4, 8, 5, 11, 0};

	for (i = 0; i < MAX_SIZE; i++) {
		out[i] = 1;
		seq[i] = NULL;
	}

	for(i = 0; i < 9; i++) {
		x = (nodePtr)malloc(sizeof(*x));
		x->data = b[i];
		x->link = seq[a[i]];
		seq[a[i]] = x;
		x = (nodePtr)malloc(sizeof(*x));
		x->data = a[i];
		x->link = seq[b[i]];
		seq[b[i]] = x;

	}

	for (i = 0; i < 9; i++) {
		if(out[i]) {
			printf("\nNew class: %5d", i);
			out[i] = 0;
			x = seq[i];
			top = NULL;
			for(;;) {
				while (x) {
					j = x->data;
					if (out[j]) {
						printf("%5d", j);
						out[j] = 0;
						y = x->link;
						x->link = top;
						top = x;
						x = y;
					}
					else
						x = x->link;
				}	
				if (!top) break;
				x = seq[top->data];
				top = top->link;
			}
		}
	}
	printf("\n");
	system("pause");
	return 0;
}
