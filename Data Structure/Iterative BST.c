#include <stdio.h>
#include <stdlib.h>
#define MAX_STACK_SIZE 100
#define DATASIZE 9

typedef struct Node *BinTreePtr;    //Definition Node
typedef struct Node {
    BinTreePtr leftchild;
    BinTreePtr rightchild;
    int data;
}BinTree;
void Insert(BinTreePtr*, int);
BinTreePtr Search(BinTreePtr, int);
void CreatBinTree(int, int []);

BinTreePtr stack[MAX_STACK_SIZE];   //Declare Globle Variable
BinTreePtr root = NULL;

int main() {
    int data[DATASIZE] = {1,3,5,7,9,11,13,15,17}, key;
    BinTreePtr target;
    CreatBinTree(DATASIZE, data);
    while (1){
        printf("Input the data you want to search: ");
        scanf("%d", &key);
        if (target = Search(root, key)) {
            printf("found! %d\n", target->data);
        } else {
            printf("Not found!\n");
        }
    }
}

void Insert(BinTreePtr *node, int data) { //iterative BST_Insert
    BinTreePtr temp_node = *node, previous_node, temp;
    while (temp_node) {
        previous_node = temp_node;
        if (data == temp_node->data) {
            return;    // if key data present
        } else if (data < temp_node->data) {
            temp_node = (temp_node)->leftchild;
        } else {
            temp_node = (temp_node)->rightchild;
        }
    }
    temp_node = previous_node;  // 找到要插入位置的上一個node
    if (!(*node)||temp_node) { // 如果BST是空的或 key data 不存在
        temp = (BinTreePtr)malloc(sizeof(BinTree));
        if (!temp) {    //檢查是否 malloc() 失敗
            fprintf(stderr, "Memory Allocate Error!");
            system("pause");
            exit(1);
        }
        temp->data = data;
        temp->leftchild = temp->rightchild = NULL;
        if (*node) {
            (data < temp_node->data) ? (temp_node->leftchild = temp) : (temp_node->rightchild = temp);
        }else *node = temp;
    }
}
/*BinTreePtr BST_Delete (BinTree *bst, BinTreePtr *root, int data) { //iterative BST_Delete
    BinTreePtr *node = NULL;
    BinTreePtr *right_Node;
    BinTreePtr *left_Node;
    int result;
    while (*root) {
        result = bst->compare((*root)->data, data);
        if (result == 0) {
            right_Node = (*root)->right;
            left_Node = (*root)->left;
            if (*right_Node) {
                *node = bst->newnode( rtNode->data );
                node->left = right_Node->left;
                node->right = left_Node->right;
                while (*right_Node->left) {
                    right_Node = right_Node->left;
                }
                rtNode->left = left_Node;
            } else if (*left_Node) {
                *node = bst->newnode(left_Node->data);
                node->left = left_Node->left;
                node->right = left_Node->right;
            } else {
                // clear data
            }
        } else if (result > 0) {
            root = (*root)->left;
        } else {
            root = (*root)->right;
        }
    }
    return node;
}*/
BinTreePtr Search(BinTreePtr root, int data) {  //iterative BST_search
    while (root) {
        if (data == root->data) {
            return root;
        } else if (data < root->data) {
            root = root->leftchild;
        } else {
            root = root->rightchild;
        }
    }
    return NULL; // if empty or not found
}
void CreatBinTree(int len, int data[]) {
    int i;
    for (i = 0; i < len; i++)
        Insert(&root, data[i]);
}
