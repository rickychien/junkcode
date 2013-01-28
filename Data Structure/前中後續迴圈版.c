int top ;
intNodePtr stack[100];
void push(intNodePtr item)
{
    stack[++top] = item;
}
intNodePtr pop()
{
    if(top == -1){return NULL;}
    return stack[top--];
}
void in_order(intNodePtr nn)
{
    top = -1;
    while(1){
        for(;nn;nn = nn->leftchild)
            push(nn);
        nn = pop();
        if(!nn)break;
        printf("%d",nn->data);
        nn = nn->rightchild;
   }
}
void pre_order(intNodePtr nn)
{
    top = -1;
    while(1){
        for(;nn;nn = nn->leftchild){
            printf("%d",nn->data);
            push(nn);
        }
        nn = pop();
        if(!nn)break;
        nn = nn->rightchild;
   }
}
void post_order(intNodePtr nn)
{
    top = -1;
    intNode temp;
    intNodePtr tempPtr;
    while(1){
        for(;nn;nn = nn->leftchild)
            push(nn);
        nn = pop();
        if(!nn)break;
        if(!nn->rightchild)printf("%d",nn->data);
        else {
            temp = *nn;
            temp.rightchild = NULL;
            tempPtr = &temp;
            push(tempPtr);
        }
        nn = nn->rightchild;
   }
}
