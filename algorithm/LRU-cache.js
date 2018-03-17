class Node {
  constructor(val) {
    this.val = val;
    this.prev = null;
    this.next = null;
  }
}

class LRU {
  constructor(size) {
    this.size = size;
    this.front = null;
    this.rear = null;
    this.hash = new Map();
  }

  push(val) {
    if (!this.hash.has(val)) {
      let tmpNode = this.front;
      this.front = new Node(val);
      this.front.next = tmpNode;

      if (tmpNode) {
        tmpNode.prev = this.front;
      }

      this.hash.set(val, this.front);

      if (!this.rear) {
        this.rear = this.front;
      }

      if (this.hash.size > this.size) {
        // Remove last node in hash table
        this.hash.delete(this.rear.val);

        // Remove the last node in linked list
        this.rear = this.rear.prev;
        this.rear.next = null;
      }
    } else {
      // Finding existing node in hash table takes O(1)
      let node = this.hash.get(val);

      // If it's at the front node
      if (!node.prev) {
        return;
      }

      // Remove existing node from current position
      node.prev.next = node.next;

      if (node.next) {
        node.next.prev = node.prev;
      } else {
        this.rear = this.rear.prev;
      }

      // Move existing node to the front
      let tmpNode = this.front;
      this.front.prev = node;
      this.front = node;
      this.front.next = tmpNode;
    }
  }

  print() {
    let node = this.front;
    let res = [];

    while (node) {
      res.push(node.val);
      node = node.next;
    }

    console.log(res.join(' '));
  }
}

let cache = new LRU(3);
cache.push(1);
cache.push(2);
cache.push(3);
cache.push(1);
cache.push(4);
cache.push(4);
cache.push(2);
cache.print(); // 2 4 1
