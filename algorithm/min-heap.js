class MinHeap {
  constructor(array) {
    this.items = array;
  }

  peek() {
    if (!this.items.length) throw 'Heap is empty';

    return this.items[0];
  }

  poll() {
    if (!this.items.length) throw 'Heap is empty';

    let item = this.items[0];
    item[0] = item[this.items.length - 1];
    this.items.length--;
    this.shiftDown();

    return item;
  }

  add(item) {
    this.items[this.items.length] = item;
    this.shiftUp();

    return this.items;
  }

  shiftUp() {
    let i = this.items.length - 1;
    if (i === 0) {
      return;
    }

    let parentIdx = (i - 2) <= 0 ? 0 : Math.floor((i - 2) / 2);

    while (this.items[i] < this.items[parentIdx]) {
      this.swap(i, parentIdx);
      i = parentIdx;
      parentIdx = (i - 2) <= 0 ? 0 : Math.floor((i - 2) / 2);
    }
  }

  shiftDown(start = 0, end) {
    let i = start;

    while (this.items[i * 2 + 1]) {
      let smallerChildIdx = i * 2 + 1;
      if (smallerChildIdx >= end) {
        return;
      }
      // Modify the comparison here for min / max heap
      if (this.items[smallerChildIdx] > this.items[i * 2 + 2]) {
        smallerChildIdx = i * 2 + 2;
      }
      // Modify the comparison here for min / max heap
      if (this.items[i] <= this.items[smallerChildIdx]) {
        break;
      } else {
        this.swap(i, smallerChildIdx);
      }
      i = smallerChildIdx;
    }
  }

  heapify() {
    for (let start = Math.floor((this.items.length - 2) / 2); start >= 0; start--) {
      this.shiftDown(start, this.items.length);
    }
  }

  sort() {
    this.heapify();

    for (let end = this.items.length - 1; end >= 0;) {
      this.swap(0, end);
      end--;
      this.shiftDown(0, end);
    }

    return this.items;
  }

  swap(a, b) {
    let tmp = this.items[a];
    this.items[a] = this.items[b];
    this.items[b] = tmp;
  }
}

let heap = new MinHeap([44, 23, 1, 83]);
console.log(heap.add(5));
console.log(heap.sort());
