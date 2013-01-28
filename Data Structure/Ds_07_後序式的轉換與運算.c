#include <stdio.h>
#include <stdlib.h>

/* 定義新型態 */
typedef enum {
    lparen, rparen, plus, minus, times, divide, mod, eos, operand
    } precedence;
typedef struct stack *stackPointer;
typedef struct stack {
        precedence opt;
        stackPointer link;
        } stack;
typedef struct expression *exprPointer;
typedef struct expression {
       char ch;
       exprPointer link;
       } expression; /* 用來儲存中序式與後序式 */
        
/* 宣告 Function Prototype */
exprPointer add(exprPointer previous, char ch);
void printList(exprPointer first);
void* get_node(void);
void ret_node(stackPointer node);
void cerase(stackPointer *node);
void push(precedence item);
precedence pop(void);
precedence getToken(exprPointer *expr, char *symbol);
exprPointer saveToken(exprPointer previous, precedence token);
void postfix(void);
int eval(void);

/* 全域變數初始化 */
exprPointer expr_first, post_first;
stackPointer top = NULL;
stackPointer avail = NULL; /* 用來儲存可用的節點 */
int isp[] = { 0, 19, 14, 13, 12, 11, 10, 0}; /* 先加剪後乘除的優先權 */
int icp[] = {20, 19, 14, 13, 13, 11, 10, 0};

int main(void)
{
    FILE *fp;
    int i = 0;
    char ch;
    exprPointer previous = NULL;
    
    fp = fopen("calculate.txt","r");
    
    for(i = 0;(ch = fgetc(fp))!='\n';i++) {
        if(ch != ' ')previous = add(previous,ch);
        if(i == 0)expr_first = previous;
    }
    fclose(fp);
    add(previous,' '); /* 最後放入eos */ 
    
    printf("Infix expression: \n");
    printList(expr_first);
    postfix();
    printf("Postfix expression: \n");
    printList(post_first);
    printf("The result is %d\n",eval());
    system("pause");
    return 0;
}
exprPointer add(exprPointer previous, char ch) /* 新創節點用來儲存字串 */
{
    exprPointer current;
    
    current = (exprPointer)get_node();
    if(!previous) {
        current->ch = ch;
        return current;
    }
    else {
        previous->link = current;
        current->ch = ch;
        current->link = NULL;
        return current;
    }
}
void printList(exprPointer first)
{
    for(;first;first = first->link)
        printf("%c",first->ch);
    printf("\n");
}
void* get_node(void) /* 使用閒置中的節點 */
{
    void* node;
    if(avail) {
        node = avail;
        avail = avail->link;
    }
    else
        node = (stackPointer)malloc(sizeof(stack));
    return node;
}
void ret_node(stackPointer node) /* 歸還不使用節點 */
{
    node->link = avail;
    avail = node;
}
void cerase(stackPointer *ptr) /* 一次歸還所有串列 */
{
    stackPointer temp;
    if(*ptr) {
        temp = (*ptr)->link;
        (*ptr)->link = avail;
        avail = temp;
        *ptr = NULL;
    }
}
void push(precedence item) /* 把資料放入stack */
{
    stackPointer temp;
    temp = (stackPointer)get_node(); /* 使用get_noed 來取得可用節點 */
    temp->opt = item;
    temp->link = top;
    top = temp;
}
precedence pop(void) /* 把資料從stack中提出 */
{
    stackPointer temp = top;
    precedence item;
    if (!temp) { /* 檢查stack是否為空 */
        printf("Stack is empty.\n");
        system("pause");
        exit(-1);
    }
    item = temp->opt;
    top = temp->link;
    ret_node(temp);
    return item;
}
precedence getToken(exprPointer *expr, char *symbol) /* 把字元替代成解釋文字 */
{
    *symbol = (*expr)->ch;
    switch (*symbol) {
        case '(': (*expr) = (*expr)->link; return lparen;
        case ')': (*expr) = (*expr)->link; return rparen;
        case '+': (*expr) = (*expr)->link; return plus;
        case '-': (*expr) = (*expr)->link; return minus;
        case '*': (*expr) = (*expr)->link; return times;
        case '/': (*expr) = (*expr)->link; return divide;
        case '%': (*expr) = (*expr)->link; return mod;
        case ' ': (*expr) = (*expr)->link; return eos;
        default : (*expr) = (*expr)->link; return operand;
    }
}
exprPointer saveToken(exprPointer previous, precedence token) /* 將個運算符存入post_expr */
{
    switch (token) {
        case plus   : previous = add(previous, '+'); break;
        case minus  : previous = add(previous, '-'); break;
        case times  : previous = add(previous, '*'); break;
        case divide : previous = add(previous, '/'); break;
        case mod    : previous = add(previous, '%'); break;
        case eos    : previous = add(previous, ' '); break;
    }
    return previous;
}
void postfix(void) /* 中序轉後序處理 */
{
    char symbol;
    int count = 0;
    exprPointer previous = NULL;
    precedence token;
    
    push(eos);
    
    for(token = getToken(&expr_first, &symbol); token != eos; token = getToken(&expr_first, &symbol), count++) {
        if (token == operand)
            previous = add(previous, symbol);
        else if (token == rparen) {
            while (top->opt != lparen)
                previous = saveToken(previous, pop());
            pop(); /* 把左括號pop出來 */
        }
        else {
            while (isp[top->opt] >= icp[token])
                previous = saveToken(previous, pop());
            push(token);
        }
        if(count == 1)post_first = previous;
    }
    while ((token = pop()) != eos) {
        previous = saveToken(previous, token);
    }
    previous = add(previous, ' '); /* 將最後符號設為eos */
}
int eval(void) /* 計算後序運算式 */
{
    precedence token;
    char symbol;
    int op1, op2;
    cerase(&top); /* 重新使用stack 所以將原本的串列全部歸還 */
    for (token = getToken(&post_first, &symbol); token != eos; token = getToken(&post_first, &symbol)) {
        if (token == operand)
            push(symbol - '0'); /* 使char轉成int 以便整數計算 */
        else {
            op2 = pop();
            op1 = pop();
            switch (token) {
                case plus  : push(op1 + op2); break;
                case minus : push(op1 - op2); break;
                case times : push(op1 * op2); break;
                case divide: push(op1 / op2); break;
                case mod   : push(op1 % op2); break;
            }
        }
    }
    return pop(); /* 回傳結果 */
}
