#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#define MIN2(x,y) ((x) < (y) ? (x) : (y))
#define MAX_VERTEXS 10

typedef struct node *nodePointer;
typedef struct node {
        int vertex;
        nodePointer link;
        } node;

void ADJ_Matrices(int graph[], int n);
void print_Matrices(void);
void ADJ_List(int graph[], int n);
void print_List(void);
void Matrices_Dfs(int v);
void List_Dfs(int v);
void Matrices_bicon(int u, int v);
void List_bicon(int u, int v);
void push(int x, int y);
void pop(int *x, int *y);
void init(void);
void insert(nodePointer *ptr, int vertex);

bool visited[MAX_VERTEXS];
bool matrices[MAX_VERTEXS][MAX_VERTEXS];
nodePointer list[MAX_VERTEXS];
int num, dfn[MAX_VERTEXS], low[MAX_VERTEXS];
int stack[100], top;

int main(void)
{
    FILE *fp;
    int graph[MAX_VERTEXS * MAX_VERTEXS], count;
    
    fp = fopen("graph.txt","r");
    for(count = 0; !feof(fp); count++)
        fscanf(fp,"%d",&graph[count]);
    fclose(fp);

    printf("Adjacency Matrices:\n\n");
    ADJ_Matrices(graph, count);
    print_Matrices();
    printf("\nMatrices Depth First Search:\n");
    Matrices_Dfs(0);
    printf("\n\nMatrices Biconnected Components Graph:\n\n");
    init();
    Matrices_bicon(0,-1);
    
    printf("\n\nAdjacency List:\n\n");
    ADJ_List(graph, count);
    print_List();
    printf("\nList Depth First Search:\n");
    List_Dfs(0);
    printf("\n\nList Biconnected Components Graph:\n\n");
    init();
    List_bicon(0,-1);
    
    system("pause");
    return 0;
}
void ADJ_Matrices(int graph[], int n) /* Adjacency Matrices 來表示相連的邊 */
{
    int i;
    for(i = 0; i < n; i += 2) {
        matrices[graph[i]][graph[i+1]] = 1;
        matrices[graph[i+1]][graph[i]] = 1;
    }
}
void print_Matrices(void) /* 印出矩陣版邊 */
{
    int i, j;
    printf(" \\  ");
    for(i = 0; i < MAX_VERTEXS; i++)
        printf("(%d)",i);
    printf("\n");
    for(i = 0; i < MAX_VERTEXS; i++) {
        for(j = 0; j < MAX_VERTEXS; j++) {
            if(j == 0)printf("(%d)",i);
            printf("%3d",matrices[i][j]);
        }
        printf("\n");
    }
}
void ADJ_List(int graph[], int n) /* Adjacency List 來表示相連的邊 */
{
    int i;
    for(i = 0; i < n; i += 2) {
        insert(&list[graph[i]], graph[i+1]);
        insert(&list[graph[i+1]], graph[i]);
    }
}
void print_List(void) /* 印出List的邊 */
{
    int i;
    nodePointer w;

    for(i = 0; i < MAX_VERTEXS; i++) {
        printf("(%d)->",i);
        for(w = list[i]; w; w = w->link) {
            printf("%d",w->vertex);
            if(w->link)printf("->");
        }
        printf("\n");
    }
}
void Matrices_Dfs(int v) /* Matrices 深度搜尋 */
{
    int i;
    visited[v] = true;
    printf("%-3d",v);
    for(i = 0; i < MAX_VERTEXS; i++)
        if(!visited[i] && matrices[v][i])
            Matrices_Dfs(i);
}
void List_Dfs(int v) /* List 深度搜尋 */
{
    nodePointer w;
    visited[v] = true;
    printf("%-3d",v);
    for(w = list[v]; w; w = w->link)
        if(!visited[w->vertex])
            List_Dfs(w->vertex);
}
void Matrices_bicon(int u, int v) /* Matrices representation */
{
    int i, w, x, y;
    dfn[u] = low[u] = num++;
    for(i = 0; i < MAX_VERTEXS; i++) {
        for(w = i; w < MAX_VERTEXS && !matrices[u][w]; w++);
        i = w;
        if(w == MAX_VERTEXS)break; /* w == MAX_VERTEXS時已將matrices的一行跑完 所以break出迴圈 */
        if(v != w && dfn[w] < dfn[u])
            push(u,w);
        if(dfn[w] < 0) { /* w 未被拜訪過 */
            Matrices_bicon(w,u);
            low[u] = MIN2(low[u],low[w]);
            if(low[w] >= dfn[u]) {/* 若low[w] >= dfn[u] 則為連結點*/
                printf("New biconnected component:");
                do {
                    pop(&x,&y);
                    printf(" <%d,%d>",x,y);
                } while(!((x == u) && (y == w)));
                printf("\n");
            }
        }
        else if(w != v) low[u] = MIN2(low[u],dfn[w]);
    }
}
void List_bicon(int u, int v) /* List representation */
{
    nodePointer ptr;
    int w,x,y;
    dfn[u] = low[u] = num++;
    for(ptr = list[u]; ptr; ptr = ptr->link) {
        w = ptr->vertex;
        if(v != w && dfn[w] < dfn[u])
            push(u,w);
        if(dfn[w] < 0) { /* w 未被拜訪過 */
            List_bicon(w,u);
            low[u] = MIN2(low[u],low[w]);
            if(low[w] >= dfn[u]) {/* 若low[w] >= dfn[u] 則為連結點*/
                printf("New biconnected component:");
                do {
                    pop(&x,&y);
                    printf(" <%d,%d>",x,y);
                } while(!((x == u) && (y == w)));
                printf("\n");
            }
        }
        else if(w != v) low[u] = MIN2(low[u],dfn[w]);
    }
}
void push(int x, int y)
{
    stack[top++] = x;
    stack[top++] = y;
}
void pop(int *x, int *y)
{
    *y = stack[--top];
    *x = stack[--top];
}
void init(void) /* 初始化 visited[], dfn[], low[], num */
{
    int i;
    for(i = 0; i < MAX_VERTEXS; i++) {
        visited[i] = false;
        dfn[i] = low[i] = -1;
    }
    num = 0;
}
void insert(nodePointer *ptr, int vertex) /* 插入節點並創造 node */
{
    nodePointer temp, temp_ptr = *ptr;
    if(!*ptr) { /* 若*ptr為NULL則表示vertex第一次出現 */
        temp = (nodePointer)malloc(sizeof(node));
        temp->vertex = vertex;
        temp->link = NULL;
        *ptr = temp; /* 需改變 list[]的指標內容所以使用雙重指標 */
    }
    else {/* 否則往後插入新的節點 */
        for(;vertex > temp_ptr->vertex && temp_ptr->link; temp_ptr = temp_ptr->link);
        temp = (nodePointer)malloc(sizeof(node));
        temp->vertex = vertex;
        temp->link = NULL;
        temp_ptr->link = temp;
    }
}
