function quicksort(arr) {
  if (arr.length <= 1) {
    return arr;
  }

  let pivot = arr.splice(Math.floor(arr.length / 2), 1);
  let less = [];
  let greater = [];
  let res = [];

  for (let a of arr) {
    if (a <= pivot) {
      less.push(a);
    } else {
      greater.push(a);
    }
  }

  return res.concat(quicksort(less), pivot, quicksort(greater));
}

function quicksortInPlace(arr, start = 0, end = arr.length - 1) {
  if (start >= end) {
    return;
  }

  let pivotIdx = partition(arr, start, end);

  quicksortInPlace(arr, start, pivotIdx - 1);
  quicksortInPlace(arr, pivotIdx + 1, end);

  return arr;
}

function partition(arr, start, end) {
  let pivot = arr[end];
  let i = start - 1;

  for(let j = start; j < end; j++) {
    if (arr[j] <= pivot) {
      i++;
      swap(arr, i, j);
    }
  }

  swap(arr, i + 1, end);

  return i + 1;
}

function swap(arr, i, j) {
  let tmp = arr[i];
  arr[i] = arr[j];
  arr[j] = tmp;
}

console.log(quicksort([3, 1, 43, 5, 123, 6, 231, 0]));
console.log(quicksortInPlace([3, 1, 43, 5, 123, 6, 231, 0]));
