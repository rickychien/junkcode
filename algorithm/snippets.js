function BFS(adjacentMatrix, startNode, targetNode) {
  let queue = [];
  let visited = [];

  queue.push(startNode);
  visited[startNode] = true;

  while (queue.length) {
    let current = queue.shift();

    if (current === targetNode) {
      // found
    }

    for (let i = 0; i < adjacentMatrix.length; i++) {
      if (adjacentMatrix[current][i] && !visited[i]) {
        visited[i] = true;
        queue.push(i);
      }
    }
  }

  return null;
};

function build2DArrayWith(n, filledContent) {
  return [...Array(n).keys()].map(i => Array(n).fill(filledContent));
}

function bubbleSort() {
  for (let i = 0; i < n; ++i) {
    for(let j = i + 1; j < n; ++j) {
        // checking
    }
  }
}