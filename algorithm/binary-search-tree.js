class Node {
  constructor(value) {
    this.left = null;
    this.right = null;
    this.value = value;
  }
}

class BST {
  constructor(rootValue) {
    this.root = rootValue ? this.insert(rootValue) : null;
  }

  insert(value) {
    if (!this.root) {
      this.root = new Node(value);
      return this.root;
    }

    let node = this.root;

    while (node) {
      if (value <= node.value) {
        if (node.left) {
          node = node.left;
        } else {
          node.left = new Node(value);
          break;
        }
      } else {
        if (node.right) {
          node = node.right;
        } else {
          node.right = new Node(value);
          break;
        }
      }
    }
  }

  find(node, value) {
    while (node) {
      if (node.value < value) {
        if (!node.left) {
          return false;
        } else {
          node = node.left;
        }
      } else if (node.value > value) {
        if (!node.right) {
          return false;
        } else {
          node = node.right;
        }
      } else {
        return true;
      }
    }

    return false;
  }

  printInorderRecursion(node = this.root) {
    if (node.left) {
      this.printInorderRecursion(node.left);
    }

    process.stdout.write(node.value + ' ');

    if (node.right) {
      this.printInorderRecursion(node.right);
    }
  }

  printInorderIterative(node = this.root) {
    let stack = [];
    let done = false;

    while (!done) {
      if (node) {
        stack.push(node);
        node = node.left;
      } else {
        if (stack.length) {
          node = stack.pop();
          process.stdout.write(node.value + ' ');
          node = node.right;
        } else {
          done = true;
        }
      }
    }
  }

  printPreorderRecursion(node = this.root) {
    process.stdout.write(node.value + ' ');

    if (node.left) {
      this.printPreorderRecursion(node.left);
    }

    if (node.right) {
      this.printPreorderRecursion(node.right);
    }
  }

  printPreorderIterative(node = this.root) {
    let stack = [];
    let done = false;

    while (!done) {
      if (node) {
        process.stdout.write(node.value + ' ');
        stack.push(node);
        node = node.left;
      } else {
        if (stack.length) {
          node = stack.pop();
          node = node.right;
        } else {
          done = true;
        }
      }
    }
  }

  printPostorderRecursion(node = this.root) {
    if (node.left) {
      this.printPostorderRecursion(node.left);
    }

    if (node.right) {
      this.printPostorderRecursion(node.right);
    }

    process.stdout.write(node.value + ' ');
  }

  printPostorderIterative(node = this.root) {
    let stack = [];
    let done = false;

    while (!done) {
      if (node) {
        stack.push(node);
        node = node.left;
      } else {
        if (stack.length) {
          node = stack.pop();
          node = node.right;
        } else {
          done = true;
        }
      }
    }
  }
}

let bst = new BST();
bst.insert(15);
bst.insert(5);
bst.insert(13);
bst.insert(6);
bst.insert(1);
bst.insert(7);
bst.printInorderRecursion();
console.log('')
bst.printInorderIterative();
console.log('')
bst.printPreorderRecursion();
console.log('')
bst.printPreorderIterative();
console.log('')
bst.printPostorderRecursion();
console.log('')
bst.printPostorderIterative();
console.log('')
